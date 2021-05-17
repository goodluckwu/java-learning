package io.github.wuzhihao7.chainofresponsibility;

import java.util.Objects;
import java.util.logging.Level;

/**
 * 具体处理者1：班主任类
 */
public class ClassAdviser extends Leader{
    @Override
    public void handleRequest(int leaveDays) {
        if(leaveDays <= 2){
            System.out.printf("班主任批准您请假%d天%n", leaveDays);
        }else{
            if(Objects.nonNull(getNext())){
                getNext().handleRequest(leaveDays);
            }else {
                System.out.println("请假天数太多，没有人批准该假条！");
            }
        }
    }
}
