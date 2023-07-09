package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.soulknight.game.Settings;
import com.mygdx.soulknight.game.DungeonAdventurer;
import java.io.File;

public class MenuScreen extends ScreenAdapter {
    SpriteBatch batch;
    Texture background;
    DungeonAdventurer game;
    private Stage stage;
    private boolean isExistStateDict = false;


    public MenuScreen(DungeonAdventurer game) {
        File file = new File(Settings.STATE_DICT_PATH);
        isExistStateDict = file.exists();
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("dark_menu.png");

        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("start_game_scale.png")));
        BitmapFont font = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = buttonDrawable;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        TextButton startGame = new TextButton("Start Game", Settings.skin);
        // Add click listeners to the buttons
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.deleteStateDict();
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

        Button leaderBoard = new TextButton("Leaderboard", Settings.skin);
        leaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderBoardScreen(game));
                dispose();
            }
        });

        Button settings = new TextButton("Settings", Settings.skin);
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });

        Button help = new TextButton("Help", Settings.skin);
        help.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HelpScreen(game));
                dispose();
            }
        });
        Button exitButton = new TextButton("Exit", Settings.skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the application
            }
        });
        Table table = new Table();
        table.setFillParent(true);

        float width = Gdx.graphics.getWidth() * 0.25f, padding = Gdx.graphics.getHeight() * 0.015f;
        table.add(startGame).width(width).pad(padding);
        table.row();
        table.add(continueGame).width(width).pad(padding);
        table.row();
        table.add(leaderBoard).width(width).pad(padding);
        table.row();
        table.add(settings).width(width).pad(padding);
        table.row();
        table.add(help).width(width).pad(padding);
        table.row();
        table.add(exitButton).width(width).pad(padding);
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
