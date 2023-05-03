package com.mygdx.soulknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
        monster = new Monster(new Texture("hero/rogue-right.png"));
        monster.setSize(monster.getWidth() / 16, monster.getHeight() / 16);
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

        // Update the monster's position based on keyboard input
        monster.update(Gdx.graphics.getDeltaTime(), map);

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

//		// Dispose of the Batch used by the OrthogonalTiledMapRenderer
//		Batch batch = ((BatchTiledMapRenderer)mapRenderer).getBatch();
//		if (batch != null) {
//			batch.dispose();
//		}
//
//		// Dispose of the OrthogonalTiledMapRenderer
//		((BatchTiledMapRenderer)mapRenderer).dispose();
    }
}

