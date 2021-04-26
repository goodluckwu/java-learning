package io.github.wuzhihao7.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class ReactiveStreamDemo {
    private static final Logger log = LoggerFactory.getLogger(ReactiveStreamDemo.class);

    public static void main(String[] args) throws InterruptedException {
        //创建生产者Publisher
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        //创建订阅者 Subcriber
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<Integer>() {

            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                log.info("订阅成功");
                subscription.request(1L);
                log.info("订阅方法里请求一个数据");
            }

            @Override
            public void onNext(Integer item) {
                log.info("onNext 接收到数据 item : {}", item);
                try {
                    TimeUnit.MILLISECONDS.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscription.request(1L);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("onError 出现异常");
                subscription.cancel();
            }

            @Override
            public void onComplete() {
                log.info("onComplete 所有数据接收完成");
            }
        };

        //发布者和订阅者建立订阅关系，回调订阅者的onSubcribe方法传入订阅合同
        publisher.subscribe(subscriber);

        //发布者生成数据
        for(int i = 1; i <= 1000; i++){
            log.info("生产数据 {}", i);
            //submit 是一个阻塞方法，此时会调用订阅者的onNext方法
            publisher.submit(i);
        }

        //发布者，所有数据都发布完后，关闭发送，此时会回调订阅者的onComplete方法
        publisher.close();

        Thread.currentThread().join(10000L);
    }
}
