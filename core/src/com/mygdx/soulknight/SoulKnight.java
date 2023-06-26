package com.mygdx.soulknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.screen.MainGameScreen;
import com.mygdx.soulknight.screen.MenuScreen;


import java.util.ArrayList;

public class SoulKnight extends Game {

    private SpriteBatch batch;

    @Override
    public void create () {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this));
    }
    @Override
    public void render () {
        super.render();
    }
    @Override
    public void dispose () {
        batch.dispose();
    }
    public SpriteBatch getBatch() {
        return batch;
    }
    public void resetBatch() {
        batch.dispose();
        batch = new SpriteBatch();
    }
}
