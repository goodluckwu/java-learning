package io.github.wuzhihao7.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ProcessorDemo {
    private static final Logger log = LoggerFactory.getLogger(ProcessorDemo.class);

    public static void main(String[] args) throws InterruptedException {
        //创建发布者
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        //创建Processor，既是发布者也是订阅者
        MyProcessor processor = new MyProcessor();
        //创建最终订阅者
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<Integer>() {
            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1L);
            }

            @Override
            public void onNext(Integer item) {
                log.info("onNext 从 Processor 接收过滤后的数据 item : {}", item);
                this.subscription.request(1L);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("onError 出现异常");
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                log.info("onComplete 所有数据接收完成");
            }
        };
        //建立关系，发布者和处理器，此时处理器扮演订阅者
        publisher.subscribe(processor);
        //建立关系，处理器和订阅者，此时处理器扮演发布者
        processor.subscribe(subscriber);
        //发布者发布数据
        IntStream.range(0, 5).forEach(publisher::submit);

        publisher.close();

        TimeUnit.SECONDS.sleep(10L);
    }
}

class MyProcessor extends SubmissionPublisher<Integer> implements Flow.Processor<Integer, Integer> {
    private static final Logger log = LoggerFactory.getLogger(MyProcessor.class);

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("Processor 收到订阅请求");
        this.subscription = subscription;
        this.subscription.request(1L);
    }

    @Override
    public void onNext(Integer item) {
        log.info("onNext 收到发布者数据 : {}", item);
        if(item % 2 == 0){
            this.submit(item);
        }
        this.subscription.request(1L);
    }

    @Override
    public void onError(Throwable throwable) {
        this.subscription.cancel();
    }

    @Override
    public void onComplete() {
        log.info("处理器处理完毕");
        this.close();
    }
}
