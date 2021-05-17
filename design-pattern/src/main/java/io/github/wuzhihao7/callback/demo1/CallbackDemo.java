package io.github.wuzhihao7.callback.demo1;

public class CallbackDemo {
    public static void main(String[] args) {
        Task simpleTask = new SimpleTask();
        simpleTask.execute();

        simpleTask.executeWith(() -> {
            System.out.println("callback called！");
        });
    }
}
