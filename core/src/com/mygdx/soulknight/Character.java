package com.mygdx.soulknight;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
public class Character extends Sprite {
	protected Texture texture = new Texture("bucket.png");
	protected int HP = 100;
	protected int armor = 100;
	protected Vector2 position = new Vector2(32, 32); 
	protected int speed = 200;
	protected Gun gun;
	protected MyGdxGame game;
		
	public Character(Texture texture, MyGdxGame game) {
		super(texture);
		this.setSize(this.getWidth(), this.getHeight());
		this.game = game;
//		this.game.getthis.game.getCollisionLayer()() = this.game.getthis.game.getCollisionLayer()();
//		this.game.getGuns() = this.game.getGuns();
		this.gun = new Gun("4", this);		
	}
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void update(float deltaTime) {
		if (Gdx.input.isKeyPressed(Keys.A) && !checkCollision("left", deltaTime)) {
			position.x-=deltaTime*speed;
			this.setFlip(true, false);
		}
		if (Gdx.input.isKeyPressed(Keys.D) && !checkCollision("right", deltaTime)) {
			position.x+=deltaTime*speed;
			this.setFlip(false, false);
		}
		if (Gdx.input.isKeyPressed(Keys.S) && !checkCollision("down", deltaTime)) {
			position.y-=deltaTime*speed;
		}
		if (Gdx.input.isKeyPressed(Keys.W) && !checkCollision("up", deltaTime)) {
			position.y+=deltaTime*speed;
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE) && this.gun != null) {			
			this.gun.fire();
		}
		
		Gun g = this.collectGun();
		this.game.setConsiderGun(g);
	
		if (g != null && Gdx.input.isKeyJustPressed(Keys.F)) {
			if (this.gun != null) {
				this.game.addGun(new Gun(this.gun.getName(), g.getX(), g.getY()));
			}
			this.gun = new Gun(g.getName(), this);		
			this.game.removeGun(g);
		};		
	}
	
	public void Draw(SpriteBatch batch) {
		update(Gdx.graphics.getDeltaTime());
		this.setPosition(position.x, position.y);
		if (!this.isFlipX()) {
			this.draw(batch);
			if (this.gun != null) {
				gun.Draw(batch);
			}
		}
		else {
			if (this.gun != null) {
				gun.Draw(batch);
			}
			this.draw(batch);
		}
	}
	
	public boolean checkCollision(String dir, float deltaTime){
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		
		if (dir.equals("left")) {
			x1 = position.x + this.getWidth()/4 - deltaTime*speed;
			y1 = position.y + this.getHeight()/2;
			x2 = position.x + this.getWidth()/4 - deltaTime*speed;
			y2 = position.y;
		}
		if (dir.equals("right")) {
			x1 = position.x + 3*this.getWidth()/4 + deltaTime*speed;
			y1 = position.y + this.getHeight()/2;
			x2 = position.x + 3*this.getWidth()/4 + deltaTime*speed;
			y2 = position.y;
		}
		if (dir.equals("down")) {
			x1 = position.x + this.getWidth()/4;
			y1 = position.y - deltaTime*speed;
			x2 = position.x + 3*this.getWidth()/4;
			y2 = position.y - deltaTime*speed;
		}
		if (dir.equals("up")) {
			x1 = position.x + this.getWidth()/4;
			y1 = position.y + this.getHeight()/2 + deltaTime*speed;
			x2 = position.x + 3*this.getWidth()/4;
			y2 = position.y + this.getHeight()/2 + deltaTime*speed;
		}
		
		if (this.game.getCollisionLayer().getCell((int) x1/this.game.getCollisionLayer().getTileWidth(), (int) y1/this.game.getCollisionLayer().getTileHeight()) != null || 
				this.game.getCollisionLayer().getCell((int) x2/this.game.getCollisionLayer().getTileWidth(), (int) y2/this.game.getCollisionLayer().getTileHeight()) != null) {
			return true;
		}
		
		return false;
	}
	
	public boolean isOverlap(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
		return ((x1 <= x2 && x2 < x1 + w1) ||
			   (x2 <= x1 && x1 < x2 + w2)) &&
			   ((y1 <= y2 && y2 < y1 + h1) ||
			   (y2 <= y1 && y1 < y2 + h2));
	}
	
	public Gun collectGun() {
		float x = this.getX();
		float y = this.getY();

		for (Gun g: this.game.getGuns()) {
			if (this.isOverlap(x, y, this.getWidth(), this.getHeight(), g.getX(), g.getY(), g.getWidth(), g.getHeight())) {
				return g;
			}
		}
		
		return null;
	}
	
	

	public void getHit(int dmg) {
		if (this.armor > dmg) {
			this.armor -= dmg;
		}
		else {
			this.armor = 0;
			this.HP  -= (dmg - this.armor);
		}
	}
    public Rectangle getBoundingRectangle() {
        return new Rectangle(this.getX(), this.getY(), texture.getWidth() * 0.5f, texture.getHeight() * 0.5f);
    }
	public int getHP() {
		return HP;
	}
	public int getArmor() {
		return armor;
	}
	public TiledMapTileLayer getCollisionLayer() {
		return this.game.getCollisionLayer();
	}
}


