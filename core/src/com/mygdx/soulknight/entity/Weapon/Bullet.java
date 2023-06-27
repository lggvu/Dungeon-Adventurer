package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Map.DestroyableObject;

import java.util.ArrayList;

public class Bullet {
    private SimpleCharacter owner; //who shot ?
    private Vector2 direction;
    private float speed = 1000f;
    private int dmg = 2;
    private float x, y, startX, startY;
    private float width = 17;
    private float height = 17;
    private TextureRegion bulletTexture;
    private int numDestroyObject = 1;
    private int numWallCollide = 2;
    private int numEnemyHit = 1;
    private float distanceLeft = 500f;
    private ArrayList<String> effectsName;
    private SimpleCharacter lastEnemyHit = null;
    public Vector2 getDirection() {
        return direction;
    }

    public boolean isStop() {
        if (numDestroyObject <= 0) {
            return true;
        } else if (numWallCollide <= 0) {
            return true;
        } else if (numEnemyHit <= 0) {
            return true;
        } else if (distanceLeft <= 0) {
            return true;
        }
        return false;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Bullet(SimpleCharacter owner, TextureRegion bulletTexture, float x, float y, Vector2 direction, float speed, ArrayList<String> effectsName) {
        this.x = this.startX = x - width / 2;
        this.y = this.startY = y - height / 2;
        this.direction = direction.nor();
        this.bulletTexture = bulletTexture;
        this.speed = speed;
        this.effectsName = effectsName;
        this.owner = owner;
    }

    public float getDistanceGoThrough() {
        return new Vector2(x, y).dst(startX, startY);
    }

    public void update(float deltaTime) {
        float testX = this.x + direction.x * speed * deltaTime;
        float testY = this.y + direction.y * speed * deltaTime;
        Rectangle rectangle = new Rectangle(testX, testY, width, height);
        if (owner.getMap().isMapCollision(rectangle) || owner.getMap().isInDoor(rectangle)) {
            numWallCollide -= 1;
            if (numWallCollide > 0) {
                // this script will implement bouncing bullet
                Rectangle rectangle1 = new Rectangle(testX, this.y, width, height);
                if (owner.getMap().isMapCollision(rectangle1,false) || owner.getMap().isInDoor(rectangle1)) {
//                    only update x but still collision
                    direction = new Vector2(-direction.x, direction.y);
                    update(deltaTime);
                    return;
                }
                rectangle1 = new Rectangle(this.x, testY, width, height);
                if (owner.getMap().isMapCollision(rectangle1,false) || owner.getMap().isInDoor(rectangle1)) {
//                    only update y but still collision
                    direction = new Vector2(direction.x, -direction.y);
                    update(deltaTime);
                    return;
                }
            }
        }

        for (DestroyableObject object : owner.getMap().getDestroyableObjects()) {
            if (rectangle.overlaps(object.getRectangle())) {
                owner.getMap().removeDestroyableObject(object);
                numDestroyObject--;
                break;
            }
        }

        for (SimpleCharacter character : owner.getEnemyList()) {
            if (character.getRectangle().overlaps(rectangle) && (lastEnemyHit == null || !character.equals(lastEnemyHit))) {
                character.addEffects(CharacterEffect.loadEffect(effectsName, getDirection()));
                numEnemyHit--;
                character.getHit(dmg);
                lastEnemyHit = character;
                break;
            }
        }

        this.x = testX;
        this.y = testY;
        distanceLeft -= speed * deltaTime;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void draw(SpriteBatch batch) {
        float degree = direction.angleDeg(new Vector2(1, 0));
        batch.draw(bulletTexture, this.x, this.y,width/2,height/2, width, height,1,1 , degree);
    }
}
