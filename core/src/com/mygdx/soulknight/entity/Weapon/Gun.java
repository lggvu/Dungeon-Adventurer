package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;

public class Gun extends Weapon {
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f/2;
    private String bulletTexturePath = "bullet/bullet1.png";
    private String explosionTexturePath;
    private Animation<TextureRegion> explosionAnimation;
    private Animation<TextureRegion> shotExplosionAnimation;

    public Gun(String texturePath, String bulletTexturePath, String explosionTexturePath, String shotExplosionTexturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate, float bulletSpeed) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
        this.bulletTexturePath = bulletTexturePath;
        this.bulletSpeed = bulletSpeed;
        TextureRegion[] explosionFrames = SpriteLoader.loadTextureByFileName(explosionTexturePath);
        TextureRegion[] shotExplosionFrames = SpriteLoader.loadTextureByFileName(shotExplosionTexturePath);
        this.explosionAnimation = new Animation<TextureRegion>(0.05f, explosionFrames);
        this.shotExplosionAnimation = new Animation<>(0.01f, shotExplosionFrames);
    }


    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            subOwnerMana();
            float degree = direction.angleDeg(new Vector2(1, 0));
            float gunBarrelX, gunBarrelY;
            gunBarrelX = gunBarrelY = 0;
            if (texture.isFlipY()) {
                gunBarrelX += owner.getX() + owner.getWidth() - owner.getWeaponX();
                gunBarrelY += owner.getY() + owner.getHeight() - owner.getWeaponY();
            } else {
                gunBarrelX += owner.getX() + owner.getWeaponX();
                gunBarrelY += owner.getY() + owner.getWeaponY();
            }
            gunBarrelX += (width - origin_x) * MathUtils.cosDeg(degree);
            gunBarrelY += (width - origin_x) * MathUtils.sinDeg(degree);
            Explosion explosion = new Explosion(explosionTexturePath, gunBarrelX, gunBarrelY, this.shotExplosionAnimation);
            WorldMap.EXPLOSION_ARRAY_LIST.add(explosion);
            bulletArrayList.add(new Bullet(bulletTexturePath, gunBarrelX, gunBarrelY, direction, bulletSpeed));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!onGround && owner.isFlipX() != texture.isFlipY()) {
            texture.flip(false, true);
        }
        for (Bullet bullet : bulletArrayList) {
            bullet.update(deltaTime);
        }
        handleBulletCollision();
        dealDamageMethod();

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
        float dlX = 0;
        float dlY = 0;
        if (texture.isFlipY()) {
            dlX = owner.getX() + owner.getWidth() - (owner.getWeaponX() - origin_x);
            dlY = owner.getY() + owner.getWeaponY() - origin_y;
        } else {
            dlX = owner.getX() + owner.getWeaponX() - origin_x;
            dlY = owner.getY() + owner.getWeaponY() - origin_y;
        }
        batch.draw(texture, dlX, dlY, origin_x, origin_y, width, height, 1, 1, degree);
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
                    character.getHit(damage, bullet.getDirection(), bullet);
                    Explosion explosion=new Explosion(explosionTexturePath, character.getX(), character.getY(), this.explosionAnimation);
                    WorldMap.EXPLOSION_ARRAY_LIST.add(explosion);
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
            if (owner.getMap().isMapCollision(bullet.getRectangle(), false)) {
                removeList.add(bullet);
            } else if (owner.getMap().isInDoor(bullet.getRectangle())) {
                removeList.add(bullet);
            }
        }
        bulletArrayList.removeAll(removeList);
        while (findCollisionAndDelete()) {}
    }

    public boolean findCollisionAndDelete() {
        for (DestroyableObject object : owner.getMap().getDestroyableObjects()) {
            for (Bullet bullet : bulletArrayList) {
                if (bullet.getRectangle().overlaps(object.getRectangle())) {
                    bulletArrayList.remove(bullet);
                    owner.getMap().removeDestroyableObject(object);
                    return true;
                }
            }
        }
        return false;
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
