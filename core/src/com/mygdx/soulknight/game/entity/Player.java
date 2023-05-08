package com.mygdx.soulknight.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.soulknight.game.SoulKnight;

public class Player extends Character {

    public Player(Texture texture, SoulKnight game) {
        super(texture, game);
    }

    public Player(Texture texture, SoulKnight game, int HP, int armor, float runSpeed) {
        super(texture, game, HP, armor, runSpeed);
    }

    @Override
    public void attack() {

    }
}
