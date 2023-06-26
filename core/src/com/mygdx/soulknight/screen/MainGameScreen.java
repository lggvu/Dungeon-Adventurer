package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Map.Minimap;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.specialskill.Barrage;
import com.mygdx.soulknight.specialskill.SpecialSkill;

public class MainGameScreen extends ScreenAdapter {
    SoulKnight game;
    private WorldMap map;
    private Player player;
    private Stage stage1;
    private Stage stage2;
    private Music backgroundMusic;
    private float musicPosition = 0.0f;
    private TextButton pauseButton;
    private CooldownButton cooldownButton;
    private float cooldownTime;
    private int difficultMode;

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Minimap minimap;


    public MainGameScreen(SoulKnight game, Player player) {
        this.game = game;
        this.player = player;
        map = new WorldMap("split_map/tmx/map_2.tmx", player);
        minimap = new Minimap(map.getTiledMap(), player);
        player.setMap(map);
        Barrage barrage = new Barrage(player, "bullet/bullet4.png", 5, 1f, 100f, 5f);
        player.setSkill((SpecialSkill)barrage);
        backgroundMusic = Settings.music;
        backgroundMusic.setVolume(0.0f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        pauseButton = this.pauseButton(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);
        cooldownButton = new CooldownButton(player);
    }

    @Override
    public void show() {
        stage1 = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage2 = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage1);
        inputMultiplexer.addProcessor(stage2);

        Gdx.input.setInputProcessor(inputMultiplexer);

        cooldownButton.setPosition(Gdx.graphics.getWidth() / 1.2f - cooldownButton.RADIUS, Gdx.graphics.getHeight() / 5f - cooldownButton.RADIUS); // Set the button position as per your requirements

        // Add the button to the stage
        stage1.addActor(pauseButton);
        stage2.addActor(cooldownButton);
        

        stage2.setKeyboardFocus(cooldownButton);
    }

    @Override
    public void render(float deltaTime) {

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
        stage2.act(deltaTime);
        stage2.draw();
        stage1.act(deltaTime);
        stage1.draw();
    }

    @Override
    public void dispose() {
        map.dispose();
        stage1.dispose();
        stage2.dispose();
        backgroundMusic.dispose();
        cooldownButton.disposeShapeRenderer();

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
        float healthBarWidth = barWidth * ((float) player.getCurrentHP() / player.getMaxHP());
        float armorBarWidth = barWidth * ((float) player.getCurrentArmor() / player.getMaxArmor());
        float manaBarWidth = barWidth * ((float) player.getCurrentMana() / player.getMaxMana());

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
        cooldownButton.setCooldownTimer(cooldownTime);
    }

    public void resize(int width, int height) {
        stage1.getViewport().update(width, height, true);
        stage2.getViewport().update(width, height, true);

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
            if (player.isCoolingDown()) {
                cooldownTime = cooldownButton.getCooldownTimer();
            }
                            }
        });
        return pauseButton;
    }

}
