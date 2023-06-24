package com.mygdx.soulknight.specialskill;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.entity.Weapon.Bullet;

public class Barrage extends SpecialSkill {
    private ArrayList<Bullet> bulletArrayList = new ArrayList<>();
    private float bulletSpeed = 1000f/2;
    private TextureRegion bulletTextureRegion;
    private float intervalSeconds;
    private int damage;
	private float elapsedSeconds =1;
	private SimpleCharacter owner;
	private float duration;
	private float executedTime;
    public Barrage(SimpleCharacter owner,String bulletTexturePath, int damage, float intervalSeconds, float bulletSpeed, float duration) {
        this.owner = owner;
        this.bulletTextureRegion = new TextureRegion(new Texture(bulletTexturePath));
        this.bulletSpeed = bulletSpeed;
        this.damage = damage;
        this.intervalSeconds = intervalSeconds;
        this.duration = duration;
        
    }
    @Override
    public void activate() {
        if (elapsedSeconds > intervalSeconds && duration > 0) {
        	elapsedSeconds = 0;
        	executedTime += intervalSeconds;
        	float angleIncrement = (float) Math.toRadians(45);
            
            // Create an array to store the 8 vectors
            Vector2[] vectors = new Vector2[8];
            vectors[0] = new Vector2(1,0); // Set the original vector as the first vector
            
            // Calculate the remaining 7 vectors
            for (int i = 1; i < 8; i++) {
                float angle = i * angleIncrement;
                vectors[i] = new Vector2(
                        (float) Math.cos(angle),
                        (float) Math.sin(angle)
                );
            }
            for (Vector2 vector : vectors) {
            	bulletArrayList.add(new Bullet(bulletTextureRegion, owner.getX() + owner.getWidth()/3, owner.getY() + owner.getHeight()/3, vector, bulletSpeed));
            }
            
            if (duration < executedTime) {
            	this.setActivate(false);;
            	executedTime = 0;
            }
        }
    }
    @Override
    public void update(float deltaTime) {
    	this.elapsedSeconds += deltaTime;
        for (Bullet bullet : bulletArrayList) {
            bullet.update(deltaTime);
        }
        handleBulletCollision();
        dealDamageMethod();
    }
    public void handleBulletCollision() {
        if (owner == null) {
            return;
        }
        ArrayList<Bullet> removeList = new ArrayList<>();
        for (Bullet bullet : bulletArrayList) {
            if (owner.getMap().isMapCollision(bullet.getRectangle(), false)) {
                removeList.add(bullet);
            }
        }
        bulletArrayList.removeAll(removeList);

        ArrayList<DestroyableObject> objectsRemove = new ArrayList<>();
        for (DestroyableObject object : owner.getMap().getDestroyableObjects()) {
            removeList = new ArrayList<>();
            for (Bullet bullet : bulletArrayList) {
                if (bullet.getRectangle().overlaps(object.getRectangle())) {
                    objectsRemove.add(object);
                    removeList.add(bullet);
                    break;
                }
            }
            bulletArrayList.removeAll(removeList);
        }
        owner.getMap().removeDestroyableObject(objectsRemove);
    }
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
    @Override
    public void draw(SpriteBatch batch) {
        for (Bullet bullet : bulletArrayList) {
            bullet.draw(batch);
        }
    }



    

}
