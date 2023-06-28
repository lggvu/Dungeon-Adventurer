package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class FireImmunity extends Ability {
    private static final TextureRegion textureRegion = new TextureRegion(new Texture("dark_menu.png"));
    public FireImmunity() {
        totalTimeCooldown = currentTimeCooldown = 5f;
    }
    @Override
    public String toString() {
        return "MaxHPIncrease";
    }
    @Override
    public void addAbility(SimpleCharacter user) {

    }

    @Override
    public void removeAbility(SimpleCharacter user) {

    }

    @Override
    public TextureRegion getTextureCoolDown() {
        return textureRegion;
    }
}
