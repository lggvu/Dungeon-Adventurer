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
    private float width;
    private float height;
    private float stateTime;
    private Animation<TextureRegion> explosionAnimation;
    public Explosion(String texturePath,float x, float y, Animation<TextureRegion> explosionAnimation) {
        this.width = 60;
        this.height = 60;
        this.x = x - this.width/2;
        this.y = y - this.height/2;
//        animation
        this.explosionAnimation=explosionAnimation;
        stateTime = 0f;
    }
    public void update(float deltaTime){
        stateTime += deltaTime;
    }
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = this.explosionAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, this.x, this.y, this.width, this.height);
    }

    public boolean isExplosionFinish() {
        return this.explosionAnimation.isAnimationFinished(stateTime);
    }

    public Bullet getBullet() {
        return bullet;
    }

}
