package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;

public class SelectModeScreen extends ScreenAdapter {
    SpriteBatch batch;
    Stage stage;
    SoulKnight game;
    Texture background;
    public SelectModeScreen(SoulKnight game) {
        batch = new SpriteBatch();
        this.game = game;
        background = new Texture("dark_menu.png");
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Level[] levels = Level.class.getEnumConstants();

        float width = Gdx.graphics.getWidth() * 0.25f, padding = Gdx.graphics.getHeight() * 0.025f;
        for (Level level : levels) {
            TextButton button = new TextButton(level.name(), Settings.skin);
            final Level gameLevel = level;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new SelectCharacterScreen(game, gameLevel));
                    dispose();
                }
            });
            table.add(button).width(width).pad(padding);
            table.row();
        }

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        stage.dispose();
    }
}
