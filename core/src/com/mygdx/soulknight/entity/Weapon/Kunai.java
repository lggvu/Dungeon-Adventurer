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
    protected WorldMap map;
    public void setMap(WorldMap map) {
        this.map = map;
    }


    protected SimpleCharacter owner;
    private Texture bulletTexture = new Texture("bullet/kunai.png");
    private float speed =500f;
    public boolean isFlying=false;
    public Kunai() {
        System.out.println("SIU");
    }
    public void setOwner(SimpleCharacter owner) {
        this.owner = owner;
    }
    public void shot(Vector2 direction){
        this.direction=direction;

    }
    public void update(float deltaTime){
        if(isFlying){
        float testX = this.x, testY = this.y;
        testX = this.x + direction.x * speed * deltaTime;
        testY = this.y + direction.y * speed * deltaTime;

        ArrayList<SimpleCharacter> allCharacter = map.getAllCharacter();
        Rectangle rectangleTest = new Rectangle(this.x, testY,bulletTexture.getWidth(), bulletTexture.getHeight());
        if (y != 0 && !map.isMapCollision(rectangleTest)) {
            this.y = testY;
        }
        rectangleTest = new Rectangle(testX, this.y,bulletTexture.getWidth(), bulletTexture.getHeight());
        if (x != 0 && !map.isMapCollision(rectangleTest) ) {
            this.x = testX;
        }}
    }
    public void draw(SpriteBatch batch){
        batch.draw(bulletTexture, this.x,this.y, 30,30);
    }



}
