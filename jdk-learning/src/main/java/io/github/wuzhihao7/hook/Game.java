package io.github.wuzhihao7.hook;

import java.lang.reflect.Field;
import java.nio.file.Watchable;

public class Game {
    public static void main(String[] args) throws Exception {
        Hero hero = new Hero(new Weapon());
        hero.attack();

        Field weapon = hero.getClass().getDeclaredField("weaponMain");
        weapon.setAccessible(true);
        Weapon weaponHook = new WeaponHook();
        ((WeaponHook)weaponHook).setOnUseWeaponAttackListener(damage -> {
            System.out.printf("damage = %d%n", damage);
            return damage;
        });
        weapon.set(hero, weaponHook);
        hero.attack();
    }
}
