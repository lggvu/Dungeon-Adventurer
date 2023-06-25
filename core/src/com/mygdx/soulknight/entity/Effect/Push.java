package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class Push extends Effect {
    private SimpleCharacter affectedCharacter;
    private Vector2 pushDirection;
    private float timeLeft = 0.5f;
    private float speedPush = 20;

    public Push(SimpleCharacter affectedCharacter, Vector2 pushDirection) {
        this.affectedCharacter = affectedCharacter;
        this.pushDirection = pushDirection;
    }
    @Override
    public void update(float deltaTime) {
        timeLeft -= deltaTime;
        affectedCharacter.move(pushDirection.x, pushDirection.y, deltaTime, speedPush);
    }

    @Override
    public boolean isFinish() {
        return timeLeft <= 0;
    }
}
