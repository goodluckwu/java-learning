package io.github.wuzhihao7.proxy;

public class SgProxy {
    private WySpecialty realSubject = new WySpecialty();

    public void display(){
        preRequest();
        realSubject.display();
        postRequest();
    }

    private void postRequest() {
        System.out.println("韶关代理婺源特产开始。");
    }

    private void preRequest() {
        System.out.println("韶关代理婺源特产结束。");
    }
}
