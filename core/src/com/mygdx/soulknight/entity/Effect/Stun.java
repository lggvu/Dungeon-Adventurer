package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class Stun extends CharacterEffect {
    private float timeLeft = 0.5f;
    @Override
    public void update(float deltaTime, SimpleCharacter affectedCharacter) {
        timeLeft -= deltaTime;
        affectedCharacter.setStunned(true);
    }

    @Override
    public boolean isFinish() {
        return timeLeft <= 0;
    }
}
