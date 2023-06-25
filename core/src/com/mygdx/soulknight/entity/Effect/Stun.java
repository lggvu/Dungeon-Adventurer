package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class Stun extends Effect {
    private SimpleCharacter affectedCharacter;
    private float timeLeft = 0.5f;

    public Stun(SimpleCharacter affectedCharacter) {
        this.affectedCharacter = affectedCharacter;
    }
    @Override
    public void update(float deltaTime) {
        timeLeft -= deltaTime;
        affectedCharacter.setStunned(true);
    }

    @Override
    public boolean isFinish() {
        return timeLeft <= 0;
    }
}
