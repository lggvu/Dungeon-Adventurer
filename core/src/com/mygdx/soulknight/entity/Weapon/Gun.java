package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

import java.util.ArrayList;

public class Gun extends Weapon {
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f;
    private String bulletTexturePath = "bullet/bullet1.png";
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
            bulletArrayList.add(new Bullet(bulletTexturePath, owner.getX(), owner.getY(), direction, bulletSpeed));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Bullet bullet : bulletArrayList) {
            bullet.update(deltaTime);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        float degree = owner.getLastMoveDirection().angleDeg(new Vector2(1, 0));

        batch.draw(texture, owner.getX() + owner.getWidth() * 0.5f, owner.getY() + owner.getHeight() * 0.25f,0, 4, 12, 8, 1, 1, degree);
//        batch.draw(texture, owner.getX() + owner.getWidth() * 0.6f, owner.getY() + owner.getHeight() * 0.25f, 12, 12);
        for (Bullet bullet : bulletArrayList) {
            bullet.draw(batch);
        }
    }

    public ArrayList<Bullet> getBulletArrayList() {
        return bulletArrayList;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public void setBulletTexturePath(String bulletTexturePath) {
        this.bulletTexturePath = bulletTexturePath;
    }
}
