package com.mygdx.soulknight;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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


    @Override
    public void create () {
        // Set up the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Load the map
        tiledMap = new TmxMapLoader().load("second_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collisions");

        // Load the character texture and position
        character = new Character(new Texture("bucket.png"), tiledMap,"Collisions");
        character.setSize(0.2f * collisionLayer.getTileWidth(), 0.2f * collisionLayer.getTileHeight());
        // Set up the sprite batch
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render () {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        // Move the character based on user input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            character.move(-CHARACTER_SPEED * deltaTime,0);
            direction = new Vector2(-1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	character.move(+CHARACTER_SPEED * deltaTime, 0);
        	direction = new Vector2(1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            character.move(0, CHARACTER_SPEED * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	character.move(0, -CHARACTER_SPEED * deltaTime);
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            //Create a new bullet texture and set its position to the current position of the character
            Texture bulletTexture = new Texture("bucket.png");
            Vector2 bulletPosition = new Vector2(character.getX() + character.getTexture().getWidth() /2, character.getY() + character.getTexture().getHeight() / 2);
            direction.nor();

            //Add the new bullet to the game's list of bullets
            Bullet bullet = new Bullet(bulletTexture, bulletPosition, direction);
            bullets.add(bullet);
        }

        // ...

        // Update and draw the bullets
        
        // Update the camera position based on the character's position
        camera.position.x = character.getX() + character.getTexture().getWidth() /2;
        camera.position.y = character.getY() + character.getTexture().getHeight() / 2;
        camera.update();

        // Render the map and the character
        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(character.getTexture(), character.getX(), character.getY(), 0, 0, character.getTexture().getWidth(), character.getTexture().getHeight(), 0.5f, 0.5f, 0, 0, 0, character.getTexture().getWidth(), character.getTexture().getHeight(), false, false);
        
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()){
            //Update the bullet position based on its direction and speed
            Bullet bullet = iterator.next();
        	bullet.update();

            //Render the bullet
            spriteBatch.draw(bullet.getTexture(), bullet.getX(), bullet.getY(), 0, 0, bullet.getTexture().getWidth(), bullet.getTexture().getHeight(), 0.2f, 0.2f, 0, 0, 0, bullet.getTexture().getWidth(), bullet.getTexture().getHeight(), false, false);
            

            //Check for collision between the bullet and the collision layer
            int tileX = (int) (bullet.getX() / collisionLayer.getTileWidth());
            int tileY = (int) (bullet.getY() / collisionLayer.getTileHeight());

            TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);

            if(cell != null){
                //Remove the bullet from the game's list of bullets if it collides with a tile in the collision layer
                iterator.remove();
            }
        }

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
        spriteBatch.dispose();
    }
}


