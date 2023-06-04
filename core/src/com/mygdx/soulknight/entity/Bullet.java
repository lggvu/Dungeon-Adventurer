package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 direction;
    public float impactForce=2f;

    public float pushTimer = 0.5f;
    private float speed = 1000f;
    private int dmg = 2;
    private float x, y;
    private float width = 17;
    private float height = 17;
    private Texture texture;

    private float explosionDuration=17f;

    public float getExplosionWidth() {
        return explosionWidth;
    }

    public float getExplosionHeight() {
        return explosionHeight;
    }

    private float explosionWidth=17f;

    private float explosionHeight=17f;

    private float explosionDurationRemain=explosionDuration;
    public float getExplosionDuration(){
        return this.explosionDuration;
    }
    public float getExplosionDurationRemain(){
        return this.explosionDurationRemain;
    }
    public void setExplosionDurationRemain(float timeRemain){
        this.explosionDurationRemain=timeRemain;
    }

    public Bullet(String texturePath, float x, float y, Vector2 direction, float speed) {
        this.x = x;
        this.y = y;
        this.direction = direction.nor();
        texture = new Texture(texturePath);
        this.speed = speed;
    }

    public void update(float deltaTime) {
        this.x += direction.x * speed * deltaTime;
        this.y += direction.y * speed * deltaTime;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Rectangle getRectangle() { return new Rectangle(x, y, width, height); }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }
    public Vector2 getDirection(){
        return this.direction;
    }
    public int getDmg() {
        return dmg;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, this.x, this.y, width, height);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
