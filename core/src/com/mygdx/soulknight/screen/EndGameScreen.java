package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.util.TextItem;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Reader;
import java.util.*;

public class EndGameScreen extends ScreenAdapter {
    public final static String LEADERBOARD_SAVE_PATH = "assets/info/leader_board.json";
    private final SoulKnight game;
    private SpriteBatch batch;
    private Texture background;
    private TextItem confirmText;
    private ArrayList<Float> timeCompletes = new ArrayList<>();
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));

    public EndGameScreen(SoulKnight game, Level level, float currentTime) {
        System.out.println("Time finish: " + currentTime);
        this.game = game;
        batch = new SpriteBatch();
        loadLeaderBoard(level, currentTime);
        background = new Texture("black_back.png");
        BitmapFont textFont = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));
        BitmapFont hoverFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        confirmText = new TextItem("CONFIRM", new Vector2(Gdx.graphics.getWidth() / 1.2f, Gdx.graphics.getHeight() / 9.1f), textFont, hoverFont);
    }

    public void loadLeaderBoard(Level level, float currentTime) {
        try {
            Reader reader = Gdx.files.internal(LEADERBOARD_SAVE_PATH).reader();
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            reader.close();
            JsonArray jsonArray = json.get(level.name()).getAsJsonArray();
            jsonArray.add(currentTime);
            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                timeCompletes.add(iterator.next().getAsFloat());
            }
            Collections.sort(timeCompletes);
            Gson gson = new Gson();
            String jsonString = gson.toJson(json);
            Gdx.files.local(LEADERBOARD_SAVE_PATH).writeString(jsonString, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        for (int i = 1; i <= 5; i++) {
            if (i > timeCompletes.size()) {break;}
            table.add(new Label("" + i, skin)).width(50).left().padRight(10);
            table.add(new Label("" + timeCompletes.get(i - 1), skin)).left().padRight(10);
            table.row();
        }
        stage.addActor(table);
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
//        this.hoverFont.draw(batch, content, Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        confirmText.draw(batch);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        confirmText.dispose();
    }
}