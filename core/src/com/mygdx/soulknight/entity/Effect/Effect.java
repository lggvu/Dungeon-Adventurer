package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

import java.util.ArrayList;

public abstract class Effect {
    public abstract void update(float deltaTime);
    public abstract boolean isFinish();
    public static ArrayList<Effect> loadEffect(ArrayList<String> effectsName, SimpleCharacter affectedCharacter, Vector2 pushDirection) {
        ArrayList<Effect> effects = new ArrayList<>();
        if (effectsName.contains("push")) {
            effects.add(new Push(affectedCharacter, pushDirection));
        }
        if (effectsName.contains("stun")) {
            effects.add(new Stun(affectedCharacter));
        }
        return effects;
    }
}
