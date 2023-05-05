package com.mygdx.soulknight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
public class Character extends Sprite {
	private Texture texture =new Texture("bucket.png");

	private Rectangle bounds;
	private TiledMap tiledMap;
	private TiledMapTileLayer collisionLayer;
	public Character(Texture texture, TiledMap tiledMap, String collisionLayerName) {
		super(texture);
		// Set the initial position, size, and appearance of the character
		setPosition(60, 60);
		setSize(4, 4);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setColor(1, 1, 1, 1);
		// Initialize the bounding box of the character
		bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
		// Initialize the tiled map and collision layer
		this.tiledMap = tiledMap;
		this.collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(collisionLayerName);
	}
	public Texture getTexture() {
		return texture;
	}
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	public void move(float deltaX, float deltaY) {
		// Calculate the new position of the sprite
		float newX = getX() + deltaX;
		float newY = getY() + deltaY;

		// Check if the sprite collides with the collision layer at the new position
		boolean collides = collidesWithCollisionLayer(newX, newY);

		// If the sprite does not collide with the collision layer, update its position
		if (!collides) {
			setPosition(newX, newY);
		}
	}

	private boolean collidesWithCollisionLayer(float x, float y) {
		// Calculate the tile coordinates of the sprite at the new position
		int righttileX = (int) Math.ceil(x / collisionLayer.getTileWidth());
		int lefttileX = (int) Math.floor(x / collisionLayer.getTileWidth());
		int toptileY = (int) Math.ceil(y / collisionLayer.getTileHeight());
		int bottomtileY = (int) Math.floor(y / collisionLayer.getTileHeight());

		System.out.println("topTileX "+ lefttileX);
		System.out.println("topTileY "+ toptileY);

		// Check if the sprite collides with any cell in the collision layer
		TiledMapTileLayer.Cell cell = collisionLayer.getCell(lefttileX, toptileY);
		if (cell != null) {
			return true;
		}
		TiledMapTileLayer.Cell cell1 = collisionLayer.getCell(lefttileX, bottomtileY);
		if (cell1 != null) {
			return true;
		}
		TiledMapTileLayer.Cell cell2 = collisionLayer.getCell(righttileX, toptileY);
		if (cell2 != null) {
			return true;
		}
		TiledMapTileLayer.Cell cell3 = collisionLayer.getCell(righttileX, bottomtileY);
		if (cell3 != null) {
			return true;
		}
		return false;
	}
}


