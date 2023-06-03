package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.screen.MainGameScreen;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;

public abstract class SimpleCharacter {
    protected TextureRegion texture;
    protected SpriteLoader spriteLoader;
    protected int maxHP = 10;
    private float stateTime = 0f;
    protected int currentHP = 10;
    protected float x = 30;
    protected float y = 30;
    protected float width = 32;
    protected float height = 32;
    protected float deltaTime;
    protected float speedRun = 180f;
    protected Vector2 lastMoveDirection = new Vector2(1, 0);
    protected Vector2 currentHeadDirection = new Vector2(1, 0);
    protected int currentWeaponId = 0;
    protected ArrayList<Weapon> weapons = new ArrayList<>();
    protected MainGameScreen gameScreen;
    public SimpleCharacter (MainGameScreen gameScreen) {
        this(gameScreen, "heros/hero-1.png");
    }

    public SimpleCharacter (MainGameScreen gameScreen, String texturePath) {
        this(gameScreen, texturePath, 30, 30);
    }

    public SimpleCharacter (MainGameScreen gameScreen, String texturePath, float x, float y) {
        this(gameScreen, texturePath, x, y, 32, 32);
    }

    public SimpleCharacter (MainGameScreen gameScreen, String texturePath, float x, float y, float width, float height) {
        this(gameScreen, texturePath, x, y, width, height, 7);
    }

    public SimpleCharacter (MainGameScreen gameScreen, String texturePath, float x, float y, float width, float height, int hp) {
        this(gameScreen, texturePath, x, y, width, height, hp, 180f);
    }

    public SimpleCharacter (MainGameScreen gameScreen, String texturePath, float x, float y, float width, float height, int hp, float speedRun) {
        spriteLoader = new SpriteLoader("character/img1.png", "king");
        texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);
//        texture = new TextureRegion(new Texture(texturePath));
        this.gameScreen = gameScreen;
        setPosition(x, y);
        setSize(width, height);
        setHP(hp);
        currentHP = hp;
        setSpeedRun(speedRun);
    }
    public void update(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void move(float x, float y) {
        lastMoveDirection = new Vector2(x, y).nor();
        stateTime += deltaTime;

        float degree = lastMoveDirection.angleDeg(currentHeadDirection);
        if (degree > 90 && degree < 270) {
            currentHeadDirection = new Vector2(currentHeadDirection.x * (-1), 0);
            for (Weapon weapon : weapons) {
                weapon.getTextureRegion().flip(false, true);
            }
//            texture.flip(true, false);
        }

        texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);

        x = lastMoveDirection.x;
        y = lastMoveDirection.y;
        float testX = this.x + x * deltaTime * speedRun;
        float testY = this.y + y * deltaTime * speedRun;
        if (!gameScreen.isMapCollision(new Rectangle(testX, testY, width, height))) {
            this.x = testX;
            this.y = testY;
        }
    }

    public void draw(SpriteBatch batch) {

        batch.draw(texture, x, y, width, height);
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Weapon getCurrentWeapon() {
        if (weapons.size() > 0) {
            return weapons.get(currentWeaponId);
        }
        return null;
    };

    public void setSpeedRun(float speedRun) {
        this.speedRun = speedRun;
    }

    public abstract void getHit(int damage);
    public abstract void attack(Vector2 direction);

    public Vector2 getLastMoveDirection() {
        return lastMoveDirection;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public Vector2 getCurrentHeadDirection() {
        return currentHeadDirection;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }
}
