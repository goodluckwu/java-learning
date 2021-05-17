package io.github.wuzhihao7.chainofresponsibility;

import java.util.Objects;

public class DepartmentHead extends Leader{
    @Override
    public void handleRequest(int leaveDays) {
        if(leaveDays <= 7){
            System.out.printf("系主任批准你请假%s天%n", leaveDays);
        }else {
            if(Objects.nonNull(getNext())){
                getNext().handleRequest(leaveDays);
            }else {
                System.out.println("请假天数太多，没有人批准该假条！");
            }
        }
    }
}
