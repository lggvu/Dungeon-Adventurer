package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.DestroyableObject;

import java.util.ArrayList;

public class Sword extends Weapon {
    private Animation<TextureRegion> swordAnimation;
    private float stateTime;
    private TextureRegion[][] frames;
    private boolean isAttacking;
    private Vector2 attackDirection;

    public Sword(String texturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
    }

    public void setEffectFrames(TextureRegion[][] frames) {
        this.frames = frames;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (owner != null && owner.isFlipX() != texture.isFlipY()) {
            texture.flip(false, true);
            for (int i = 0; i < frames.length; i++) {
                for (int j = 0; j < frames[i].length; j++) {
                    frames[i][j].flip(false, true);
                }
            }
        }
        // Add any additional update logic for the sword here
    }

    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            subOwnerMana();
            // Initialize state time
            attackDirection = direction;
            stateTime = 0f;
            isAttacking = true;
            dealDamageMethod();
        }
    }

//    @Override
//    public void flip(boolean x, boolean y) {
//        texture.flip(x, y);
//        for (int i = 0; i < frames.length; i++) {
//            for (int j = 0; j < frames[i].length; j++) {
//                frames[i][j].flip(x, y);
//            }
//        }
//    }

    @Override
    public Texture getAnimation() {
        return null;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (onGround) {
            batch.draw(texture, x, y, width, height);
            return;
        }
        // Draw the sword
        if (!isAttacking) {
            float degree = owner.getCurrentHeadDirection().angleDeg(new Vector2(1, 0));
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
        // Draw effect when attacking
        if (isAttacking) {
            float degree = attackDirection.angleDeg(new Vector2(1, 0));

            // Create the animation object and define the frame duration
            float frameDuration = 0.05f; // Adjust the duration as per your preference
            swordAnimation = new Animation<>(frameDuration, frames[0]);

            stateTime += Gdx.graphics.getDeltaTime();

            // Get the current frame from the animation
            TextureRegion currentFrame = swordAnimation.getKeyFrame(stateTime, true);
//            if (degree > 90 && degree < 270) {
//                currentFrame = new TextureRegion(currentFrame);
//                currentFrame.flip(false, true);
//            }
            // Calculate the position for drawing the animation
            float offsetX = owner.getX() + owner.getWidth() / 2;
            float offsetY = owner.getY() + owner.getHeight() / 2 - 24;
            batch.draw(currentFrame, offsetX, offsetY, 0, 24, 48, 48, 1, 1, degree);
//            batch.draw(currentFrame, degree);

            // Check if the animation has reached the last frame
            if (swordAnimation.isAnimationFinished(stateTime)) {
                isAttacking = false; // Stop the animation
            }
        }
    }

    @Override
    public void dealDamageMethod() {
        ArrayList<SimpleCharacter> listEnemy = owner.getEnemyList();
        Rectangle rectangle = new Rectangle(owner.getX() + owner.getWidth() / 2 - rangeWeapon / 2, owner.getY() + owner.getHeight() / 2 - rangeWeapon / 2, rangeWeapon, rangeWeapon);
        for (SimpleCharacter character : listEnemy) {
            if (rectangle.overlaps(character.getRectangle())) {
                character.getHit(damage);
            }
        }

        ArrayList<DestroyableObject> arr = new ArrayList<>();
        for (DestroyableObject object : owner.getMap().getDestroyableObjects()) {
            if (rectangle.overlaps(object.getRectangle())) {
                arr.add(object);
            }
        }
        owner.getMap().removeDestroyableObject(arr);
    }
}
