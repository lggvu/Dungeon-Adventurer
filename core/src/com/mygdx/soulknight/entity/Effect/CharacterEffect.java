package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.WorldMap;

import java.util.ArrayList;

public abstract class CharacterEffect extends Effect {
    public abstract void update(float deltaTime, SimpleCharacter affectedCharacter);

    public static ArrayList<CharacterEffect> loadEffect(ArrayList<String> effectsName, Vector2 pushDirection) {
        ArrayList<CharacterEffect> effects = new ArrayList<>();
        if (effectsName.contains("push")) {
            effects.add(new Push(pushDirection));
        }
        if (effectsName.contains("stun")) {
            effects.add(new Stun());
        }
        if (effectsName.contains("explosion")) {
        }
        return effects;
    }
}
