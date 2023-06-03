package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Sword extends Weapon {

    public Sword(SimpleCharacter owner, String texturePath) {
        super(owner, texturePath);
    }

    public Sword(SimpleCharacter owner, String texturePath, float intervalSeconds) {
        super(owner, texturePath, intervalSeconds);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void attack(Vector2 direction) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }
}
