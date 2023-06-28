package com.mygdx.soulknight.ability;

import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public abstract class Ability implements AbilityDrawer {
    private static int ID = 0;
    private int id;
    protected float totalTimeCooldown = 10f;
    protected float currentTimeCooldown = totalTimeCooldown;
    public Ability() {
        id = ID++;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Ability) {
            return this.id == ((Ability) o).id;
        }
        return false;
    }
    public void update(float deltaTime) {
        currentTimeCooldown += deltaTime;
        if (currentTimeCooldown > totalTimeCooldown) {
            currentTimeCooldown = totalTimeCooldown;
        }
    }
    public abstract void addAbility(Player user);
    public abstract void removeAbility(Player user);
    public float getTotalTimeCoolDown() {
        return totalTimeCooldown;
    }
    public float getCurrentTimeCoolDown() {
        return currentTimeCooldown;
    }
}
