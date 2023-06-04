package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.soulknight.screen.MainGameScreen;

import java.util.ArrayList;

public class Player extends SimpleCharacter {
    private int maxArmor = 6;
    private int currentArmor = maxArmor;
    private int currentMana = 200;
    private int maxMana = 200;
    private float timeHealArmor = 1f;
    private float currentHealArmor = 0;
    private ArrayList<Weapon> weapons = new ArrayList<>();
    private int currentWeaponId = 0;

    private ArrayList<Bullet> bulletArray = new ArrayList<>();




    public Player(MainGameScreen gameScreen) {
        super(gameScreen);
    }

    public Player(MainGameScreen gameScreen, String texturePath) {
        super(gameScreen, texturePath);
    }

    public Player(MainGameScreen gameScreen, String texturePath, float x, float y) {
        super(gameScreen, texturePath, x, y);
    }

    public Player(MainGameScreen gameScreen, String texturePath, float x, float y, float width, float height) {
        super(gameScreen, texturePath, x, y, width, height);
    }

    public Player(MainGameScreen gameScreen, String texturePath, float x, float y, float width, float height, int hp) {
        super(gameScreen, texturePath, x, y, width, height, hp);
    }

    public Player(MainGameScreen gameScreen, String texturePath, float x, float y, float width, float height, int hp, float speedRun) {
        super(gameScreen, texturePath, x, y, width, height, hp, speedRun);
    }

    @Override
    public void getHit(int damage, Vector2 direction,Bullet bullet,Effect effect) {
        if (currentArmor < damage) {
            currentHP = currentHP - (damage - currentArmor);
            currentArmor = 0;
        } else {
            currentArmor -= damage;
        }
        if (currentHP < 0) {
            currentHP = 0;
        }
        bulletArray.add(bullet);
        isPushed=true;

        stunTimer = stunDuration;
        isStunned = true;

    }

    @Override
    public void attack(Vector2 direction) {getCurrentWeapon().attack(direction);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

//        Armor auto heal
        currentHealArmor += deltaTime;
        if (currentArmor == maxArmor) {
            currentHealArmor = 0;
        }
        if (currentHealArmor > timeHealArmor) {
            currentHealArmor = 0;
            currentArmor++;
        }
        if (currentArmor > maxArmor) {
            currentArmor = maxArmor;
        }
        if (isStunned) {
            stunTimer -= this.deltaTime;


            if (stunTimer <= 0) {
                // Stun duration has ended, resume normal behavior
                isStunned = false;
            }

        }
        if (!bulletArray.isEmpty()) {
            float totalPushForceX = 0.0f;
            float totalPushForceY = 0.0f;
            ArrayList<Bullet> removeBulletList=new ArrayList<>();
            for (Bullet bullet : bulletArray) {
                bullet.pushTimer-=deltaTime;
                if (bullet.pushTimer>0) {
                    totalPushForceX += bullet.impactForce * bullet.getDirection().x;
                    totalPushForceY += bullet.impactForce * bullet.getDirection().y;
                }
                else{
                    removeBulletList.add(bullet);
                }
            }
            bulletArray.removeAll(removeBulletList);
            float testX = this.x+totalPushForceX;
            float testY = this.y+totalPushForceY ;

            if (!gameScreen.isMapCollision(new Rectangle(testX, testY, width, height))) {
                this.x = testX;
                this.y = testY;
            }

        }




    }

    public void switchWeapon() {
        currentWeaponId++;
        if (currentWeaponId >= weapons.size()) {
            currentWeaponId = 0;
        }
    }
    public Weapon getCurrentWeapon() {
        return weapons.get(currentWeaponId);
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public int getMaxArmor() {
        return maxArmor;
    }

    public int getCurrentArmor() {
        return currentArmor;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }
}
