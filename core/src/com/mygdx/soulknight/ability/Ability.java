package com.mygdx.soulknight.ability;

import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public abstract class Ability implements AbilityDrawer {
    protected float totalTimeCooldown = 10f;
    protected float currentTimeCooldown = totalTimeCooldown;
    public void update(float deltaTime) {
        currentTimeCooldown += deltaTime;
        if (currentTimeCooldown > totalTimeCooldown) {
            currentTimeCooldown = totalTimeCooldown;
        }
    }
    public abstract void addAbility(SimpleCharacter user);
    public abstract void removeAbility(SimpleCharacter user);
    public float getTotalTimeCoolDown() {
        return totalTimeCooldown;
    }
    public float getCurrentTimeCoolDown() {
        return currentTimeCooldown;
    }
}
