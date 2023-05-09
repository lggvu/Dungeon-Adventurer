package com.mygdx.soulknight.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.game.SoulKnight;
import com.mygdx.soulknight.game.util.ReuseCode;

import java.util.HashMap;

public abstract class AnimationCharecter extends Character {
    private static final float ANIMATION_SPEED = 0.1f;
    private TextureRegion[][] allTexture;
    private HashMap<Integer, Integer> degree2AnimationID;
    private float stateTime = 0f;
    private int textureWidth, textureHeight;
    private TextureRegion currentTextureRegion;
    private int frame_cols = 8;
    private int frame_rows = 8;
    public AnimationCharecter(SoulKnight game, Texture texture) {
        super(texture, game);
        degree2AnimationID = new HashMap<>();
        Texture walkSheet = getTexture();
        textureWidth = 50;
        textureHeight = 90;
        allTexture = ReuseCode.splitTexture(walkSheet,
                textureWidth, textureHeight, 25, 28, frame_cols, frame_rows
        );
        degree2AnimationID.put(0, 2);
        degree2AnimationID.put(45, 7);
        degree2AnimationID.put(90, 3);
        degree2AnimationID.put(135, 6);
        degree2AnimationID.put(180, 1);
        degree2AnimationID.put(225, 4);
        degree2AnimationID.put(270, 0);
        degree2AnimationID.put(315, 5);

        int bestID = getBestFit(headDirection);
        currentTextureRegion = getWalkFrames(bestID).getKeyFrame(stateTime, true);
    }

    private Animation<TextureRegion> getWalkFrames(int direction) {
//        direction 0: 0, direction 1: 90, direction 2: 180, direction 3: 270
        TextureRegion[] walkFrames = new TextureRegion[frame_cols];
        for (int i = 0; i < frame_cols; i++) {
            walkFrames[i] = allTexture[direction][i];
        }
        Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(ANIMATION_SPEED, walkFrames);
        return walkAnimation;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public int getBestFit(Vector2 direction) {
        float degree = direction.angleDeg(new Vector2(1,0));
        float minDegree = 1000f;
        int bestID = 0;
        for (Integer key : degree2AnimationID.keySet()) {
            float temp = Math.abs(degree - key);
            if (temp < minDegree) {
                minDegree = temp;
                bestID = degree2AnimationID.get(key);
            }
        }
        return bestID;
    }
//    public abstract
    @Override
    public void move(Vector2 direction, float deltaTime) {
        stateTime += deltaTime;
        int bestID = getBestFit(direction);
        currentTextureRegion = getWalkFrames(bestID).getKeyFrame(stateTime, true);;
        super.move(direction, deltaTime);
    }

    public void render() {
        float scaleX = getWidth() / textureWidth;
        float scaleY = getHeight() / textureHeight;
        game.getBatch().draw(currentTextureRegion, getX(), getY(), 0f, 0f, textureWidth, textureHeight, scaleX, scaleY, 0);
    }
}
