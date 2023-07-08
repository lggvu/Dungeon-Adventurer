package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.gson.*;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.entity.Character.Player.Player;
import com.mygdx.soulknight.entity.Map.Minimap;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MainGameScreen extends ScreenAdapter {
    SoulKnight game;
    private SpriteBatch batch, batchHealth;
    private final static Animation<TextureRegion> WIN_GAME = new Animation<>(0.05f, SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName("end-game/win_game_1_24.png")));
    private final static Animation<TextureRegion> LOSE_GAME = new Animation<>(0.05f, SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName("end-game/lose_game_1_24.png")));
    private WorldMap map;
    private Level level;
    private Player player;
    private Stage stage1, stage2, stage3, stage4;
    private Music backgroundMusic;
    private float musicPosition = 0.0f;
    private TextButton pauseButton;
    private CooldownButton specialSkillCooldownButton, dodgeCooldownBtn;
    private CoolDownBar coolDownBar;
    private float stateTime = 0, currentTimeCount;
    private Skin skin = new Skin();
    private boolean clickEndBtn = false;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Minimap minimap;
    ArrayList<String> mapLeft = new ArrayList<>();

    public MainGameScreen(SoulKnight game, Player player, Level level) {
        this.game = game;
        this.player = player;
        this.level = level;
        mapLeft = new ArrayList<>(Arrays.asList(
                "assets/map/map/map2.1.tmx",
                "assets/map/map/map4.1.tmx",
                "assets/map/map/map3.1.tmx",
                "assets/map/map/map5.1.tmx",
                "assets/map/map/map1.1.tmx"
        ));
        loadNextMap();
        initScreenElement();
    }

    public void loadNextMap() {
        boolean save = false;
        if (map != null) {
            save = true;
            map.dispose();
        }
        map = new WorldMap(mapLeft.get(0), player, level);
        minimap = new Minimap(map.getTiledMap(), player);
        mapLeft.remove(0);
        if (save) {saveStateDict();}
        player.setMap(map);
    }

    public MainGameScreen(SoulKnight game) {
        this.game = game;
        try {
            Reader reader = Gdx.files.internal(Settings.STATE_DICT_PATH).reader();
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            reader.close();
            level = Level.valueOf(json.get("level").getAsString());
            currentTimeCount = json.get("time_count").getAsFloat();
            Class<?> clazz = Class.forName(json.get("player").getAsJsonObject().get("class_name").getAsString());
            // Get the constructor that takes no arguments
            Constructor<?> constructor = clazz.getConstructor();
            // Create an instance of the class using the constructor
            player = (Player) constructor.newInstance();
            map = new WorldMap(json.get("current_map").getAsJsonObject().get("map_path").getAsString(), player, level);
            player.setMap(map);
            player.loadStateDict(json.get("player").getAsJsonObject());
            map.loadStateDict(json.get("current_map").getAsJsonObject());
            Iterator<JsonElement> iterator = json.get("map_left").getAsJsonArray().iterator();
            while (iterator.hasNext()) {
                mapLeft.add(iterator.next().getAsString());
            }
            initScreenElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initScreenElement() {
        skin.add("heart_icon", new Texture("hud_bar/hp_icon.png"));
        skin.add("mana_icon", new Texture("hud_bar/mana_icon.png"));
        skin.add("stamina_icon", new Texture("hud_bar/stamina_icon.png"));
        skin.add("left_right_border", new Texture("hud_bar/left_right_border.png"));
        skin.add("up_down_border", new Texture("hud_bar/up_down_border.png"));
        skin.add("armor_icon", new Texture("hud_bar/armor_icon.png"));
        skin.add("heart_icon_color", Color.RED);
        skin.add("mana_icon_color", Color.BLUE);
        skin.add("armor_icon_color", Color.GRAY);
        skin.add("stamina_icon_color", Color.GRAY);

        batch = new SpriteBatch();
        batchHealth = new SpriteBatch();
        minimap = new Minimap(map.getTiledMap(), player);
        backgroundMusic = Settings.music;
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        pauseButton = this.pauseButton(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);
        specialSkillCooldownButton = new CooldownButton(player.getSpecialSkill(),Gdx.graphics.getWidth() - 160,160,50f,4f, Settings.GameButton.SPECIAL_SKILL);
        dodgeCooldownBtn = new CooldownButton(player.getDodgeSkill(),Gdx.graphics.getWidth() - 80,250,20f,0, Settings.GameButton.DODGE);
        coolDownBar = new CoolDownBar(player);
    }

    @Override
    public void show() {
        stage1 = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage2 = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage3 = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage4 = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage1);
        inputMultiplexer.addProcessor(stage2);
        inputMultiplexer.addProcessor(stage3);
        inputMultiplexer.addProcessor(stage4);

        Gdx.input.setInputProcessor(inputMultiplexer);

        // Add the button to the stage
        stage1.addActor(pauseButton);
        stage2.addActor(specialSkillCooldownButton);
        stage2.setKeyboardFocus(specialSkillCooldownButton);
        stage3.addActor(dodgeCooldownBtn);
        stage3.setKeyboardFocus(dodgeCooldownBtn);

        Skin skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));
        TextButton btn = new TextButton("Continue", skin);
        btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderBoardScreen(game, level, currentTimeCount));
                clickEndBtn = true;
                }
            }
        );

        float width = Gdx.graphics.getWidth() / 6f, height = Gdx.graphics.getHeight() / 15f;
        float x = (Gdx.graphics.getWidth() - width) / 2, y = Gdx.graphics.getHeight() / 4f;
        btn.setPosition(x, y);
        btn.setSize(width, height);
        stage4.addActor(btn);
    }

    public JsonObject getStateDict() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("level", new JsonPrimitive(this.level.name()));
        jsonObject.add("player", player.getStateDict());
        jsonObject.add("current_map", map.getStateDict());
        jsonObject.addProperty("time_count", currentTimeCount);
        JsonArray jsonArray = new JsonArray();
        for (String name : mapLeft) {
            jsonArray.add(name);
        }
        jsonObject.add("map_left", jsonArray);
        return jsonObject;
    }

    public void saveStateDict() {
        Gson gson = new Gson();
        System.out.println("SAVE STATE DICT");
        String json = gson.toJson(getStateDict());
        Gdx.files.local(Settings.STATE_DICT_PATH).writeString(json, false);
    }
    @Override
    public void render(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
//            player.setCurrentHP(0);
            map.setOver(true);
        }
        if (player.isJustStopFighting() && player.isAlive()) {
            saveStateDict();
        }

        Gdx.gl.glClearColor(28/255f,17/255f,23/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!map.isOver()) {
            currentTimeCount += deltaTime;
            map.update(deltaTime);
        }

        map.draw(batch);
        minimap.render();
        drawHealthBar();
        stage3.act(deltaTime);
        stage3.draw();
        stage2.act(deltaTime);
        stage2.draw();
        stage1.act(deltaTime);
        stage1.draw();
        coolDownBar.draw();

        if (map.isOver() && mapLeft.size() > 0 && player.isAlive()) {
            game.setScreen(new SelectAbilityScreen(game, this));
        } else if (map.isOver() && (mapLeft.size() == 0 || !player.isAlive())) {
//            Settings.deleteStateDict();
            if (stateTime == 0) {Settings.deleteStateDict();}
            if (player.getMovingSound().isPlaying()) {
                player.getMovingSound().stop();
            }
            stateTime += deltaTime;
            TextureRegion textureRegion;
            Animation<TextureRegion> temp;
            if (player.isAlive()) {
                temp = WIN_GAME;
            } else {
                temp = LOSE_GAME;
            }
            textureRegion = temp.getKeyFrame(stateTime, false);
            float width = Gdx.graphics.getWidth() / 2, height = width;
            float x = (Gdx.graphics.getWidth() - width) / 2, y = (Gdx.graphics.getHeight() - height) / 2;
            batchHealth.begin();
            batchHealth.draw(textureRegion, x, y, width, height);
            batchHealth.end();
            if (temp.isAnimationFinished(stateTime)) {
                stage4.act(deltaTime);
                stage4.draw();
            }
        }
        if (clickEndBtn) {
            dispose();
        }
    }

    @Override
    public void dispose() {
        map.dispose();
        batch.dispose();
        stage1.dispose();
        stage2.dispose();
        stage3.dispose();
        stage4.dispose();
        stage4.dispose();
        backgroundMusic.dispose();
        specialSkillCooldownButton.disposeShapeRenderer();
        dodgeCooldownBtn.disposeShapeRenderer();
    }

    private float topDown(float y) {
        return Gdx.graphics.getHeight() - y;
    }

    public Player getPlayer() {
        return player;
    }

    private void drawOneBar(String iconName, float offsetX, float offsetY, float barX, float barWidth, float scale, float ratio) {
        Texture icon = skin.get(iconName, Texture.class);
        Texture leftRight = skin.get("left_right_border", Texture.class);
        Texture upDown = skin.get("up_down_border", Texture.class);

        float cX = offsetX, barHeight = leftRight.getHeight() * scale - 2 * upDown.getHeight() * scale;
        batchHealth.begin();
        batchHealth.draw(icon, cX, offsetY + scale, icon.getWidth() * scale, icon.getHeight() * scale);
        cX = barX;
        batchHealth.draw(leftRight, cX, offsetY, leftRight.getWidth() * scale, leftRight.getHeight() * scale);
        cX += leftRight.getWidth() * scale;
        batchHealth.draw(upDown, cX, offsetY, barWidth * scale, upDown.getHeight() * scale);
        batchHealth.draw(upDown, cX, offsetY + barHeight + upDown.getHeight() * scale, barWidth * scale, upDown.getHeight() * scale);
        cX += barWidth * scale;
        batchHealth.draw(leftRight, cX, offsetY, leftRight.getWidth() * scale, leftRight.getHeight() * scale);
        batchHealth.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX + leftRight.getWidth() * scale, offsetY + upDown.getHeight() * scale, barWidth * scale, barHeight);
        shapeRenderer.setColor(skin.get(iconName + "_color", Color.class));
        shapeRenderer.rect(barX + leftRight.getWidth() * scale, offsetY + upDown.getHeight() * scale, barWidth * ratio * scale, barHeight);
        shapeRenderer.end();
    }
    private void drawHealthBar() {
        float maxWidthIcon = skin.get("heart_icon", Texture.class).getWidth();
        float barHeight = skin.get("left_right_border", Texture.class).getHeight();
        float scale = 1.5f;
        float barWidth = 150;

//        do not scale
        float gapWidth = 10;
        float gapHeight = 10;
        float paddingX = 10;
        float paddingY = 10;

        float offsetY = Gdx.graphics.getHeight() - paddingY - barHeight * scale;
        float barX = paddingX + maxWidthIcon * scale + gapWidth;
        drawOneBar("heart_icon", paddingX, offsetY, barX, barWidth, scale,(float) player.getCurrentHP() / player.getCurrentMaxHP());
        offsetY -= (barHeight * scale + gapHeight);
        drawOneBar("mana_icon", paddingX, offsetY, barX, barWidth, scale,(float) player.getCurrentMana() / player.getCurrentMaxMana());
        offsetY -= (barHeight * scale + gapHeight);
        drawOneBar("armor_icon", paddingX, offsetY, barX, barWidth, scale,(float) player.getCurrentArmor() / player.getCurrentMaxArmor());
    }

    public void resumeGame() {
        backgroundMusic.play();
        backgroundMusic.setPosition(musicPosition);
    }

    public void resize(int width, int height) {
        stage1.getViewport().update(width, height, true);
        stage2.getViewport().update(width, height, true);
        stage3.getViewport().update(width, height, true);
        // Update the position of the pause button when the screen is resized
        pauseButton.setPosition(stage1.getWidth() - pauseButton.getWidth() - 10, stage1.getHeight() - pauseButton.getHeight() - 10);
    }

    public TextButton pauseButton(float x, float y) {
        Skin skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));

        // Create the pause button
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(x,y);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new PauseGameScreen(game, MainGameScreen.this));
            if (backgroundMusic.isPlaying()) {
                musicPosition = backgroundMusic.getPosition();
                backgroundMusic.stop();
            }
                            }
        });
        return pauseButton;
    }

}
