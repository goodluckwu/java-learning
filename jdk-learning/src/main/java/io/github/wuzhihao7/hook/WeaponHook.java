package io.github.wuzhihao7.hook;

public class WeaponHook extends Weapon{
    private OnUseWeaponAttackListener onUseWeaponAttackListener;

    @Override
    public void attack() {
        super.attack();
        if(onUseWeaponAttackListener != null){
            onUseWeaponAttackListener.onUseWeaponAttack(damage);
        }
    }

    public void setOnUseWeaponAttackListener(OnUseWeaponAttackListener onUseWeaponAttackListener){
        this.onUseWeaponAttackListener = onUseWeaponAttackListener;
    }

    public interface OnUseWeaponAttackListener {
        int onUseWeaponAttack(int damage);
    }
}
