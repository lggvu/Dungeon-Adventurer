package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import java.io.File;

public class MenuScreen extends ScreenAdapter {
    SpriteBatch batch;
    Texture background;
    SoulKnight game;
    private Stage stage;
    private boolean isExistStateDict = false;

    public MenuScreen(SoulKnight game) {
        File file = new File(Settings.STATE_DICT_PATH);
        isExistStateDict = file.exists();
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("dark_menu.png");
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Button startGame = new TextButton("Start Game", Settings.skin);
        // Add click listeners to the buttons
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SelectModeScreen(game));
                dispose();
            }
        });

        Button continueGame = new TextButton("Continue", Settings.skin);
        if (isExistStateDict) {
            continueGame.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new MainGameScreen(game));
                    dispose();
                }
            });
        } else {
            continueGame.setDisabled(true);
        }

        Table table = new Table();
        table.setFillParent(true);

        table.add(startGame).width(200).pad(20);
        table.row();
        table.add(continueGame).width(200).pad(20);

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
