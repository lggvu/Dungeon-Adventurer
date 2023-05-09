package com.mygdx.soulknight.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.game.SoulKnight;
import com.mygdx.soulknight.game.util.ReuseCode;

public class Bullet extends Sprite {
    SoulKnight game;
    private Vector2 direction;
    private float speed = 300f;



    private int dmg = 50;
    private float rotation = 0;

    public Bullet(Texture texture, SoulKnight game, Vector2 position, Vector2 direction) {
        super(texture);
        this.game = game;
        setPosition(position.x, position.y);
        this.direction = direction;
        setSize(32, 32);
    }

    public int getDmg() {
        return dmg;
    }

    public void update(float deltaTime){
        //Update the bullet position based on its direction and speed
        this.rotation = this.direction.angleDeg(new Vector2(1, 0));
//        this.rotation += deltaTime * 30;
        this.setX(this.getX() + direction.x * speed * deltaTime);
        this.setY(this.getY() + direction.y * speed * deltaTime);
    }

    public Rectangle getBoundingRectangle() {
        return new Rectangle(this.getX(), getY(), getWidth(), getHeight());
    }

//    This function will be an abstract class when we have more bullet type
    public boolean handleCollideWithWall() {
        if (ReuseCode.isMapCollision(game, getX(), getY())) {
            dispose();
//        If return true, dispose bullet
            return true;
        }
        return false;
    }

    public void render() {
        float scaleX = getWidth() / getTexture().getWidth();
        float scaleY = getHeight() / getTexture().getHeight();
        game.getBatch().draw(getTexture(), getX(), getY(),getWidth() / 2, getHeight() / 2, getTexture().getWidth(), getTexture().getHeight(), scaleX, scaleY, rotation, 0, 0, getTexture().getWidth(), getTexture().getHeight(), false, false);
    }

    public void dispose() {
        getTexture().dispose();
    }
}
