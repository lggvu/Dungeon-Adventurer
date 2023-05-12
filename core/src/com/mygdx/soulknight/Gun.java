package com.mygdx.soulknight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Gun extends Sprite {
	private String name;
	private Character character;
	private int damage = 3;
	private int energyCost = 1;
	private int precision = 1;
	private int bulletsPerTime = 1;
	private double attackSpeed = 0.3;
	private int range = 400;
	private double criticalRate = 0.25;
	private int bulletSpeed = 600;
	private Texture gunImg = new Texture("gun_4_1.png");
	private Texture bulletImg = new Texture("bullet_4_1.png");
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private Bullet bullet;
	private double lastBulletTime; 
	private double timeInterval;
	private double currentTime;
	
	public Gun(String name, float x, float y) {
		super(new Texture("guns/" + name + "/gun.png"));
		this.name = name;
		this.initFromName(name);
		this.setSize((int) (this.getWidth()*1.7), (int) (this.getHeight()*1.7));
		this.setX(x);
		this.setY(y);
	}
	
	public Gun(String name, Character character) {
		super(new Texture("guns/" + name + "/gun.png"));
		this.name = name;
		this.initFromName(name);
		this.setSize((int) (this.getWidth()*1.7), (int) (this.getHeight()*1.7));
		this.character = character;
	}
		
	private void initFromName(String name) {
		String gunImgPath = "guns/" + name + "/gun.png";
		String bulletImgPath = "guns/" + name + "/bullet.png";
		String propertiesImgPath = "../assets/guns/" + name + "/properties.txt";

		this.gunImg = new Texture(gunImgPath);
		this.bulletImg = new Texture(bulletImgPath);
		
		Map<String, Double> dict = new HashMap<String, Double>();
		String[] property;
		try {
		    BufferedReader reader = new BufferedReader(new FileReader(propertiesImgPath));
		    String line;
		    while ((line = reader.readLine()) != null) {
		        property = line.split(":");
		        dict.put(property[0], Double.parseDouble(property[1]));
		    }
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		this.damage = dict.get("damage").intValue();
		this.energyCost = dict.get("energy_cost").intValue();
		this.precision = dict.get("precision").intValue();
		this.bulletsPerTime = dict.get("bullets_per_time").intValue();
		this.attackSpeed = dict.get("attack_speed");
		this.range = dict.get("range").intValue();
		this.criticalRate = dict.get("critical_rate");
		this.bulletSpeed = dict.get("bullet_speed").intValue();
	}
	
	public void fire() {
        this.currentTime = TimeUtils.nanoTime();
        this.timeInterval = (this.currentTime - this.lastBulletTime)/1e9;
        
		if(this.timeInterval >= this.attackSpeed) {
			if (!this.isFlipX()) {
				this.bullet = new Bullet(this, new Vector2(1, 0));
			}
			else {
				this.bullet = new Bullet(this, new Vector2(-1, 0));
			}
			this.bullets.add(bullet);
	        this.lastBulletTime = this.currentTime;
	        this.timeInterval = 0;
		}
	}
	
	public void update() {
		if (!this.character.isFlipX()) {
			this.setFlip(false, false);
			this.setPosition(this.character.getX() + this.character.getWidth()/2, 
					this.character.getY() + (this.character.getHeight() - this.getHeight())/2);
		}
		else {
			this.setFlip(true, false);
			this.setPosition(this.character.getX() + this.character.getWidth()/2 - this.getWidth(), 
					this.character.getY() + (this.character.getHeight() - this.getHeight())/2);
		}
	}
	
	public void Draw(SpriteBatch batch) {
		this.update();
		this.draw(batch);
		for (Bullet b: bullets) {
			b.Draw(batch);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Gun && this.getName().equals(((Gun) o).getName()) && this.getX() == ((Gun) o).getX() && this.getY() == ((Gun) o).getY();
	}
	
	public Character getCharacter() {
		return this.character;
	}
	
	public Texture getBulletImg() {
		return this.bulletImg;
	}
	
	public int getRange() {
		return this.range;
	}
	
	public int getBulletSpeed() {
		return this.bulletSpeed;
	}
	
	public String getName() {
		return this.name;
	}
}
