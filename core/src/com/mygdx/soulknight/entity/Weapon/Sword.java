package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class Sword extends Weapon {
    private Animation<TextureRegion> swordAnimation;
    private float stateTime;
    private TextureRegion[][] frames;
    private boolean isAttacking;

    public Sword(SimpleCharacter owner, String texturePath) {
        super(owner, texturePath);
    }

    public void setEffectFrames(TextureRegion[][] frames) {
        this.frames = frames;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Add any additional update logic for the sword here
    }

    @Override
    public void attack(Vector2 direction) {
        // Initialize state time
        stateTime = 0f;
        isAttacking = true;
    }

    @Override
    public void flip(boolean x, boolean y) {
        texture.flip(x, y);
        for (int i = 0; i < frames.length; i++) {
            for (int j = 0; j < frames[i].length; j++) {
                frames[i][j].flip(x, y);
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        float degree = owner.getCurrentHeadDirection().angleDeg(new Vector2(1, 0));
        // Draw the sword
        if (!isAttacking) {
            batch.draw(texture, owner.getX() + owner.getWidth() * 0.5f, owner.getY() + owner.getHeight() * 0.25f, 0, 4, 12, 8, 1, 1, degree);
        }
        // Draw effect when attacking
        if (isAttacking) {

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
}
