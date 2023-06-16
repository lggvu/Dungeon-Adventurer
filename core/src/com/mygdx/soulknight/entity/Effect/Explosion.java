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

    private float x;
    private float y;

    private SimpleCharacter affectedCharacter;
    private Bullet bullet;

    private float stateTime;
    private float durationTimeRemain=2f;
    private Animation<TextureRegion> explosionAnimation;
    public Explosion(String texturePath,float x, float y, Animation<TextureRegion> explosionAnimation) {
        this.x = x;
        this.y= y;


//        animation

//        Texture explosionSheet = new Texture(Gdx.files.internal("explosion/pngwing.com.png"));
        this.explosionAnimation=explosionAnimation;
        stateTime = 0f;

    }
    private float deltaTime;
    public void update(float deltaTime){
        this.deltaTime=deltaTime;
        durationTimeRemain-=deltaTime;
    }
    public void draw(SpriteBatch batch) {
        stateTime += this.deltaTime;
        TextureRegion currentFrame = this.explosionAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, this.x, this.y, 40, 40);
    }

    public float getDurationTimeRemain() {

        return durationTimeRemain;
    }

    public Bullet getBullet() {
        return bullet;
    }

}
