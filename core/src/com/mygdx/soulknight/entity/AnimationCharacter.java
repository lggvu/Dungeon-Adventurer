package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.util.ReuseCode;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.HashMap;

public abstract class AnimationCharacter extends Character {
    private static final float ANIMATION_SPEED = 0.1f;
    private float stateTime = 0f;
    private SpriteLoader spriteLoader;
    private TextureRegion currentTextureRegion;
    public AnimationCharacter(SoulKnight game, String texturePath, String characterName) {
        super(game, texturePath);
        spriteLoader = new SpriteLoader(texturePath, characterName);
        currentTextureRegion = spriteLoader.getWalkFrames(headDirection).getKeyFrame(stateTime, true);
    }

    public AnimationCharacter(SoulKnight game, String texturePath, String characterName, int HP, int armor, float runSpeed) {
        this(game, texturePath, characterName);
        this.HP = HP;
        this.armor = armor;
        this.runSpeed = runSpeed;
    }

    @Override
    public void move(Vector2 direction, float deltaTime) {
        stateTime += deltaTime;
        currentTextureRegion = spriteLoader.getWalkFrames(direction).getKeyFrame(stateTime, true);
        super.move(direction, deltaTime);
    }

    public void render() {
        float scaleX = getWidth() / spriteLoader.getImgCharacterWidth();
        float scaleY = getHeight() / spriteLoader.getImgCharacterHeight();
        game.getBatch().draw(currentTextureRegion, getX(), getY(), 0f, 0f, spriteLoader.getImgCharacterWidth(), spriteLoader.getImgCharacterHeight(), scaleX, scaleY, 0);
    }
}
