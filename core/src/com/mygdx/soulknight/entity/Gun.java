package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Gun extends Weapon {
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f;

    public Gun(SimpleCharacter owner) {
        super(owner);
    }

    public Gun(SimpleCharacter owner, float bulletSpeed) {
        super(owner);
        this.bulletSpeed = bulletSpeed;
    }

    public Gun(SimpleCharacter owner, String texturePath) {
        super(owner, texturePath);
    }

    public Gun(SimpleCharacter owner, String texturePath, float intervalSeconds) {
        super(owner, texturePath, intervalSeconds);
    }

    public Gun(SimpleCharacter owner, String texturePath, float intervalSeconds, float bulletSpeed) {
        super(owner, texturePath, intervalSeconds);
        this.bulletSpeed = bulletSpeed;
    }

    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            bulletArrayList.add(new Bullet("bullet/bullet1.png", owner.getX(), owner.getY(), direction, bulletSpeed));
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, owner.getX() + owner.getWidth() * 0.75f, owner.getY() + owner.getHeight() * 0.25f, 16, 16);
    }

    public ArrayList<Bullet> getBulletArrayList() {
        return bulletArrayList;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
}
