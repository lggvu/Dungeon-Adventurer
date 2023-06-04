package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Weapon.Bullet;

public class Explosion {
    private float initialX;
    private float initialY;

    private float x;
    private float y;
    private float duration=1f;
    private float durationTimeRemain=duration;
    private SimpleCharacter affectedCharacter;
    private float relativeHitX;
    private float relativeHitY;
    private Bullet bullet;

    private Texture texture;

    public Explosion(String texturePath,float initialX, float initialY, SimpleCharacter affectedCharacter, Bullet bullet) {
        texture = new Texture(texturePath);
        this.initialX = initialX;
        this.x=initialX;

        this.initialY = initialY;
        this.y=initialY;
        this.affectedCharacter = affectedCharacter;
        this.bullet = bullet;
        this.relativeHitX=bullet.getX()- affectedCharacter.getX()- Math.signum(bullet.getX()- affectedCharacter.getX())*affectedCharacter.getWidth()/3;
        this.relativeHitX=bullet.getY()- affectedCharacter.getY()- Math.signum(bullet.getY()- affectedCharacter.getY())*affectedCharacter.getHeight()/3;
    }

    public void update(float deltaTime){
        durationTimeRemain-=deltaTime;
        setX(affectedCharacter.getX()+relativeHitX);
        setY(affectedCharacter.getY()+relativeHitY);
    }
    public void draw(SpriteBatch batch) {
        batch.draw(texture, this.initialX, this.initialY, 20, 20);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getInitialX() {
        return initialX;
    }

    public void setInitialX(float initialX) {
        this.initialX = initialX;
    }

    public float getInitialY() {
        return initialY;
    }


    public float getDurationTimeRemain() {
        return durationTimeRemain;
    }

    public void setDurationTimeRemain(float durationTimeRemain) {
        durationTimeRemain = durationTimeRemain;
    }

    public SimpleCharacter getAffectedCharacter() {
        return affectedCharacter;
    }

    public void setAffectedCharacter(SimpleCharacter affectedCharacter) {
        this.affectedCharacter = affectedCharacter;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }
}
