package com.huanle.capabilities;


public interface IMana {

    boolean consumeMana(float amount);

    void restoreMana(float amount);

    void setMana(float amount);

    float getMana();

    void setMaxMana(float amount);

    float getMaxMana();

    void setManaRegenRate(float rate);

    float getManaRegenRate();

    void tick();

    boolean hasMana(float amount);
}