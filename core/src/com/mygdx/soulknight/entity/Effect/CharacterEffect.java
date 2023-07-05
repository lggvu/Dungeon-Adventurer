package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import java.util.ArrayList;

public abstract class CharacterEffect extends Effect {
    protected TextureRegion textureRegion = null;
    public abstract void update(float deltaTime, SimpleCharacter affectedCharacter);

    public static ArrayList<CharacterEffect> loadEffect(ArrayList<EffectEnum> effectsEnum, Vector2 pushDirection) {
        ArrayList<CharacterEffect> effects = new ArrayList<>();
        for (EffectEnum effectEnum : effectsEnum) {
            CharacterEffect effect = loadEffect(effectEnum, pushDirection);
            if (effect != null) {
                effects.add(effect);
            }
        }
        return effects;
    }

    public static CharacterEffect loadEffect(EffectEnum effectEnum, Vector2 pushDirection){
        switch (effectEnum) {
            case PUSH:
                return new Push(pushDirection);
            case STUN:
                return new Stun();
            case POISON:
                return new Poison();
            case FIRE:
                return new Fire();
            default:
                return null;
        }
    }

    public boolean isDrawEffect() {
        return textureRegion != null;
    }
    public void draw(SpriteBatch batch, float x, float y) {
        if (textureRegion != null) {
            float radius = 5;
            batch.draw(textureRegion, x-radius, y-radius,radius*2,radius*2);
        }
    }
}
