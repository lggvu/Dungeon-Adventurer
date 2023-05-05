package com.mygdx.soulknight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
public class Character extends Sprite {
	private Texture texture =new Texture("bucket.png");

	private TiledMapTileLayer collisionLayer;
	public Character(Texture texture, TiledMap tiledMap, String collisionLayerName) {
		super(texture);
		// Set the initial position, size, and appearance of the character
		setPosition(60, 60);
//		setSize(4, 4);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setColor(1, 1, 1, 1);
		// Initialize the bounding box of the character
//		bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
		// Initialize the tiled map and collision layer
		this.collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(collisionLayerName);
	}
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	public void move(float deltaX, float deltaY) {
		float overlapW = collisionLayer.getTileWidth()/12f;
		float overlapH = collisionLayer.getTileHeight()/12f;
		if (deltaX!=0 || deltaY!=0){


			float resX = getX();
			float resY = getY();
			float newX = getX() + deltaX;
			float newY = getY() + deltaY;
			// Calculate the tile coordinates of the sprite at the new position
			int righttileX = (int) Math.ceil((newX-overlapW) / collisionLayer.getTileWidth());
			int lefttileX = (int) Math.floor((newX+overlapW) / collisionLayer.getTileWidth());
			int toptileY = (int) Math.ceil((newY-overlapH) / collisionLayer.getTileHeight());
			int bottomtileY = (int) Math.floor((newY+overlapH) / collisionLayer.getTileHeight());
			// Check if the sprite collides with any cell in the collision layer
			TiledMapTileLayer.Cell cell = collisionLayer.getCell(lefttileX, toptileY);
		TiledMapTileLayer.Cell cell1 = collisionLayer.getCell(lefttileX, bottomtileY);
		TiledMapTileLayer.Cell cell2 = collisionLayer.getCell(righttileX, toptileY);
			TiledMapTileLayer.Cell cell3 = collisionLayer.getCell(righttileX, bottomtileY);

//		System.out.println("location"+newX+newY);
			if (cell == null & cell1 == null& cell2 == null& cell3 == null) {
				resX = newX;
				resY = newY;
				setPosition(resX, resY);
			} else {
				System.out.println("tile"+ righttileX + lefttileX+ toptileY +bottomtileY);
				System.out.println("character size"+ getHeight()+getWidth());
				System.out.println("tile size"+ collisionLayer.getTileWidth()+collisionLayer.getTileWidth());
				setPosition(resX, resY);
			}
		}
		}
	public boolean check_collision(float x, float y){
		int cell_X= (int) Math.ceil(x/collisionLayer.getTileWidth());
		int cell_Y=(int) Math.ceil(y/collisionLayer.getTileHeight());
		return collisionLayer.getCell(cell_X, cell_Y) != null;
	}
}


