package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class Stun extends CharacterEffect {
    private static TextureRegion TEXTURE_REGION = new TextureRegion(new Texture("effect/character_effect/Stun .png"));
    private float timeLeft = 0.5f;
    @Override
    public void update(float deltaTime, SimpleCharacter affectedCharacter) {
        textureRegion = TEXTURE_REGION;
        timeLeft -= deltaTime;
        affectedCharacter.setStunned(true);
    }

    @Override
    public boolean isFinish() {
        return timeLeft <= 0;
    }
}
