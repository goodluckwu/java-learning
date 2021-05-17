package io.github.wuzhihao7.callback.demo1;

public abstract class Task {
    public final void executeWith(Callback callback){
        execute();
        if(callback != null){
            callback.call();
        }
    }

    protected abstract void execute();
}
