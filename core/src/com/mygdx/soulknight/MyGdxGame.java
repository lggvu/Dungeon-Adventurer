package com.mygdx.soulknight;
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

public class MyGdxGame extends ApplicationAdapter {
    private static final float CHARACTER_SPEED = 4f;
    private final float CHARACTER_SCALE_FACTOR = 0.75f;

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private Texture characterTexture, actualTexture;
    private Vector2 characterPosition;
    private SpriteBatch spriteBatch;

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
        characterTexture = new Texture("bucket.png");
        characterPosition = new Vector2(130,130);
        // Set up the sprite batch
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render () {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Move the character based on user input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveCharacter(-CHARACTER_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveCharacter(CHARACTER_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveCharacter(0, CHARACTER_SPEED);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveCharacter(0, -CHARACTER_SPEED);
        }

        // Update the camera position based on the character's position
        camera.position.x = characterPosition.x + characterTexture.getWidth() /2;
        camera.position.y = characterPosition.y + characterTexture.getHeight() / 2;
        camera.update();

        // Render the map and the character
        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(characterTexture, characterPosition.x, characterPosition.y, 0, 0, characterTexture.getWidth(), characterTexture.getHeight(), 0.5f, 0.5f, 0, 0, 0, characterTexture.getWidth(), characterTexture.getHeight(), false, false);
        

        spriteBatch.end();
    }

    private void moveCharacter(float deltaX, float deltaY) {
        // Check if the character can move to the desired position
    	int tileX = (int) ((characterPosition.x + deltaX) / collisionLayer.getTileWidth());
        int tileY = (int) ((characterPosition.y + deltaY) / collisionLayer.getTileHeight());

        // Check the tiles surrounding the character for collisions
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
        TiledMapTileLayer.Cell cellRight = collisionLayer.getCell(tileX + 1, tileY);
        TiledMapTileLayer.Cell cellTop = collisionLayer.getCell(tileX, tileY + 1);
        TiledMapTileLayer.Cell cellDiagonal = collisionLayer.getCell(tileX + 1, tileY + 1);

        // If none of the surrounding tiles are collidable, move the character
        if (cell == null && cellRight == null && cellTop == null && cellDiagonal == null) {
            characterPosition.x += deltaX;
            characterPosition.y += deltaY;
        }
    }

    @Override
    public void dispose () {
        // Dispose of resources
        tiledMap.dispose();
        mapRenderer.dispose();
        characterTexture.dispose();
        spriteBatch.dispose();
    }
}


