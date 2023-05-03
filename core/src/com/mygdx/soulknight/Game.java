package com.mygdx.soulknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture playerTexture;
	private float playerX, playerY;
	float playerSpeed = 200;

	private TiledMap map;
	private OrthographicCamera camera;
	private TiledMapRenderer mapRenderer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerTexture = new Texture("hero/knight1.png");
		// resize the player texture to 50x50
		playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		playerX = Gdx.graphics.getWidth() / 2 - playerTexture.getWidth() / 2;
		playerY = Gdx.graphics.getHeight() / 2 - playerTexture.getHeight() / 2;

//		// Load Tiled map
//		map = new TmxMapLoader().load("test-map/second_map.tmx");
//
//
//		// Create camera
//		camera = new OrthographicCamera();
//		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//		// Create map renderer
//		mapRenderer = new OrthogonalTiledMapRenderer(map);
	}

	@Override
	public void render () {
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Move the player based on input
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			playerX -= playerSpeed * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playerX += playerSpeed * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			playerY += playerSpeed * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			playerY -= playerSpeed * Gdx.graphics.getDeltaTime();
		}

		// Draw the player texture at its current position
		batch.begin();
		batch.draw(playerTexture, playerX, playerY);
		batch.end();
//
//		// Update the camera
//		camera.update();
//
//		// Set the batch's projection matrix to the camera's combined matrix
//		batch.setProjectionMatrix(camera.combined);
//
//		// Begin drawing the batch
//		batch.begin();
//
//		// Draw the player sprite
//		batch.draw(playerTexture, playerX, playerY);
//
//		// End drawing the batch
//		batch.end();
//
//		// Render the Tiled map
//		mapRenderer.setView(camera);
//		mapRenderer.render();
	}
	
	@Override
	public void dispose () {
		// Dispose of the resources
		batch.dispose();
		playerTexture.dispose();
//		map.dispose();

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
