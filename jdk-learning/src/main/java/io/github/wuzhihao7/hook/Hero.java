package io.github.wuzhihao7.hook;

import java.text.MessageFormat;

public class Hero {
    private Weapon weaponMain;
    private final int weaponMainId;

    public Hero(Weapon weaponMain){
        this.weaponMain = weaponMain;
        this.weaponMainId = this.weaponMain.hashCode();
    }

    public void attack(){
        if(this.weaponMain.hashCode() != this.weaponMainId){
            throw new IllegalAccessError(MessageFormat.format("警告！遭到入侵！入侵者身份: {0}", this.weaponMain.hashCode()));
        }
        weaponMain.attack();
    }
}
