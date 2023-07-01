package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;

public class Sword extends Weapon {
    private Animation<TextureRegion> swordAnimation;
    private float stateTime = 0;
    private boolean isAttacking;
    private Vector2 attackDirection;

    public Sword(String texturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
    }

    public Sword() { super(); }

    @Override
    public JsonObject load(JsonObject jsonObject) {
        jsonObject = super.load(jsonObject);
        initWeaponTexture(jsonObject.get("sword_texture").getAsString());
        JsonObject effectTexture = jsonObject.get("effect_texture").getAsJsonObject();
        Texture texture = new Texture(effectTexture.get("path").getAsString());
        TextureRegion[][] frames = SpriteLoader.splitTexture(
                texture,
                effectTexture.get("imgWidth").getAsInt(), effectTexture.get("imgHeight").getAsInt(),
                effectTexture.get("gapWidth").getAsInt(), effectTexture.get("gapHeight").getAsInt(),
                effectTexture.get("paddingWidth").getAsInt(), effectTexture.get("paddingHeight").getAsInt(),
                effectTexture.get("frameCols").getAsInt(), effectTexture.get("frameRows").getAsInt(),
                effectTexture.get("startCol").getAsInt(), effectTexture.get("startRow").getAsInt()
        );
        swordAnimation = new Animation<>(0.05f, frames[0]);
        return jsonObject;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        stateTime += deltaTime;
        if (owner != null && owner.isFlipX() != texture.isFlipY()) {
            texture.flip(false, true);
        }
        // Add any additional update logic for the sword here
    }

    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            elapsedSeconds = 0;
            subOwnerMana();
            // Initialize state time
            attackDirection = direction;
            stateTime = 0f;
            isAttacking = true;
            dealDamageMethod();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (onGround) {
            batch.draw(texture, x, y, width, height);
            return;
        }
        // Draw the sword
        if (!isAttacking && !onGround) {
            float degree = owner.getCurrentHeadDirection().angleDeg(new Vector2(1, 0));
            float dlX = 0;
            float dlY = 0;
            if (texture.isFlipY()) {
                dlX = owner.getX() + owner.getWidth() - (owner.getWeaponX() - originX);
                dlY = owner.getY() + owner.getWeaponY() - originY;
            } else {
                dlX = owner.getX() + owner.getWeaponX() - originX;
                dlY = owner.getY() + owner.getWeaponY() - originY;
            }
            batch.draw(texture, dlX, dlY, originX, originY, width, height, 1, 1, degree);
        }
        // Draw effect when attacking
        if (isAttacking) {
            float degree = attackDirection.angleDeg(new Vector2(1, 0));

            // Create the animation object and define the frame duration
            float frameDuration = 0.05f; // Adjust the duration as per your preference

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

    public void dealDamageMethod() {
        ArrayList<SimpleCharacter> listEnemy = owner.getEnemyList();
        Rectangle rectangle = new Rectangle(owner.getX() + owner.getWidth() / 2 - rangeWeapon / 2, owner.getY() + owner.getHeight() / 2 - rangeWeapon / 2, rangeWeapon, rangeWeapon);
        for (SimpleCharacter character : listEnemy) {
            if (rectangle.overlaps(character.getRectangle())) {
                character.getHit(getCurrentDamage(), DamageType.PHYSIC);
                Vector2 ownerPos = new Vector2(owner.getX() + owner.getWidth() / 2, owner.getY() + owner.getHeight() / 2);
                Vector2 monsterPos = new Vector2(character.getX() + character.getWidth() / 2, character.getY() + character.getHeight() / 2);
                character.addEffects(CharacterEffect.loadEffect(effectsEnum, monsterPos.sub(ownerPos)));
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
