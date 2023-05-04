package com.mygdx.soulknight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Texture texture;
    private Vector2 position;
    private Vector2 direction;
    private float speed = 10f;

    public Bullet(Texture texture, Vector2 position, Vector2 direction){
        this.texture = texture;
        this.position = position;
        this.direction = direction;
    }

    public void update(){
        //Update the bullet position based on its direction and speed
        position.x += direction.x * speed;
        position.y += direction.y * speed;
    }

    public Texture getTexture(){
        return texture;
    }

    public Vector2 getPosition(){
        return position;
    }
}