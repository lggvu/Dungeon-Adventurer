package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Effect.EffectEnum;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Effect.RegionEffect;
import com.mygdx.soulknight.entity.Map.DestroyableObject;

import java.util.ArrayList;

public class Bullet {
    private SimpleCharacter owner; //who shot ?
    private Vector2 direction;
    private float speed = 1000f;
    private int damage = 2;
    private float x, y;
    private float width = 17;
    private float height = 17;
    private TextureRegion bulletTexture;
    private int numDestroyObject = 1;
    private int numWallCollide = 1;
    private int numEnemyHit = 1;
    private float distanceLeft = 500f;
    private ArrayList<EffectEnum> effectsEnum;
    private ArrayList<SimpleCharacter> enemyHitRecently = new ArrayList<>();
    private SimpleCharacter target = null;
    private float degreeChangePerSec = 60f;
    private Animation<TextureRegion> explosionAnimation;
    private float criticalRate;
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
    public Bullet(SimpleCharacter owner, TextureRegion bulletTexture, Animation<TextureRegion> explosionAnimation, int damage, float x, float y,
                  Vector2 direction, float speed, ArrayList<EffectEnum> effectsEnum, int numDestroyObject,
                  int numEnemyHit, int numWallCollide, float degreeChangePerSec, float distanceLeft, float criticalRate) {
        this.explosionAnimation = explosionAnimation;
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.direction = direction.nor();
        this.bulletTexture = bulletTexture;
        this.speed = speed;
        this.effectsEnum = effectsEnum;
        this.owner = owner;
        this.numDestroyObject = numDestroyObject;
        this.numWallCollide = numWallCollide;
        this.numEnemyHit = numEnemyHit;
        this.degreeChangePerSec = degreeChangePerSec;
        this.distanceLeft = distanceLeft;
        this.damage = damage;
        this.criticalRate = criticalRate;
    }
    public void update(float deltaTime) {
        if (degreeChangePerSec != 0) {
            Vector2 currentPos = new Vector2(x + width / 2, y + height / 2);
            if (target == null) {
                float smallestDst = Float.MAX_VALUE;
                for (SimpleCharacter character : owner.getEnemyList()) {
                    float dst = currentPos.dst(character.getX() + character.getWidth() / 2, character.getY() + character.getHeight() / 2);
                    if (dst < smallestDst && dst < 300f) {
                        smallestDst = dst;
                        target = character;
                    }
                }
            }
            if (target != null) {
                Vector2 targetDirection = new Vector2(target.getX() + target.getWidth() / 2, target.getY() + target.getHeight() / 2).sub(currentPos);
                Vector2 Ox = new Vector2(1, 0);
                float currentDegree = direction.angleDeg(Ox);
                if (targetDirection.angleDeg(direction) <= 180) {
//                    nghĩa là góc target direction đang ở bên phải góc current direction
                    currentDegree += degreeChangePerSec * deltaTime;
                    direction = new Vector2(MathUtils.cosDeg(currentDegree), MathUtils.sinDeg(currentDegree));
                } else {
                    currentDegree -= degreeChangePerSec * deltaTime;
                    direction = new Vector2(MathUtils.cosDeg(currentDegree), MathUtils.sinDeg(currentDegree));
                }
            }
        }

        float testX = this.x, testY = this.y;
        Rectangle rectangle = null;
        boolean noCollide = false;

        for (int i = 0; i < 2; i++) {
            testX = this.x + direction.x * speed * deltaTime;
            testY = this.y + direction.y * speed * deltaTime;
            rectangle = new Rectangle(testX, testY, width, height);
            if (owner.getMap().isMapCollision(rectangle, false) || owner.getMap().isInDoor(rectangle)) {
                if (i == 0) {
                    numWallCollide -= 1;
                }
                if (numWallCollide > 0) {
                    boolean objectX = false, objectY = false;
                    // this script will implement bouncing bullet
                    Rectangle rectangle1 = new Rectangle(testX, this.y, width, height);
                    if (owner.getMap().isMapCollision(rectangle1,false) || owner.getMap().isInDoor(rectangle1)) {
//                    collide detect when update x
                        objectX = true;
                    }
                    rectangle1 = new Rectangle(this.x, testY, width, height);
                    if (owner.getMap().isMapCollision(rectangle1,false) || owner.getMap().isInDoor(rectangle1)) {
//                    collide detect when update y
                        objectY = true;
                    }

                    if (objectX == objectY) {
                        direction = new Vector2(-direction.x, -direction.y);
                    } else if (objectX) {
                        direction = new Vector2(-direction.x, direction.y);
                    } else if (objectY) {
                        direction = new Vector2(direction.x, -direction.y);
                    }
                }
            } else {
                noCollide = true;
                break;
            }
        }

        if (noCollide) {
            for (DestroyableObject object : owner.getMap().getDestroyableObjects()) {
                if (rectangle.overlaps(object.getRectangle())) {
                    owner.getMap().removeDestroyableObject(object);
                    numDestroyObject--;
                    break;
                }
            }

            ArrayList<SimpleCharacter> rmList = new ArrayList<>();
            for (SimpleCharacter character : enemyHitRecently) {
                if (!character.getRectangle().overlaps(rectangle)) {
                    rmList.add(character);
                }
            }
            enemyHitRecently.removeAll(rmList);

            for (SimpleCharacter character : owner.getEnemyList()) {
                if (character.getRectangle().overlaps(rectangle) && !enemyHitRecently.contains(rmList)) {
                    character.addEffects(CharacterEffect.loadEffect(effectsEnum, getDirection()));
                    numEnemyHit--;
                    if (Weapon.randomCrit(criticalRate)) {
                        character.getHit(damage * 2, DamageType.PHYSIC, true);
                    } else {
                        character.getHit(damage, DamageType.PHYSIC, false);
                    }
                    enemyHitRecently.add(character);
                    break;
                }
            }

            this.x = testX;
            this.y = testY;
            distanceLeft -= speed * deltaTime;
        }

        if (isStop()) {
            owner.getMap().createAnExplosion(owner, getX(), getY(), 30, this.explosionAnimation, false);
            RegionEffect.loadRegionEffect(owner, owner.getMap(), effectsEnum, getX(), getY());
        }
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
