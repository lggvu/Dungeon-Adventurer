package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Effect.Effect;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Effect.RegionEffect;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Gun extends Weapon {
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f/2;
    private TextureRegion bulletTextureRegion;
    private Animation<TextureRegion> explosionAnimation;
    private Animation<TextureRegion> shotExplosionAnimation;
    private int numDestroyObject = 1;
    private int numWallCollide = 1;
    private int numEnemyHit = 1;
    private float degreeChangePerSec = 0;
    private ArrayList<Float> directionAttack = new ArrayList<>();

    public Gun(String texturePath, String bulletTexturePath, String explosionTexturePath, String shotExplosionTexturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate, float bulletSpeed) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
        this.bulletTextureRegion = new TextureRegion(new Texture(bulletTexturePath));
        this.bulletSpeed = bulletSpeed;
        TextureRegion[] explosionFrames = SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName(explosionTexturePath));
        TextureRegion[] shotExplosionFrames = SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName(shotExplosionTexturePath));
        this.explosionAnimation = new Animation<TextureRegion>(0.05f, explosionFrames);
        this.shotExplosionAnimation = new Animation<>(0.01f, shotExplosionFrames);
    }

    @Override
    public JsonObject load(JsonObject jsonObject) {
        jsonObject = super.load(jsonObject);
        initWeaponTexture(jsonObject.get("gun_texture").getAsString());
        JsonObject properties = jsonObject.get("properties").getAsJsonObject();
        bulletTextureRegion = new TextureRegion(new Texture(jsonObject.get("bullet_texture").getAsString()));
        bulletSpeed = properties.get("bullet_speed").getAsFloat();
        String explosionTexturePath = jsonObject.get("explosion_texture").getAsString();
        TextureRegion[] explosionFrames = SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName(explosionTexturePath));
        explosionAnimation = new Animation<TextureRegion>(0.05f, explosionFrames);
        String shotExplosionTexturePath = jsonObject.get("shot_explosion_texture").getAsString();
        TextureRegion[] shotExplosionFrames = SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName(shotExplosionTexturePath));
        shotExplosionAnimation = new Animation<>(0.01f, shotExplosionFrames);
        JsonElement jsonElement = properties.get("attack_directions");
        Iterator<JsonElement> directions = jsonElement.getAsJsonArray().iterator();
        while (directions.hasNext()) {
            this.addDirectionAttack(directions.next().getAsFloat());
        }
        return jsonObject;
    }

    public Gun() {
        super();
    }

    protected void shot(Vector2 direction) {
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
        gunBarrelX += (width - originX) * MathUtils.cosDeg(degree);
        gunBarrelY += (width - originX) * MathUtils.sinDeg(degree);
        owner.getMap().createAnExplosion(owner, gunBarrelX, gunBarrelY, 15, this.shotExplosionAnimation, false);
        bulletArrayList.add(new Bullet(owner, bulletTextureRegion, explosionAnimation, getCurrentDamage(), gunBarrelX, gunBarrelY, direction,
                bulletSpeed, effectsEnum, numDestroyObject, numEnemyHit, getCurrentNumWallCollide(), degreeChangePerSec, rangeWeapon, criticalRate));
    }


    public void setNumDestroyObject(int numDestroyObject) {
        this.numDestroyObject = numDestroyObject;
    }

    public void setNumWallCollide(int numWallCollide) {
        this.numWallCollide = numWallCollide;
    }

    public void setNumEnemyHit(int numEnemyHit) {
        this.numEnemyHit = numEnemyHit;
    }

    public void setDegreeChangePerSec(float degreeChangePerSec) {
        this.degreeChangePerSec = degreeChangePerSec;
    }

    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            float degree = direction.angleDeg(new Vector2(1, 0));
            for (Float deg : directionAttack) {
                deg += degree;
                shot(new Vector2(MathUtils.cosDeg(deg), MathUtils.sinDeg(deg)));
            }
            elapsedSeconds = 0;
            subOwnerMana();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        ArrayList<Bullet> removeArrayList = new ArrayList<>();
        for (Bullet bullet : bulletArrayList) {
            bullet.update(deltaTime);
            if (bullet.isStop()) {
                removeArrayList.add(bullet);
            }
        }
        bulletArrayList.removeAll(removeArrayList);
    }
    public void addDirectionAttack(float... degrees) {
        for (float degree : degrees) {
            directionAttack.add(degree);
        }
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

        if (!drawWeapon) {
            return;
        }

        if (owner.isFlipX() != texture.isFlipY()) {
            texture.flip(false, true);
        }

        Vector2 weaponPos = owner.getAbsoluteWeaponPos();
        float degree = owner.getLastMoveDirection().angleDeg(new Vector2(1, 0));
        float dlX = weaponPos.x;
        float dlY = weaponPos.y - originY;
        if (owner.isFlipX()) {
            dlX += originX;
        } else {
            dlX -= originX;
        }

        batch.draw(texture, dlX, dlY, originX, originY, width, height, 1, 1, degree);
    }
    public int getCurrentNumWallCollide() {
        if (owner != null) {
            return numWallCollide + owner.getAbility().getWallCollideIncrease();
        }
        return numWallCollide;
    }
    public ArrayList<Bullet> getBulletArrayList() {
        return bulletArrayList;
    }
}
