package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.WeaponLoader;

import java.util.ArrayList;

public class MainGameScreen extends ScreenAdapter {
    SoulKnight game;
    private WorldMap map;
    private Player player;
    private Stage stage;
    private Music backgroundMusic;
    private float musicPosition = 0.0f;
    private TextButton pauseButton;
    private CooldownButton cooldownButton;
    private float cooldownTime;

    ShapeRenderer shapeRenderer = new ShapeRenderer();


    public MainGameScreen(SoulKnight game) {
        this.game = game;
        player = new Player("king", null);
        map = new WorldMap("split_map/tmx/maps.tmx", player);
        player.setMap(map);
        backgroundMusic = Settings.music;
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        
        pauseButton = this.pauseButton();
        cooldownButton = new CooldownButton();
                
    }

    @Override
    public void show() {
    	stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Gdx.input.setInputProcessor(stage);
        cooldownButton.setPosition(Gdx.graphics.getWidth() / 1.2f - cooldownButton.RADIUS, Gdx.graphics.getHeight() / 5f - cooldownButton.RADIUS); // Set the button position as per your requirements

        // Add the button to the stage
        stage.addActor(cooldownButton);
        stage.setKeyboardFocus(cooldownButton);

        stage.addActor(pauseButton);
    }

    @Override
    public void render(float deltaTime) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.update(deltaTime);

        if (map.isOver()) {
            game.setScreen(new MenuScreen(game));
            game.resetBatch();
            this.dispose();
            return;
        }

        map.draw(game.getBatch());
        drawHealthBar();
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void dispose() {
        map.dispose();
        stage.dispose();
        backgroundMusic.dispose();
       

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

    @Override
    public void hide() {
        backgroundMusic.stop();
    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        // Update the position of the pause button when the screen is resized
        pauseButton.setPosition(stage.getWidth() - pauseButton.getWidth() - 10, stage.getHeight() - pauseButton.getHeight() - 10);
    }

    public TextButton pauseButton() {
        Skin skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));

        // Create the pause button
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - 10, Gdx.graphics.getHeight() - pauseButton.getHeight() - 10);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseGameScreen(game, MainGameScreen.this));
                if (backgroundMusic.isPlaying()) {
                    musicPosition = backgroundMusic.getPosition();
                    backgroundMusic.stop();
                }
                if (cooldownButton.isCoolingDown()) {
                	cooldownTime = cooldownButton.getCooldownTimer();
                }
                            }
        });
        return pauseButton;
    }

}
