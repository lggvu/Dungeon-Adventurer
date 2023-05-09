package com.mygdx.soulknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private TiledMapTileLayer collisionLayer;
	private float elapsedSeconds = 0;
	@Override
	public void create () {

		// Set up the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Load the map
		tiledMap = new TmxMapLoader().load("maps.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collisions");

	}

	@Override
	public void render () {
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float deltaTime = Gdx.graphics.getDeltaTime();
		elapsedSeconds += Gdx.graphics.getDeltaTime();

		// Render the map and the character
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
	
	@Override
	public void dispose () {
		// Dispose of resources
		tiledMap.dispose();
		mapRenderer.dispose();
	}
}
