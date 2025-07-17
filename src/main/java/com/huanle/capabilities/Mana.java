package com.huanle.capabilities;


public class Mana implements IMana {
    
    private float mana;
    private float maxMana;
    private float manaRegenRate;
    private int regenCooldown;
    private static final int REGEN_DELAY = 60;
    
    public Mana() {
        this.maxMana = 100.0f;
        this.mana = this.maxMana;
        this.manaRegenRate = 0.01f;
        this.regenCooldown = 0;
    }
    
    @Override
    public boolean consumeMana(float amount) {
        if (amount < 0) return false;
        if (this.mana >= amount) {
            this.mana -= amount;
            this.regenCooldown = REGEN_DELAY;
            return true;
        }
        return false;
    }
    
    @Override
    public void restoreMana(float amount) {
        if (amount < 0) return;
        this.mana = Math.min(this.mana + amount, this.maxMana);
    }
    
    @Override
    public void setMana(float amount) {
        this.mana = Math.max(0, Math.min(amount, this.maxMana));
    }
    
    @Override
    public float getMana() {
        return this.mana;
    }
    
    @Override
    public void setMaxMana(float amount) {
        if (amount > 0) {
            float ratio = this.mana / this.maxMana;
            this.maxMana = amount;
            this.mana = Math.min(this.mana, this.maxMana);
        }
    }
    
    @Override
    public float getMaxMana() {
        return this.maxMana;
    }
    
    @Override
    public void setManaRegenRate(float rate) {
        this.manaRegenRate = Math.max(0, rate);
    }
    
    @Override
    public float getManaRegenRate() {
        return this.manaRegenRate;
    }
    
    @Override
    public void tick() {
        if (this.regenCooldown > 0) {
            this.regenCooldown--;
        } else if (this.mana < this.maxMana - 0.01f) {
            // 自动恢复魔力（使用容差值避免浮点数精度问题）
            this.restoreMana(this.manaRegenRate);
        }
    }
    
    @Override
    public boolean hasMana(float amount) {
        return this.mana >= amount;
    }

    public float getManaPercentage() {
        return this.maxMana > 0 ? this.mana / this.maxMana : 0;
    }

    public boolean isManaFull() {
        return this.mana >= this.maxMana - 0.01f;
    }

    public boolean isManaEmpty() {
        return this.mana <= 0;
    }
}