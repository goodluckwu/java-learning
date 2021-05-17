package io.github.wuzhihao7.callback.demo2;

public class CallbackDemo {
    public static void main(String[] args) {
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.publishTask(student,50);
    }
}
