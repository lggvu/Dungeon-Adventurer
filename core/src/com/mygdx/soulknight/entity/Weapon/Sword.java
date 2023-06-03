package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

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
