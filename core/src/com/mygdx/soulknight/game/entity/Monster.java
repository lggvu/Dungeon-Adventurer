package com.mygdx.soulknight.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.soulknight.game.SoulKnight;

public class Monster extends Character {

    public Monster(Texture texture, SoulKnight game) {
        super(texture, game);
        this.mana = Integer.MAX_VALUE;
        this.armor = 0;
    }

    public Monster(Texture texture, SoulKnight game, int HP, int armor, float runSpeed) {
        super(texture, game, HP, armor, runSpeed);
        this.mana = Integer.MAX_VALUE;
        this.armor = 0;
    }

    @Override
    public void attack() {

    }
}
