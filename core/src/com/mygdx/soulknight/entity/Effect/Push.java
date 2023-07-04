package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class Push extends CharacterEffect {
    private Vector2 pushDirection;
    private float timeLeft = 0.5f;
    private float speedPush = 20;

    public Push(Vector2 pushDirection) {
        this.pushDirection = pushDirection;
    }
    @Override
    public void update(float deltaTime, SimpleCharacter affectedCharacter) {
        timeLeft -= deltaTime;
        affectedCharacter.move(pushDirection.x, pushDirection.y, deltaTime, speedPush, 100);
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y) {

    }

    @Override
    public boolean isFinish() {
        return timeLeft <= 0;
    }
}
