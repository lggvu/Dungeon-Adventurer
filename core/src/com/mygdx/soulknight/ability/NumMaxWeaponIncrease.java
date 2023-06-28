package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.Player;

public class NumMaxWeaponIncrease extends Ability {
    private final static TextureRegion textureRegion = new TextureRegion(new Texture("start_game.png"));
    @Override
    public void addAbility(Player user) {
        user.setMaxWeaponNumber(user.getMaxWeaponNumber() + 1);
    }

    @Override
    public void removeAbility(Player user) {
        user.setMaxWeaponNumber(user.getMaxWeaponNumber() - 1);
    }

    @Override
    public TextureRegion getTextureCoolDown() {
        return textureRegion;
    }
}
