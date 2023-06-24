package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Map.DestroyableObject;

import java.util.ArrayList;

public class Gun extends Weapon {
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f/2;

    private ArrayList<Explosion> explosionArrayList = new ArrayList<>();
    private String bulletTexturePath = "bullet/bullet1.png";

    private String explosionTexturePath;

    private TextureRegion[] explosionFrames;
    private Animation<TextureRegion> explosionAnimation;

    public Gun(String texturePath, String bulletTexturePath, String explosionTexturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate, float bulletSpeed) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
        int underscoreIndex1 = explosionTexturePath.indexOf('_');
        int underscoreIndex2 = explosionTexturePath.lastIndexOf('_');

        // Extract the x and y substrings
        String xSubstring = explosionTexturePath.substring(underscoreIndex1 + 1, underscoreIndex2);
        String ySubstring = explosionTexturePath.substring(underscoreIndex2 + 1, explosionTexturePath.lastIndexOf('.'));

        // Parse the x and y values as integers
        int frameRows = Integer.parseInt(xSubstring);
        int frameCols = Integer.parseInt(ySubstring);
        this.bulletTexturePath = bulletTexturePath;
        this.bulletSpeed = bulletSpeed;

        Texture explosionSheet = new Texture(explosionTexturePath);

        int frameWidth = explosionSheet.getWidth() / frameCols;
        int frameHeight = explosionSheet.getHeight() / frameRows;
        TextureRegion[][] temp = TextureRegion.split(explosionSheet, frameWidth, frameHeight);
        explosionFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int row = 0; row < frameRows; row++) {
            for (int col = 0; col < frameCols; col++) {
                explosionFrames[index++] = temp[row][col];
            }
        }

        this.explosionAnimation = new Animation<TextureRegion>(0.05f, explosionFrames);

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
                    character.getRoom().explosionArrayList.add(explosion);

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

    public ArrayList<Explosion> getExplosionArrayList() {
        return explosionArrayList;
    }
}
