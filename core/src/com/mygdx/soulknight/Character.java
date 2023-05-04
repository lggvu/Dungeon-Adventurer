package com.mygdx.soulknight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Character {
	private Texture texture = new Texture("bucket.png");
    private Vector2 position = new Vector2(130,130);
    public Character(Texture texture, Vector2 position) {
    	this.texture = texture;
    	this.position = position;
    }
	public Texture getTexture() {
		return texture;
	}
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	public Vector2 getPosition() {
		return position;
	}
	public void setPositionX(float Xpos) {
		this.position.x = Xpos;
	}
	public void setPositionY(float Ypos) {
		this.position.y = Ypos;
	}
    public void move(float deltaX, float deltaY, TiledMapTileLayer collisionLayer) {
    	int tileX = (int) ((this.getPosition().x + deltaX) / collisionLayer.getTileWidth());
        int tileY = (int) ((this.getPosition().y + deltaY) / collisionLayer.getTileHeight());

        // Check the tiles surrounding the character for collisions
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
        TiledMapTileLayer.Cell cellRight = collisionLayer.getCell(tileX + 1, tileY);
        TiledMapTileLayer.Cell cellTop = collisionLayer.getCell(tileX, tileY + 1);
        TiledMapTileLayer.Cell cellDiagonal = collisionLayer.getCell(tileX + 1, tileY + 1);

        // If none of the surrounding tiles are collidable, move the character
        if (cell == null && cellRight == null && cellTop == null && cellDiagonal == null) {
            this.setPositionX(this.getPosition().x += deltaX);
            this.setPositionY(this.getPosition().y += deltaY);
        }
    }

}
