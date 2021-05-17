package io.github.wuzhihao7.chainofresponsibility;

import java.util.Objects;

public class Dean extends Leader{
    @Override
    public void handleRequest(int leaveDays) {
        if(leaveDays <= 10){
            System.out.printf("院长批准您请假%d天%n", leaveDays);
        }else {
            if(Objects.nonNull(getNext())){
                getNext().handleRequest(leaveDays);
            }else {
                System.out.println("请假天数太多，没有人批准该假条！");
            }
        }
    }
}
