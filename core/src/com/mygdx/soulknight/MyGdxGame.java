package com.mygdx.soulknight;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyGdxGame extends ApplicationAdapter {
    private static final float CHARACTER_SPEED = 180f;
    private final float CHARACTER_SCALE_FACTOR = 0.75f;

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private Character character;
    private SpriteBatch spriteBatch;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private Vector2 direction = new Vector2(1,0);
    private ArrayList<Bullet> monsterbullets = new ArrayList<Bullet>();
//    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private float elapsedSeconds =0;
    private float intervalSeconds = 2; //Monster will shoot each 3 seconds
    private ArrayList<Gun> guns = new ArrayList<Gun>();
    private Gun considerGun;
    
    @Override
    public void create () {
        // Set up the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Load the map
        tiledMap = new TmxMapLoader().load("second_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collisions");

        this.guns.add(new Gun("8", 70, 100));

        // Load the character texture and position
        character = new Character(new Texture("Knight.png"), this);
        character.setSize((int) (character.getWidth()*0.6), (int) (character.getHeight()*0.6));
        // Set up the sprite batch
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render () {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);        

        // Update the camera position based on the character's position
        camera.position.x = character.getX() + character.getTexture().getWidth() /2;
        camera.position.y = character.getY() + character.getTexture().getHeight() / 2;
        camera.update();

        // Render the map and the character
        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Gun g: this.guns) {
        	if (g.equals(this.considerGun)) {
        		Sprite consider = new Sprite(new Texture("ppp.png"));
        		consider.setPosition(g.getX() + g.getWidth()/2 - consider.getWidth()/2, g.getY() + g.getHeight() + 5);
        		consider.draw(spriteBatch);
        	}
        	g.draw(spriteBatch);
        }
        character.Draw(spriteBatch);                
        spriteBatch.end();
    }



    @Override
    public void dispose () {
        // Dispose of resources
        tiledMap.dispose();
        mapRenderer.dispose();
        character.getTexture().dispose();
        for (Bullet bullet : bullets) {
            bullet.getTexture().dispose();
        }
//        for (Monster monster : monsters) {
//            monster.getTexture().dispose();
//        }

        spriteBatch.dispose();
    }
    
    public TiledMapTileLayer getCollisionLayer() {
    	return this.collisionLayer;
    }

    public ArrayList<Gun> getGuns() {
    	return this.guns;
    }

    public void removeGun(Gun g) {
    	this.guns.remove(g);
    }
    
    public void addGun(Gun g) {
    	this.guns.add(g);
    }
    
    public void setConsiderGun(Gun g) {
    	this.considerGun = g;
    }
}


