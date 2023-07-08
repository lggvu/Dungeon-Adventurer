package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.WorldMap;

import java.util.ArrayList;

public class Kunai {
    private float x;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private float y;
    private Vector2 direction;
//    protected WorldMap map;
//    public void setMap(WorldMap map) {
//        this.map = map;
//    }


    protected SimpleCharacter owner;
    private Texture bulletTexture = new Texture("bullet/kunai.png");
    private float speed =50f;
    public boolean isFlying=false;
    public Kunai(){}
    public void setOwner(SimpleCharacter owner) {

        this.owner = owner;

    }
    public void shot(Vector2 direction){
        this.x=this.owner.getX()+this.owner.getWidth()/2;
        this.y=this.owner.getY()+this.owner.getHeight()/2;
        this.direction=direction;
        this.isFlying=true;

    }
    public void update(float deltaTime, WorldMap map){
        if(this.isFlying) {

            float testX = this.x + direction.x * speed * deltaTime;
            float testY = this.y + direction.y * speed * deltaTime;

            Rectangle rectangleTest = new Rectangle(this.x, testY, bulletTexture.getWidth()/20, bulletTexture.getHeight()/20);
            if (!map.isMapCollision(rectangleTest)) {
                System.out.println("MOvingggg");
                this.y = testY;
            }
            rectangleTest = new Rectangle(testX, this.y, bulletTexture.getWidth()/20, bulletTexture.getHeight()/20);
            if (!map.isMapCollision(rectangleTest)) {
                this.x = testX;
            }
        }
    }
    public void draw(SpriteBatch batch){
        batch.draw(bulletTexture, this.x,this.y, 20,20);
    }



}
