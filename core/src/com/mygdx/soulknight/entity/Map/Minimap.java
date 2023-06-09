package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.soulknight.entity.Character.Player;

public class Minimap {

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera = new OrthographicCamera();
    private Player player;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    private float unitScale = 1/20f;
    private float widthMap, heightMap;
    private float border = 3f;
    private float padding = 3f;
    private float marginWidth = 0f;
    private float marginHeight = 0f;

    public Minimap(TiledMap map, Player player) {
        this.player = player;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        widthMap = Float.parseFloat(map.getProperties().get("width").toString()) * Float.parseFloat(map.getProperties().get("tilewidth").toString());
        heightMap = Float.parseFloat(map.getProperties().get("height").toString()) * Float.parseFloat(map.getProperties().get("tileheight").toString());;

        camera.setToOrtho(false, Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 1.5f);

        camera.position.x = Gdx.graphics.getWidth() / 3f - (marginWidth + border + padding) / 1.5f;
        camera.position.y = Gdx.graphics.getHeight() / 3f- (marginHeight + border + padding) / 1.5f;

        camera.update();
        renderer.setView(camera);
    }

    public void render(){
//      Draw minimap boundary
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(marginWidth, marginHeight, widthMap * unitScale * 1.5f + (border + padding) * 2, heightMap * unitScale * 1.5f + (border + padding) * 2);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(marginWidth + border, marginHeight + border, widthMap * unitScale * 1.5f + padding * 2, heightMap * unitScale * 1.5f + padding * 2);
        shapeRenderer.end();

//        Draw minimap
        renderer.render();

//        Draw player in minimap
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(player.getX() * unitScale * 1.5f + (marginWidth + border + padding), player.getY() * unitScale * 1.5f + (marginHeight + border + padding), 4);
        shapeRenderer.end();
    }

}