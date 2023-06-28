package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class MaxHPIncrease extends Ability {
    private final static TextureRegion textureRegion = new TextureRegion(new Texture("ppp.png"));
    public MaxHPIncrease() {
        totalTimeCooldown = currentTimeCooldown = 0;
    }
    @Override
    public String toString() {
        return "MaxHPIncrease";
    }
    @Override
    public TextureRegion getTextureCoolDown() {
        return textureRegion;
    }

    @Override
    public void addAbility(SimpleCharacter user) {

    }

    @Override
    public void removeAbility(SimpleCharacter user) {

    }
}
