package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.Player;
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
    public void addAbility(Player user) {
        user.setMaxHP(user.getMaxHP() + 1);
        user.setCurrentHP(user.getCurrentHP() + 1);
    }

    @Override
    public void removeAbility(Player user) {
        user.setMaxHP(user.getMaxHP() - 1);
        if (user.getCurrentHP() > user.getMaxHP()) {
            user.setCurrentHP(user.getMaxHP());
        }
    }
}
