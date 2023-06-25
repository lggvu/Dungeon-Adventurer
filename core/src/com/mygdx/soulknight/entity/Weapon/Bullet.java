package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 direction;
    private float speed = 1000f;
    private int dmg = 2;
    private float x, y, startX, startY;
    private float width = 17;
    private float height = 17;
    private TextureRegion textureRegion;

    public Vector2 getDirection() {
        return direction;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Bullet(TextureRegion textureRegion, float x, float y, Vector2 direction, float speed) {
        this.x = this.startX = x - width / 2;
        this.y = this.startY = y - height / 2;
        this.direction = direction.nor();
        this.textureRegion = textureRegion;
        this.speed = speed;
    }

    public float getDistanceGoThrough() {
        return new Vector2(x, y).dst(startX, startY);
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

    public int getDmg() {
        return dmg;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void draw(SpriteBatch batch) {
        float degree = direction.angleDeg(new Vector2(1, 0));
        batch.draw(textureRegion, this.x, this.y,width/2,height/2, width, height,1,1 , degree);
    }
}
