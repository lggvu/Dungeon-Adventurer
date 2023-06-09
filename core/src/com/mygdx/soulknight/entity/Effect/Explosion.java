package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Weapon.Bullet;

public class Explosion {
    private float initialX;
    private float initialY;
    private TextureRegion[] explosionFrames;
    private Animation<TextureRegion> explosionAnimation;
    private float x;
    private float y;
    private float duration=1f;
    private float durationTimeRemain=duration;
    private SimpleCharacter affectedCharacter;
    private float relativeHitX;
    private float relativeHitY;
    private Bullet bullet;

    private Texture texture;
    private float stateTime;

    public Explosion(String texturePath,float initialX, float initialY, SimpleCharacter affectedCharacter, Bullet bullet) {
        texture = new Texture(texturePath);
        this.initialX = initialX;
        this.x=initialX;
        float characterCenterX=affectedCharacter.getX()+affectedCharacter.getWidth()/2;
        float characterCenterY=affectedCharacter.getY()+affectedCharacter.getWidth()/2;
        float bulletCenterX= bullet.getX()+bullet.getWidth()/2;
        float bulletCenterY= bullet.getY()+bullet.getHeight()/2;
//        Vector2 direction=bullet.getDirection().nor();
        this.initialY = initialY;
        this.y=initialY;
        this.affectedCharacter = affectedCharacter;
        this.bullet = bullet;
        float scale=0.6f;
        Vector2 direction=new Vector2(bulletCenterX-characterCenterX,bulletCenterY-characterCenterY);
        direction=direction.nor();
        float scaleX=affectedCharacter.getWidth()/4;
        float scaleY=affectedCharacter.getHeight()/4;


//        animation
        this.relativeHitX=scaleX*direction.x;
        this.relativeHitY=scaleY*direction.y;

        Texture explosionSheet = new Texture(Gdx.files.internal("explosion/pngwing.com.png"));
        int frameCols = 8;
        int frameRows = 6;
        int frameWidth = explosionSheet.getWidth() / frameCols;
        int frameHeight = explosionSheet.getHeight() / frameRows;
        TextureRegion[][] temp = TextureRegion.split(explosionSheet, frameWidth, frameHeight);
        explosionFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int row = 0; row < frameRows; row++) {
            for (int col = 0; col < frameCols; col++) {
                explosionFrames[index++] = temp[row][col];
            }
        }
        explosionAnimation = new Animation<TextureRegion>(0.05f, explosionFrames);
        stateTime = 0f;

    }

    public void update(float deltaTime){
        durationTimeRemain-=deltaTime;
        float characterCenterX=affectedCharacter.getX()+affectedCharacter.getWidth()/2;
        float characterCenterY=affectedCharacter.getY()+affectedCharacter.getWidth()/2;

        setX(characterCenterX+this.relativeHitX);
        setY(characterCenterY+this.relativeHitY);
    }
    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, this.x, this.y, 20, 20);
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
