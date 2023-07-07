package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.entity.Character.Player.Player;
import com.mygdx.soulknight.entity.Map.Minimap;
import com.mygdx.soulknight.entity.Map.WorldMap;

import java.lang.reflect.Constructor;
import java.nio.file.Paths;

public class MainGameScreen extends ScreenAdapter {
    SoulKnight game;
    private SpriteBatch batch, batchHealth;
    private WorldMap map;
    private Player player;
    private Stage stage1, stage2, stage3;
    private Music backgroundMusic;
    private float musicPosition = 0.0f;
    private TextButton pauseButton;
    private CooldownButton specialSkillCooldownButton, dodgeCooldownBtn;
    private CoolDownBar coolDownBar;
    private float countTime = 0;

    private Skin skin = new Skin();
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Minimap minimap;


    public MainGameScreen(SoulKnight game, Player player, Level level) {
        this.game = game;
        this.player = player;
        map = new WorldMap("split_map/tmx/map_2.tmx", player, level);
        player.setMap(map);
        initScreenElement();
    }

    public MainGameScreen(SoulKnight game) {
        this.game = game;
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(Settings.STATE_DICT_PATH).reader(), JsonObject.class);
            Level level = Level.valueOf(json.get("level").getAsString());
            Class<?> clazz = Class.forName(json.get("player").getAsJsonObject().get("class_name").getAsString());
            // Get the constructor that takes no arguments
            Constructor<?> constructor = clazz.getConstructor();
            // Create an instance of the class using the constructor
            player = (Player) constructor.newInstance();
            map = new WorldMap(json.get("map_path").getAsString(), player, level);
            player.setMap(map);
            map.loadStateDict(json);
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
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage1);
        inputMultiplexer.addProcessor(stage2);
        inputMultiplexer.addProcessor(stage3);

        Gdx.input.setInputProcessor(inputMultiplexer);

        // Add the button to the stage
        stage1.addActor(pauseButton);
        stage2.addActor(specialSkillCooldownButton);
        stage2.setKeyboardFocus(specialSkillCooldownButton);
        stage3.addActor(dodgeCooldownBtn);
        stage3.setKeyboardFocus(dodgeCooldownBtn);
    }

    @Override
    public void render(float deltaTime) {
        countTime += deltaTime;
        if (countTime > 1) {
            countTime = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Y) && countTime >= 1) {
            countTime = 0;
            try {
                Gson gson = new Gson();
                System.out.println(Paths.get(Gdx.files.getLocalStoragePath(),"assets", "state_dict.json").toString());
//                gson.toJson(map, );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gdx.gl.glClearColor(28/255f,17/255f,23/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.update(deltaTime);
        if (map.isOver()) {
            game.setScreen(new EndGameScreen(game,player.isAlive()));
            this.dispose();
            return;
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
    }

    @Override
    public void dispose() {
        map.dispose();
        batch.dispose();
        stage1.dispose();
        stage2.dispose();
        stage3.dispose();
        backgroundMusic.dispose();
        specialSkillCooldownButton.disposeShapeRenderer();
        dodgeCooldownBtn.disposeShapeRenderer();
    }

    private float topDown(float y) {
        return Gdx.graphics.getHeight() - y;
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

//        skin.add("heart_icon", new Texture("hud_bar/hp_icon.png"));
//        skin.add("mana_icon", new Texture("hud_bar/mana_icon.png"));
//        skin.add("stamina_icon", new Texture("hud_bar/stamina_icon.png"));
        float offsetY = Gdx.graphics.getHeight() - paddingY - barHeight * scale;
        float barX = paddingX + maxWidthIcon * scale + gapWidth;
        drawOneBar("heart_icon", paddingX, offsetY, barX, barWidth, scale,(float) player.getCurrentHP() / player.getCurrentMaxHP());
        offsetY -= (barHeight * scale + gapHeight);
        drawOneBar("mana_icon", paddingX, offsetY, barX, barWidth, scale,(float) player.getCurrentMana() / player.getCurrentMaxMana());
        offsetY -= (barHeight * scale + gapHeight);
        drawOneBar("armor_icon", paddingX, offsetY, barX, barWidth, scale,(float) player.getCurrentArmor() / player.getCurrentMaxArmor());

//        batchHealth.begin();
//        float cX = paddingX;
//        Texture icon = skin.get("heart_icon", Texture.class);
//        Texture leftRight = skin.get("left_right_border", Texture.class);
//        Texture upDown = skin.get("up_down_border", Texture.class);
//        batchHealth.draw(icon, cX, topDown(paddingY + (icon.getHeight() + 1) * scale), icon.getWidth() * scale, icon.getHeight() * scale);
//        cX += icon.getWidth() * scale + gapWidth;
//        batchHealth.draw(leftRight, cX, topDown(paddingY + leftRight.getHeight() * scale), leftRight.getWidth() * scale, leftRight.getHeight() * scale);
//        cX += leftRight.getWidth() * scale;
//        batchHealth.draw(upDown, cX, topDown(paddingY + upDown.getHeight() * scale), barWidth * scale, upDown.getHeight() * scale);
//        batchHealth.draw(upDown, cX, topDown(paddingY + leftRight.getHeight() * scale), barWidth * scale, upDown.getHeight() * scale);
//        cX += barWidth * scale;
//        batchHealth.draw(leftRight, cX, topDown(paddingY + leftRight.getHeight() * scale), leftRight.getWidth() * scale, leftRight.getHeight() * scale);
//        batchHealth.end();
//        float barHeight = 20;
//        float boardWidth = 250;
//        float boardHeight = 80;
//
//        float barX = 25;
//
//        float barHPY = Gdx.graphics.getHeight() - 25;
//        float barManaY = Gdx.graphics.getHeight() - 50;
//        float barArmorY = Gdx.graphics.getHeight() - 75;
//        float boardY = Gdx.graphics.getHeight() - boardHeight;
//
//        // Calculate the width of current health
//        float healthBarWidth = barWidth * ((float) player.getCurrentHP() / player.getCurrentMaxHP());
//        float armorBarWidth = barWidth * ((float) player.getCurrentArmor() / player.getCurrentMaxArmor());
//        float manaBarWidth = barWidth * ((float) player.getCurrentMana() / player.getCurrentMaxMana());

        // Start the shape rendering
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
////         Set the color for the filled portion of the health bar
//        shapeRenderer.setColor(new Color(234/255f,182/255f,118/255f,1));
//        shapeRenderer.rect(0, boardY, boardWidth, boardHeight);
//        shapeRenderer.end();

//        float scale = 1f, hudWidth = hudBar.getWidth() * scale, hudHeight = hudBar.getHeight() * scale;

//        batchHealth.begin();
//        batchHealth.draw(hudBar, 0, Gdx.graphics.getHeight() - hudHeight, hudWidth, hudHeight);
//        batchHealth.end();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.BLACK);
//        shapeRenderer.rect(barX, barHPY, barWidth, barHeight);
//        shapeRenderer.rect(barX, barArmorY, barWidth, barHeight);
//        shapeRenderer.rect(barX, barManaY, barWidth, barHeight);
//
//        // Set the color for the outline of the health bar
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.rect(barX, barHPY, healthBarWidth, barHeight);
//
//        shapeRenderer.setColor(Color.BLUE);
//        shapeRenderer.rect(barX, barManaY, manaBarWidth, barHeight);
//
//        shapeRenderer.setColor(Color.GRAY);
//        shapeRenderer.rect(barX, barArmorY, armorBarWidth, barHeight);
//
//        shapeRenderer.end();
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
