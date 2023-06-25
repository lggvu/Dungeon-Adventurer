package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Effect.Effect;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Effect.RegionEffect;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;

public class Gun extends Weapon {
    protected ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f/2;
    protected TextureRegion bulletTextureRegion;
    protected Animation<TextureRegion> explosionAnimation;
    protected Animation<TextureRegion> shotExplosionAnimation;

    public Gun(String texturePath, String bulletTexturePath, String explosionTexturePath, String shotExplosionTexturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate, float bulletSpeed) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
        this.bulletTextureRegion = new TextureRegion(new Texture(bulletTexturePath));
        this.bulletSpeed = bulletSpeed;
        TextureRegion[] explosionFrames = SpriteLoader.loadTextureByFileName(explosionTexturePath);
        TextureRegion[] shotExplosionFrames = SpriteLoader.loadTextureByFileName(shotExplosionTexturePath);
        this.explosionAnimation = new Animation<TextureRegion>(0.05f, explosionFrames);
        this.shotExplosionAnimation = new Animation<>(0.01f, shotExplosionFrames);
    }

    public Gun() {
        super();
    }

    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            elapsedSeconds = 0;
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
            owner.getMap().createAnExplosion(owner, gunBarrelX, gunBarrelY, 15, this.shotExplosionAnimation, false);
            bulletArrayList.add(new Bullet(bulletTextureRegion, gunBarrelX, gunBarrelY, direction, bulletSpeed));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!onGround && owner.isFlipX() != texture.isFlipY()) {
            texture.flip(false, true);
        }
        ArrayList<Bullet> removeArrayList = new ArrayList<>();
        for (Bullet bullet : bulletArrayList) {
            bullet.update(deltaTime);
            if (bullet.getDistanceGoThrough() > rangeWeapon) {
                removeArrayList.add(bullet);
            }
        }
        bulletArrayList.removeAll(removeArrayList);
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
                    character.getHit(damage);
                    character.addEffects(CharacterEffect.loadEffect(effectsName, bullet.getDirection()));
                    RegionEffect.loadRegionEffect(owner, owner.getMap(), effectsName, bullet.getX(), bullet.getY());
                    owner.getMap().createAnExplosion(owner, character.getX(), character.getY(), 30, this.explosionAnimation, false);
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
                owner.getMap().createAnExplosion(owner, bullet.getX(), bullet.getY(), 30, this.explosionAnimation, false);
                RegionEffect.loadRegionEffect(owner, owner.getMap(), effectsName, bullet.getX(), bullet.getY());
                removeList.add(bullet);
            } else if (owner.getMap().isInDoor(bullet.getRectangle())) {
                owner.getMap().createAnExplosion(owner, bullet.getX(), bullet.getY(), 30, this.explosionAnimation, false);
                RegionEffect.loadRegionEffect(owner, owner.getMap(), effectsName, bullet.getX(), bullet.getY());
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
                    owner.getMap().createAnExplosion(owner, bullet.getX(), bullet.getY(),30, this.explosionAnimation, false);
                    owner.getMap().removeDestroyableObject(object);
                    RegionEffect.loadRegionEffect(owner, owner.getMap(), effectsName, bullet.getX(), bullet.getY());
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
}
