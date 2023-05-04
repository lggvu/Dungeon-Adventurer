package com.mygdx.soulknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.monsters.Monster;

public class PlayScreen extends ApplicationAdapter {
    private TiledMap map;
    private OrthographicCamera camera;
    private TiledMapRenderer mapRenderer;
    private Monster monster;
    private SpriteBatch batch;

    @Override
    public void create () {
        batch = new SpriteBatch();

        // Load the Tiled map
        map = new TmxMapLoader().load("test-map/second_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        // Create the monster
        Texture texture = new Texture("hero/rogue-right.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        TextureRegion region = new TextureRegion(texture, texture.getWidth(), texture.getHeight());

        monster = new Monster(new Sprite(region), (TiledMapTileLayer) map.getLayers().get("Collisions"));
        monster.setSize(monster.getWidth() / 20, monster.getHeight() / 20);
        monster.setPosition(Gdx.graphics.getWidth()/2 - monster.getWidth()/2, Gdx.graphics.getHeight()/2 - monster.getHeight()/2);
    }

    @Override
    public void render () {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Update the camera
//        camera.position.set(monster.getX(), monster.getY(), 0);
        camera.update();

        // Render the Tiled map
        mapRenderer.setView(camera);
        mapRenderer.render();

        // ====== FOR DEBUG ONLY ======
        // Draw bounding boxes for the character and tiles on the map
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(monster.getX(), monster.getY(), monster.getWidth(), monster.getHeight());

        // Render bounding boxes for tiles on the collision layer
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collisions");
        for (int x = 0; x < collisionLayer.getWidth(); x++) {
            for (int y = 0; y < collisionLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                if (cell != null) {
//                    TiledMapTile tile = cell.getTile();
                    float tileX = x * collisionLayer.getTileWidth();
                    float tileY = y * collisionLayer.getTileHeight();
                        shapeRenderer.rect(tileX, tileY, collisionLayer.getTileWidth(), collisionLayer.getTileHeight());

                }
            }
        }

        shapeRenderer.end();
        // ============================

        // Update the monster's position based on keyboard input
        monster.update(Gdx.graphics.getDeltaTime());

        // Draw the monster to the screen
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        monster.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
        // Dispose of the resources
        batch.dispose();
        monster.getTexture().dispose();
        map.dispose();
        ((BatchTiledMapRenderer)mapRenderer).dispose();

    }
}

