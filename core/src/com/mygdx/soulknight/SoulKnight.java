package com.mygdx.soulknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.soulknight.screen.MenuScreen;

public class SoulKnight extends Game {
    @Override
    public void create () {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        this.setScreen(new MenuScreen(this));
    }
}
