package io.github.wuzhihao7.callback.demo1;

public class SimpleTask extends Task{
    @Override
    protected void execute() {
        System.out.println("Do some tasks before the callback method.");
    }
}
