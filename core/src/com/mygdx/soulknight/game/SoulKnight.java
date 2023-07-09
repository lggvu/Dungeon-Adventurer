package com.mygdx.soulknight.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.soulknight.screen.MenuScreen;

public class SoulKnight extends Game {
    @Override
    public void create () {
        Settings.loadSetting();
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        this.setScreen(new MenuScreen(this));
    }
}
