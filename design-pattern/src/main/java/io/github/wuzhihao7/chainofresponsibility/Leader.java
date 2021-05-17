package io.github.wuzhihao7.chainofresponsibility;

/**
 * 抽象处理者：领导类
 */
public abstract class Leader {
    private Leader next;

    public void setNext(Leader next){
        this.next = next;
    }

    public Leader getNext(){
        return next;
    }

    public abstract void handleRequest(int leaveDays);
}
