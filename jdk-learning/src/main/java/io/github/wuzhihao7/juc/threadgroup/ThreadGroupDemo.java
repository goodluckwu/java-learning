package io.github.wuzhihao7.juc.threadgroup;

public class ThreadGroupDemo {
    public static void main(String[] args) {
        ThreadGroup mainThreadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup systemThreadGroup = mainThreadGroup.getParent();
        System.out.println("mainThreadGroup: " + mainThreadGroup.getName());
        System.out.println("systemThreadGroup: " + systemThreadGroup.getName());
        System.out.println("-------------------------------");
        ThreadGroup subThreadGroup1 = new ThreadGroup("subThreadGroup1");
        ThreadGroup subThreadGroup2 = new ThreadGroup(subThreadGroup1, "subThreadGroup2");
        System.out.println("parent of subThreadGroup1: " + subThreadGroup1.getParent().getName());
        System.out.println("parent of subThreadGroup2: " + subThreadGroup2.getParent().getName());
        System.out.println("-------------------------------");
        ThreadGroup tg = new ThreadGroup("subGroup 1");
        Thread t1 = new Thread(tg, "thread 1");
        Thread t2 = new Thread(tg, "thread 2");
        Thread t3 = new Thread(tg, "thread 3");
        tg = new ThreadGroup("subgroup 2");
        Thread t4 = new Thread(tg, "my thread");
        tg = Thread.currentThread().getThreadGroup();
        int agc = tg.activeGroupCount();
        System.out.println("Active thread groups in " + tg.getName() + " thread group: " + agc);
        tg.list();
    }
}
