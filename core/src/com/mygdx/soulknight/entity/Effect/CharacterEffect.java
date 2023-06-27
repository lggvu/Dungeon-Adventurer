package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.WorldMap;

import java.util.ArrayList;

public abstract class CharacterEffect extends Effect {
    protected TextureRegion textureRegion = null;
    public abstract void update(float deltaTime, SimpleCharacter affectedCharacter);

    public static ArrayList<CharacterEffect> loadEffect(ArrayList<String> effectsName, Vector2 pushDirection) {
        ArrayList<CharacterEffect> effects = new ArrayList<>();
//        Stupid design, fix it later
        if (effectsName.contains("push")) {
            effects.add(new Push(pushDirection));
        }
        if (effectsName.contains("stun")) {
            effects.add(new Stun());
        }
        if (effectsName.contains("poison")) {
            effects.add(new Poison());
        }
        if (effectsName.contains("fire")) {
            effects.add(new Fire());
        }
        return effects;
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
