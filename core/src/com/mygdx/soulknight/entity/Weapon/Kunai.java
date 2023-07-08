package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private TextureRegion bulletTexture = new TextureRegion(new Texture("weapon/kunai.png"));
    private float speed =50f;
    public boolean isFlying=false;
    public Kunai(){}
    public void setOwner(SimpleCharacter owner) {

        this.owner = owner;

    }
    public void shot(Vector2 direction){
        this.x=this.owner.getX();
        this.y=this.owner.getY();
        this.direction=direction;
        this.isFlying=true;

    }
    public void update(float deltaTime, WorldMap map){
        if(this.isFlying) {

            float testX = this.x + direction.x * speed * deltaTime;
            float testY = this.y + direction.y * speed * deltaTime;

            Rectangle rectangleTest = new Rectangle(this.x, testY, owner.getMaxWidth(), owner.getMaxHeight());
            if (!map.isMapCollision(rectangleTest)){
                this.y = testY;
            }
            rectangleTest = new Rectangle(testX, this.y, owner.getMaxWidth(), owner.getMaxHeight());
            if (!map.isMapCollision(rectangleTest)) {
                this.x = testX;
            }
        }
    }
    public void draw(SpriteBatch batch){
        float height = 17, width = bulletTexture.getTexture().getWidth() * height / bulletTexture.getTexture().getHeight();
        float degree = direction.angleDeg(new Vector2(1, 0));
        batch.draw(bulletTexture, this.x,this.y, width/2,height/2, width, height,1,1 , degree);
    }



}
