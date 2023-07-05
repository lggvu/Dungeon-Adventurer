package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Effect.EffectEnum;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.util.Collision;
import com.mygdx.soulknight.util.TextureInfo;

import java.util.ArrayList;

public class Slice {
    private final static float DST_UNIT = 50;
    private float stateTime = 0;
    private SimpleCharacter owner;
    private Animation<TextureInfo> animation;
    private float directionDegree, rangeWeapon;
    private int damage;
    private ArrayList<Integer> characterAttackedId = new ArrayList<>();
    private ArrayList<EffectEnum> effectsEnum;
    private float criticalRate;
    public Slice(Animation<TextureInfo> animation, Vector2 direction, SimpleCharacter owner, float rangeWeapon, int damage, ArrayList<EffectEnum> effectsEnum, float criticalRate) {
        this.owner = owner;
        this.directionDegree = direction.angleDeg(new Vector2(1, 0));
        this.animation = animation;
        this.rangeWeapon = rangeWeapon;
        this.damage = damage;
        this.effectsEnum = effectsEnum;
        this.criticalRate = criticalRate;
    }

    public boolean isStop() {
        return animation.isAnimationFinished(stateTime);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (!isStop()) {
            TextureInfo temp = animation.getKeyFrame(stateTime, true);
            float degree = MathUtils.atan(temp.getHeight() / 2 / DST_UNIT) * MathUtils.radiansToDegrees;
            float startDegree = directionDegree - degree, endDegree = directionDegree + degree;
            Vector2 weaponPos = owner.getAbsoluteWeaponPos();
            boolean isCollide;

            for (SimpleCharacter enemy : owner.getEnemyList()) {
                isCollide = Collision.isArcCollideRect(weaponPos, rangeWeapon, startDegree, endDegree, enemy.getRectangle());
                if (!characterAttackedId.contains(enemy.getId()) && isCollide) {
                    characterAttackedId.add(enemy.getId());
                    if (Weapon.randomCrit(criticalRate)) {
                        enemy.getHit(damage * 2, DamageType.PHYSIC, true);
                    } else {
                        enemy.getHit(damage, DamageType.PHYSIC, false);
                    }
                    Vector2 enemyPos = new Vector2(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
                    enemy.addEffects(CharacterEffect.loadEffect(effectsEnum, enemyPos.sub(weaponPos)));
                }
            }

            ArrayList<DestroyableObject> rmObject = new ArrayList<>();
            for (DestroyableObject destroyableObject : owner.getMap().getDestroyableObjects()) {
                isCollide = Collision.isArcCollideRect(weaponPos, rangeWeapon, startDegree, endDegree, destroyableObject.getRectangle());
                if (isCollide) {
                    rmObject.add(destroyableObject);
                }
            }
            owner.getMap().removeDestroyableObject(rmObject);
        }
    }

    public void draw(SpriteBatch batch) {
        TextureInfo textureInfo = animation.getKeyFrame(stateTime, true);
        float alpha = textureInfo.getHeight() / 2 / DST_UNIT;
        float beta = textureInfo.getWidth() / textureInfo.getHeight();
        float actualHeight = rangeWeapon / (0.5f + alpha * beta);
        float actualWidth = beta * actualHeight;
        Vector2 absPos = owner.getAbsoluteWeaponPos();
        float offsetX = absPos.x + rangeWeapon - actualWidth;
        float offsetY = absPos.y - actualHeight / 2;
        float originX = -(rangeWeapon - actualWidth);
        float originY = actualHeight / 2;
        TextureRegion textureRegion = textureInfo.getTextureRegion();
        if (owner.isFlipX() != textureRegion.isFlipY()) {
            textureRegion.flip(false, true);
        }
        batch.draw(textureRegion, offsetX, offsetY, originX, originY, actualWidth, actualHeight, 1, 1, directionDegree);
    }
}
