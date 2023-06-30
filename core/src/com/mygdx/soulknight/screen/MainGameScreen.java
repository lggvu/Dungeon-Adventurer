package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Map.Minimap;
import com.mygdx.soulknight.entity.Map.WorldMap;

import java.io.FileWriter;
import java.nio.file.Paths;

public class MainGameScreen extends ScreenAdapter {
    SoulKnight game;
    private WorldMap map;
    private Player player;
    private Stage stage1, stage2, stage3;
    private Music backgroundMusic;
    private float musicPosition = 0.0f;
    private TextButton pauseButton;
    private CooldownButton specialSkillCooldownButton, dodgeCooldownBtn;
    private CoolDownBar coolDownBar;
    private float countTime = 0;

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Minimap minimap;


    public MainGameScreen(SoulKnight game, Player player) {
        this.game = game;
        this.player = player;
        map = new WorldMap("split_map/tmx/map_2.tmx", player);
        minimap = new Minimap(map.getTiledMap(), player);
        player.setMap(map);
        backgroundMusic = Settings.music;
        backgroundMusic.setVolume(0.0f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        pauseButton = this.pauseButton(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);
        specialSkillCooldownButton = new CooldownButton(player.getSpecialSkill(),Gdx.graphics.getWidth() / 1.2f,Gdx.graphics.getHeight() / 5f,50f,4f, Input.Keys.P);
        dodgeCooldownBtn = new CooldownButton(player.getDodgeSkill(), Gdx.graphics.getWidth() / 1.5f,Gdx.graphics.getHeight() / 5f,50f,4f, Input.Keys.H);
        coolDownBar = new CoolDownBar(player);
//        System.out.println(Level.EASY.name());
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
            game.setScreen(new MenuScreen(game));
            game.resetBatch();
            this.dispose();
            return;
        }

        map.draw(game.getBatch());
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
        stage1.dispose();
        stage2.dispose();
        stage3.dispose();
        backgroundMusic.dispose();
        specialSkillCooldownButton.disposeShapeRenderer();

    }

    
    private void drawHealthBar() {

        float barWidth = 200;
        float barHeight = 20;
        float boardWidth = 250;
        float boardHeight = 80;

        float barX = 25;

        float barHPY = Gdx.graphics.getHeight() - 25;
        float barManaY = Gdx.graphics.getHeight() - 50;
        float barArmorY = Gdx.graphics.getHeight() - 75;
        float boardY = Gdx.graphics.getHeight() - boardHeight;

        // Calculate the width of current health
        float healthBarWidth = barWidth * ((float) player.getCurrentHP() / player.getCurrentMaxHP());
        float armorBarWidth = barWidth * ((float) player.getCurrentArmor() / player.getCurrentMaxArmor());
        float manaBarWidth = barWidth * ((float) player.getCurrentMana() / player.getCurrentMaxMana());

        // Start the shape rendering
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Set the color for the filled portion of the health bar
        shapeRenderer.setColor(new Color(234/255f,182/255f,118/255f,1));
        shapeRenderer.rect(0, boardY, boardWidth, boardHeight);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX, barHPY, barWidth, barHeight);
        shapeRenderer.rect(barX, barArmorY, barWidth, barHeight);
        shapeRenderer.rect(barX, barManaY, barWidth, barHeight);

        // Set the color for the outline of the health bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barHPY, healthBarWidth, barHeight);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(barX, barManaY, manaBarWidth, barHeight);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(barX, barArmorY, armorBarWidth, barHeight);

        shapeRenderer.end();

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
