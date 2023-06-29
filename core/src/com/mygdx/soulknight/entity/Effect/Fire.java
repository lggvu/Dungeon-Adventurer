package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;

public class Fire extends CharacterEffect {
    private final static TextureRegion TEXTURE_REGION = new TextureRegion(new Texture("effect/fire_region.png"));
    private float timeLeft = 3f;
    private float timeEffectAgain = 1f;
    private float timeCount = 0.4f;
    private int damage = 1;
    @Override
    public void update(float deltaTime, SimpleCharacter affectedCharacter) {
        textureRegion = TEXTURE_REGION;
        timeCount += deltaTime;
        timeLeft -= deltaTime;
        if (timeCount >= timeEffectAgain) {
            timeCount = 0;
            affectedCharacter.getHit(damage, DamageType.FIRE);
        }
    }

    @Override
    public boolean isFinish() {
        return timeLeft <= 0;
    }
}
