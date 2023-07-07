package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.util.TextItem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

public class EndGameScreen extends ScreenAdapter {
    private final SoulKnight game;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont textFont;
    private BitmapFont hoverFont;
    private TextItem confirmText;
    private String content;

    public EndGameScreen(SoulKnight game, Boolean status) {
        this.game = game;
        batch = new SpriteBatch();
        loadRandomBackground();
        textFont = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));
        hoverFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        confirmText = new TextItem("CONFIRM", new Vector2(Gdx.graphics.getWidth() / 1.2f, Gdx.graphics.getHeight() / 9.1f), textFont, hoverFont);
        if (status == true){
            content = "Congratulations, you have finished mode X in Y seconds";
        }
        else {
            content = "You have failed! Try again when you are better";
        }
        Gdx.files.local(Settings.STATE_DICT_PATH).delete();
    }

    private void loadRandomBackground() {
        File backgroundDirectory = new File("background/endgame");
        File[] backgroundFiles = backgroundDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        });

        for (File file : backgroundFiles) {
            System.out.println(file.getPath());
        }
        if (backgroundFiles != null && backgroundFiles.length > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(backgroundFiles.length);
            String randomBackgroundPath = backgroundFiles[randomIndex].getPath();
            background = new Texture(randomBackgroundPath);
        } else {
            // Fallback to a default background if no suitable backgrounds are found
            background = new Texture("backgrounds/endgame/dungeon background 1.png");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (Gdx.input.getX() >= confirmText.getPosition().x && Gdx.input.getX() <= confirmText.getPosition().x + confirmText.getLayout().width &&
                Gdx.input.getY() - 30 >= Gdx.graphics.getHeight() - confirmText.getPosition().y - confirmText.getLayout().height &&
                Gdx.input.getY() - 30 <= Gdx.graphics.getHeight() - confirmText.getPosition().y) {
            confirmText.setHovered(true);
            if (Gdx.input.isTouched() ) {
                game.setScreen(new MenuScreen(game));
                this.dispose();
                return;
            }
        } else {
            confirmText.setHovered(false);
        }
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hoverFont.draw(batch, content, Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        confirmText.getFont().draw(batch, confirmText.getLayout(), confirmText.getPosition().x, confirmText.getPosition().y);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        textFont.dispose();
        hoverFont.dispose();
    }
}