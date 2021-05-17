package io.github.wuzhihao7.hook;

import java.text.MessageFormat;

public class Weapon {
    int damage = 10;

    public void attack(){
        System.out.printf("对目标造成%d点伤害%n", damage);
    }
}
