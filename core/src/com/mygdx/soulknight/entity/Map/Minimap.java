package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Minimap {

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera = new OrthographicCamera();
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Minimap(TiledMap map) {

        renderer = new OrthogonalTiledMapRenderer(map, 1 / 20f);
        int width = Integer.parseInt(map.getProperties().get("width").toString());
        int height = Integer.parseInt(map.getProperties().get("height").toString());
        int tilewidth = Integer.parseInt(map.getProperties().get("tilewidth").toString());
        int tileheight = Integer.parseInt(map.getProperties().get("tileheight").toString());


        camera.setToOrtho(false, 30, 30);

        camera.zoom = 30;

        camera.position.x = -300; //Gdx.graphics.getWidth() - 200;
        camera.position.y = -200; //Gdx.graphics.getHeight() - 200;
        camera.update();
        renderer.setView(camera.combined, 0, 0, 500, 500);
    }

    public void render(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 200, 250, 120);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(Gdx.graphics.getWidth() - 245, Gdx.graphics.getHeight() - 195, 240, 110);
        shapeRenderer.end();
        renderer.render();
    }

}