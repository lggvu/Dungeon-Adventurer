package com.mygdx.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Monster extends Sprite {
    // Character stats
    private final int SPEED = 150; // Speed of movement
    private int hp;
    private int damage;
    private int defense;

    private Vector2 characterPosition = new Vector2();
    private TiledMapTileLayer collisionLayer;
    private final int COLLISION_STEP_CHECK = 8;

    public Monster(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
    }

    public Vector2 getCharacterPosition() {
        return this.characterPosition;
    }

    @Override
    public void draw(Batch Batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(Batch);
    }

    private boolean isCellBlocked(float x, float y) {
        TiledMapTileLayer.Cell cell;
        cell = this.collisionLayer.getCell((int) (x / this.collisionLayer.getTileWidth()), (int) (y / this.collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null;
    }

    public boolean collidesRight() {
        boolean collides = false;

        for(float step = 0; step < getHeight(); step += this.collisionLayer.getTileHeight() / COLLISION_STEP_CHECK) {
            if (collides = isCellBlocked(getX() + getWidth(), getY() + step)) {
                break;
            }
        }
        return collides;
    }

    public boolean collidesLeft() {
        boolean collides = false;
        for(float step = 0; step < getHeight(); step += this.collisionLayer.getTileHeight() / COLLISION_STEP_CHECK) {
            if (collides = isCellBlocked(getX()-getWidth(), getY() + step)) {
                break;
            }
        }
        return collides;
    }

    public boolean collidesTop() {
        boolean collides = false;
        for(float step = 0; step < getWidth(); step += this.collisionLayer.getTileWidth() / COLLISION_STEP_CHECK) {
            if (collides = isCellBlocked(getX() + step, getY())) {
                break;
            }
        }
        return collides;
    }

    public boolean collidesBottom() {
        boolean collides = false;
        for(float step = 0; step < getWidth(); step += this.collisionLayer.getTileWidth() / COLLISION_STEP_CHECK) {
            if (collides = isCellBlocked(getX() + step, getY())) {
                break;
            }
        }
        return collides;
    }

    public void update(float delta) {
        boolean collisionX, collisionY;
        float oldX = this.getX(), oldY = this.getY();

        // Move the monster based on the input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            System.out.println("left");
            collisionX = collidesLeft();
            System.out.println(collisionX);

            // reacting to collision
            if(collisionX) {
                this.setX(oldX);
            }
            else {
                this.translateX(-SPEED*delta);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            System.out.println("right");
            collisionX = collidesRight();
            System.out.println(collisionX);

            // reacting to collision
            if(collisionX) {
                this.setX(oldX);
            }
            else {
                this.translateX(SPEED*delta);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            System.out.println("up");
            collisionY = collidesTop();
            System.out.println(collisionY);

            if(collisionY) {
                this.setY(oldY);
            }
            else {
                this.translateY(SPEED*delta);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            System.out.println("down");
            collisionY = collidesBottom();
            System.out.println(collisionY);

            if(collisionY) {
                this.setY(oldY);
            }
            else {
                this.translateY(-SPEED*delta);
            }
        }
    }
}