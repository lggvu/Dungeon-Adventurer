package com.mygdx.soulknight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Bullet extends Sprite {
	private Vector2 direction;
	private Gun gun;
	private Vector2 originalPos;
	private TiledMapTileLayer collisionLayer;
	private float deltaTime;
	
    public Bullet(Gun gun, Vector2 direction){
    	super(gun.getBulletImg());
    	this.gun = gun;
        this.direction = direction;
        this.deltaTime = Gdx.graphics.getDeltaTime(); 
        this.setSize((int) (this.getWidth()*1.7), (int) (this.getHeight()*1.7));
        if (!this.gun.isFlipX()) {
	        this.setPosition(this.gun.getX() + this.gun.getWidth() - this.deltaTime*direction.x*this.gun.getBulletSpeed(), 
	        		this.gun.getY() + this.gun.getHeight() - this.getHeight() - this.deltaTime*direction.y*this.gun.getBulletSpeed());
        }
        else {
	        this.setPosition(this.gun.getX() - this.getWidth() - this.deltaTime*direction.x*this.gun.getBulletSpeed(), 
	        		this.gun.getY() + this.gun.getHeight() - this.getHeight() - this.deltaTime*direction.y*this.gun.getBulletSpeed());
        }
        this.originalPos = new Vector2(this.getX(), this.getY());
        this.collisionLayer = this.gun.getCharacter().getCollisionLayer();
    }

    public void update(){
        this.setPosition(this.getX() + this.deltaTime*direction.x*this.gun.getBulletSpeed(), 
        		this.getY() + this.deltaTime*direction.y*this.gun.getBulletSpeed());
    }
    
    public void Draw(SpriteBatch batch) {
    	if((this.getX() - this.originalPos.x) < this.gun.getRange() && ((!this.checkCollision("right") && !this.isFlipX()) || 
    			((!this.checkCollision("left") && this.isFlipX())))) {
	    	this.update();
	    	this.draw(batch);
    	}
    }

	public boolean checkCollision(String dir){
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		
		if (dir.equals("left")) {
			x1 = this.getX();
			y1 = this.getY() + this.getHeight();
			x2 = this.getX();
			y2 = this.getY();
		}
		if (dir.equals("right")) {
			x1 = this.getX() + this.getWidth();
			y1 = this.getY();
			x2 = this.getX() + this.getWidth();
			y2 = this.getY() + this.getHeight();
		}
		if (dir.equals("down")) {
			x1 = this.getX();
			y1 = this.getY();
			x2 = this.getX() + this.getWidth();
			y2 = this.getY();
		}
		if (dir.equals("up")) {
			x1 = this.getX();
			y1 = this.getY() + this.getHeight();
			x2 = this.getX() + this.getWidth();
			y2 = this.getY() + this.getHeight();
		}
		
		if (collisionLayer.getCell((int) x1/collisionLayer.getTileWidth(), (int) y1/collisionLayer.getTileHeight()) != null || 
				collisionLayer.getCell((int) x2/collisionLayer.getTileWidth(), (int) y2/collisionLayer.getTileHeight()) != null) {
			return true;
		}
		
		return false;
	}
}