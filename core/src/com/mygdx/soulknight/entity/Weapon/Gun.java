package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.Room;

import java.util.ArrayList;

public class Gun extends Weapon {
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f/2;
    private String bulletTexturePath = "bullet/bullet1.png";

    public Gun(String texturePath, String bulletTexturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate, float bulletSpeed) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
        this.bulletTexturePath = bulletTexturePath;
        this.bulletSpeed = bulletSpeed;
        width = 12;
        height = 8;
    }


    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            subOwnerMana();
            bulletArrayList.add(new Bullet(bulletTexturePath, owner.getX(), owner.getY(), direction, bulletSpeed));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Bullet bullet : bulletArrayList) {
            bullet.update(deltaTime);
        }
        handleBulletCollision();
        dealDamageMethod();
    }

    @Override
    public void flip(boolean x, boolean y) {
        texture.flip(x, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Bullet bullet : bulletArrayList) {
            bullet.draw(batch);
        }

        if (onGround) {
            batch.draw(texture, x, y, width, height);
            return;
        }

        float degree = owner.getLastMoveDirection().angleDeg(new Vector2(1, 0));

        batch.draw(texture, owner.getX() + owner.getWidth() * 0.5f, owner.getY() + owner.getHeight() * 0.25f,0, 4, width, height, 1, 1, degree);
//        batch.draw(texture, owner.getX() + owner.getWidth() * 0.6f, owner.getY() + owner.getHeight() * 0.25f, 12, 12);

    }

    @Override
    public void dealDamageMethod() {
        if (owner == null) {
            return;
        }
        ArrayList<SimpleCharacter> listEnemy = owner.getEnemyList();
        ArrayList<Bullet> removeList;
        for (SimpleCharacter character : listEnemy) {
            removeList = new ArrayList<>();
            for (Bullet bullet : bulletArrayList) {
                if (bullet.getRectangle().overlaps(character.getRectangle())) {
                    character.getHit(damage);
                    removeList.add(bullet);
                }
            }
            bulletArrayList.removeAll(removeList);
        }
    }

    public void handleBulletCollision() {
        if (owner == null) {
            return;
        }
        ArrayList<Bullet> removeList = new ArrayList<>();
        for (Bullet bullet : bulletArrayList) {
            if (owner.getMap().isMapCollision(bullet.getRectangle())) {
                removeList.add(bullet);
            }
        }
        bulletArrayList.removeAll(removeList);
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
