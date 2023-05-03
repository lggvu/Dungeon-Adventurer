package com.mygdx.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Monster extends Sprite {
    private float speed = 200; // Speed of movement
    private int hp;
    private int damage;
    private int defense;

    private float monsterX;
    private float monsterY;

    public Monster(Texture texture) {
        super(texture);
    }

//    public Monster(Texture texture, TiledMap map) {
//        super(texture
//    }

    public Monster(Sprite sprite) {
        super(sprite);
    }

//    public Monster(Sprite sprite , TiledMapTileLayer collisionLayer)

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    public void update(float delta, TiledMap map) {
//        monsterX = Gdx.graphics.getWidth() / 2 - this.getWidth()  /2;
//        monsterY = Gdx.graphics.getHeight() / 2 - this.getHeight() /2;

        // Move the monster based on the input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.translateX(-speed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.translateX(speed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.translateY(speed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.translateY(-speed * delta);
        }

        // Handle collision
        // Get the tile layer from the TiledMap
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get("Collisions");

        // Get the cell of the tile at the monster's position
        TiledMapTileLayer.Cell cell = tileLayer.getCell((int) (this.getX() / tileLayer.getTileWidth()),
                (int) (this.getY() / tileLayer.getTileHeight()));

        // Check if the cell is null or contains a tile with a collision property
        if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("collision")) {
            // Handle collision here
            System.out.println("Collision!");
        }


    }
}