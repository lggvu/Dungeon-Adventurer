package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.soulknight.SoulKnight;

public class MenuScreen extends ScreenAdapter {
    SoulKnight game;
    Texture background;
    Texture startGameTexture;
    private float startBtnWidth = 250;
    private float startBtnHeight = 250;
    private float startBtnY = 50;

    public MenuScreen(SoulKnight game) {
        this.game = game;
        background = new Texture("menu.png");
        startGameTexture = new Texture("start_game.png");
    }

    @Override
    public void render(float delta) {
//        System.out.println("" + Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float x = (Gdx.graphics.getWidth() - startBtnWidth) / 2;
        game.getBatch().begin();
        if (
            (Gdx.input.getX() > x) &&
            (Gdx.input.getX() < x + startBtnWidth) &&
            (Gdx.graphics.getHeight() - Gdx.input.getY() > startBtnY) &&
            (Gdx.graphics.getHeight() - Gdx.input.getY() < startBtnHeight + startBtnY)
        ) {
            if (Gdx.input.isTouched()) {
                game.setScreen(new MainGameScreen(game));
                this.dispose();
            }
        }
        game.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().draw(startGameTexture, x, startBtnY, startBtnWidth, startBtnHeight);
        game.getBatch().end();
    }

    @Override
    public void dispose() {
        background.dispose();
        startGameTexture.dispose();
    }
}
