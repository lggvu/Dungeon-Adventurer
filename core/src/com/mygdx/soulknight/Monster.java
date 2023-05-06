package com.mygdx.soulknight;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Monster extends Character {
    private ArrayList<Bullet> monsterBullets = new ArrayList<Bullet>();

	public Monster(Texture texture, TiledMap tiledMap, String collisionLayerName) {
		super(texture, tiledMap, collisionLayerName);
		// TODO Auto-generated constructor stub
		this.HP = 100;
		this.armor = 0;
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(collisionLayerName);
        int mapWidth = collisionLayer.getWidth();
        int mapHeight = collisionLayer.getHeight();

        while (true) {
            int x = MathUtils.random(mapWidth - 1);
            int y = MathUtils.random(mapHeight - 1);

            if (collisionLayer.getCell(x, y) == null) {
                Vector2 position = new Vector2(x * collisionLayer.getTileWidth(), y * collisionLayer.getTileHeight());
                this.setX(position.x);
                this.setY(position.y);
                break;
            }
        }
	}
    public ArrayList<Bullet> shootBullets() {
        Vector2[] directions = {new Vector2(1, 0), new Vector2(-1, 0), new Vector2(0, 1), new Vector2(0, -1)};
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        for (Vector2 direction : directions) {
            Texture bulletTexture = new Texture("bucket.png");
            Vector2 bulletPosition = new Vector2(this.getX() + this.getTexture().getWidth() / 2, this.getY() + this.getTexture().getHeight() / 2);
            direction.nor();
            Bullet bullet = new Bullet(bulletTexture, bulletPosition, direction);
            monsterBullets.add(bullet);
            bullets.add(bullet);
        }
        return bullets;
    }
    public ArrayList<Bullet> getMonsterBullets() {
    	return monsterBullets;
    }

	
	
	
	
	
}
