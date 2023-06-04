package com.mygdx.soulknight.entity;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.screen.MainGameScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class Monster extends SimpleCharacter {

    Weapon weapon;

    public Monster(MainGameScreen gameScreen) {
        super(gameScreen, "heros/monster-1.png");
        setSpeedRun(120f);
    }

    @Override
    public void getHit(int damage, Vector2 direction, Bullet bullet,Effect effect) {
        currentHP -= damage;

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        while (true) {
            float testX = getX() + lastMoveDirection.x * speedRun * deltaTime;
            float testY = getY() + lastMoveDirection.y * speedRun * deltaTime;
            if (!gameScreen.isMapCollision(new Rectangle(testX, testY, width, height)) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                move(lastMoveDirection.x, lastMoveDirection.y);
                break;
            }
            lastMoveDirection = new Vector2(MathUtils.random(-10, 10), MathUtils.random(-10, 10)).nor();
        }
    }

    @Override
    public void attack(Vector2 direction) {
        weapon.attack(direction);
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
