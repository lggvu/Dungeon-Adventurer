package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.ability.Ability;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextItem;

import java.io.Reader;
import java.util.*;

public class LeaderBoardScreen extends ScreenAdapter {
    public final static String LEADERBOARD_SAVE_PATH = "assets/info/leader_board.json";
    private final SoulKnight game;
    private BitmapFont headingFont, normalFont;
    private SpriteBatch batch = new SpriteBatch();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Texture background;
    private TextItem confirmText;
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));
    private HashMap<Level, ArrayList<Float>> leaderBoard = new HashMap<>();
    private TextItemWithBorder[] textItemWithBorders;
    private Level selectedLevel;
    private Label.LabelStyle labelStyle;
    private Table table;

    public LeaderBoardScreen(SoulKnight game, Level level, float currentTime) {
        System.out.println("Time finish: " + currentTime);
        selectedLevel = level;
        this.game = game;
        headingFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        headingFont.getData().setScale(2);
        normalFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        loadLeaderBoard(level, currentTime);
        background = new Texture("background.png");
        BitmapFont textFont = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));
        BitmapFont hoverFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        confirmText = new TextItem("CONFIRM", new Vector2(Gdx.graphics.getWidth() / 1.2f, Gdx.graphics.getHeight() / 9.1f), textFont, hoverFont);
        Level[] enums = Level.class.getEnumConstants();
        textItemWithBorders = new TextItemWithBorder[enums.length];
        float widthRatio = 3f / 20, gapWidthRatio = 1f / 20, heightRatio = 1f / 10;
        float offsetXRatio = (1 - widthRatio * enums.length - gapWidthRatio * (enums.length - 1)) / 2;
        float offsetYRatio = 0.6f;
        for (int i = 0; i < enums.length; i++) {
            textItemWithBorders[i] = new TextItemWithBorder(enums[i].name(),
                (offsetXRatio + (widthRatio + gapWidthRatio) * i) * Gdx.graphics.getWidth(),
                offsetYRatio * Gdx.graphics.getHeight(),
                widthRatio * Gdx.graphics.getWidth(),
                heightRatio * Gdx.graphics.getHeight()
            );
        }
        labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        labelStyle.font.getData().setScale(0.7f);
    }

    public LeaderBoardScreen(SoulKnight game) {
        this(game, Level.EASY, -1);
    }

    class TextItemWithBorder {
        private TextItem textItem;
        private boolean hover = false;
        private float x, y, width, height, border = 2;
        public TextItemWithBorder(String text, float x, float y, float width, float height) {
            BitmapFont normalFont = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));
            BitmapFont hoverFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
            textItem = new TextItem(text, null, normalFont, hoverFont);;
            this.x = x; this.y = y; this.width = width; this.height = height;
            GlyphLayout layout = textItem.getLayout();
            textItem.setPosition(new Vector2(x + (width - layout.width) / 2, y + (height + layout.height) / 2));
        }
        public void setHover(boolean hover) {
            this.hover = hover;
            textItem.setHovered(hover);
        }
        public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Color color = hover ? Color.valueOf("#f3f3f3") : Color.valueOf("#666666");
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x - border, y - border, width + border * 2, height + border * 2);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.end();
            batch.begin();
            textItem.draw(batch);
            batch.end();
        }
    }

    public void loadLeaderBoard(Level level, float currentTime) {
        try {
            Reader reader = Gdx.files.internal(LEADERBOARD_SAVE_PATH).reader();
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            reader.close();

            if (currentTime >= 0) {
                JsonArray jsonArray = json.get(level.name()).getAsJsonArray();
                jsonArray.add(currentTime);
            }

            Level[] enums = Level.class.getEnumConstants();
            ArrayList<Float> timeCompletes;
            for (Level level1 : enums) {
                timeCompletes  = new ArrayList<>();
                Iterator<JsonElement> iterator = json.get(level1.name()).getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    timeCompletes.add(iterator.next().getAsFloat());
                }
                Collections.sort(timeCompletes);
                leaderBoard.put(level1, timeCompletes);
            }

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
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
    }
    @Override
    public void render(float delta) {
//      update
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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        this.hoverFont.draw(batch, content, Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        GlyphLayout headingLayout = new GlyphLayout(headingFont, "LEADERBOARD");
        headingFont.draw(batch, headingLayout, (Gdx.graphics.getWidth() - headingLayout.width) / 2, Gdx.graphics.getHeight() * 0.85f);
        confirmText.draw(batch);
        batch.end();

        float mouseX = Gdx.input.getX(), mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (TextItemWithBorder t : textItemWithBorders) {
            if (t.x <= mouseX && mouseX <= t.x + t.width && t.y <= mouseY && mouseY <= t.y + t.height) {
                t.setHover(true);
                if (Gdx.input.isTouched()) {
                    selectedLevel = Level.valueOf(t.textItem.getText());
                }
            } else {
                t.setHover(false);
            }
        }

        for (TextItemWithBorder textItemWithBorder : textItemWithBorders) {
            if (Level.valueOf(textItemWithBorder.textItem.getText().toUpperCase()).equals(selectedLevel)) {
                textItemWithBorder.setHover(true);
            }
            textItemWithBorder.draw(batch, shapeRenderer);
        }

        table.reset();
        table.setY(-Gdx.graphics.getHeight() * 0.05f);
        ArrayList<Float> timeCompletes = leaderBoard.get(selectedLevel);

        for (int i = 1; i <= 5; i++) {
            if (i > timeCompletes.size()) {break;}
            Label label = new Label("NO " + i, labelStyle);
            table.add(label);
            table.getCell(label).padRight(Gdx.graphics.getWidth() * 0.05f);
            table.getCell(label).padBottom(Gdx.graphics.getHeight() * 0.01f);

            label = new Label("" + Math.round(timeCompletes.get(i - 1) * 100) / 100f, labelStyle);
            table.add(label);
            table.getCell(label).padBottom(Gdx.graphics.getHeight() * 0.01f);
            table.row();
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        confirmText.dispose();
    }
}