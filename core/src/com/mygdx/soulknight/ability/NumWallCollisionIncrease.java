package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

public class NumWallCollisionIncrease extends Ability {
    private final static TextureRegion textureRegion = new TextureRegion(new Texture("dark_menu.png"));
    private int numIncrease = 1;
    @Override
    public void addAbility(Player user) {
        for (Weapon weapon : user.getWeapons()) {
            if (weapon instanceof Gun) {
                ((Gun) weapon).setNumWallCollide(((Gun) weapon).getNumWallCollide() + 1);
            }
        }
    }

    @Override
    public void removeAbility(Player user) {
        for (Weapon weapon : user.getWeapons()) {
            if (weapon instanceof Gun) {
                ((Gun) weapon).setNumWallCollide(((Gun) weapon).getNumWallCollide() - 1);
            }
        }
    }

    @Override
    public TextureRegion getTextureCoolDown() {
        return textureRegion;
    }

    public Weapon collectGun(Weapon weapon) {
        if (weapon instanceof Gun) {
            ((Gun) weapon).setNumWallCollide(((Gun) weapon).getNumWallCollide() + 1);
        }
        return weapon;
    }

    public Weapon dropGun(Weapon weapon) {
        if (weapon instanceof Gun) {
            ((Gun) weapon).setNumWallCollide(((Gun) weapon).getNumWallCollide() - 1);
        }
        return weapon;
    }
}
