package io.github.wuzhihao7.callback.demo2;

public class Student {
    public void doRun(Score score, int length){
        if(length == 50){
            System.out.println("Begin to Run.");
            System.out.println("Run finished, my time spend is 6.8s.");
        }
        score.recordScore(6.8);
    }
}
