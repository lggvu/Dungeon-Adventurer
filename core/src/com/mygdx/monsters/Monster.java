package com.mygdx.monsters;

public class Monster {
    private int hp;
    private int atk;
    private int def;
    private int speed;

    public Monster(int hp, int atk, int def, int speed) {
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.speed = speed;
    }

    public int getHp() {
        return hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getSpeed() {
        return speed;
    }





}
