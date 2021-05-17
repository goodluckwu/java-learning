package io.github.wuzhihao7.callback.demo2;

public class Teacher implements Score{

    public void publishTask(Student student, int length){
        System.out.println("Teacher publish the task, run " + length + "m.");
        student.doRun(this,length);
    }
    @Override
    public void recordScore(double timeSpend) {
        if(timeSpend < 7){
            System.out.println("Well done.Excellent.");
        }else {
            System.out.println("Good.");
        }
    }
}
