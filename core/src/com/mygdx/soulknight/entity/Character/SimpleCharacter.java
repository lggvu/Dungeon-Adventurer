package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.screen.MainGameScreen;
import com.mygdx.soulknight.util.SpriteLoader;


import java.util.ArrayList;

public abstract class SimpleCharacter {

    public static final String CHARACTER_INFO_PATH = "info/character_info.json";
    protected TextureRegion texture;
    protected WorldMap map;
    protected Room room;
    protected String characterName;
    protected SpriteLoader spriteLoader;
    protected int maxHP = 10;
    protected float stateTime = 0f;
    protected int currentHP = 10;
    protected float x = 30;
    protected float y = 30;
    protected float width = 32;
    protected float height = 32;
    protected float speedRun = 180f;
    protected Vector2 lastMoveDirection = new Vector2(1, 0);
    protected Vector2 currentHeadDirection = new Vector2(1, 0);
    protected int currentWeaponId = 0;
    protected ArrayList<Weapon> weapons = new ArrayList<>();


    protected ArrayList<Bullet> hitBulletArrayList=new ArrayList<>();

    public SimpleCharacter(String characterName, WorldMap map) {
        this.characterName = characterName;
        this.map = map;
//        spriteLoader = new SpriteLoader("character/img1.png", "king");
//        texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);
    }

    public abstract void load();

    public abstract Room getRoom();

    public abstract void update(float deltaTime);

    public void setMap(WorldMap map) {
        this.map = map;
    }

    public void move(float x, float y, float deltaTime) {
        lastMoveDirection = new Vector2(x, y).nor();
        stateTime += deltaTime;

        float degree = lastMoveDirection.angleDeg(currentHeadDirection);
        if (degree > 90 && degree < 270) {
            currentHeadDirection = new Vector2(currentHeadDirection.x * (-1), 0);
            for (Weapon weapon : weapons) {
                weapon.flip(false, true);
            }
        }

        texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);

        x = lastMoveDirection.x;
        y = lastMoveDirection.y;
        float testX = this.x + x * deltaTime * speedRun;
        float testY = this.y + y * deltaTime * speedRun;
        if (!map.isMapCollision(new Rectangle(testX, testY, width, height))) {
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

    public WorldMap getMap() {
        return map;
    }

    public ArrayList<SimpleCharacter> getEnemyList() {
        ArrayList<SimpleCharacter> listEnemy = new ArrayList<>();
        if (this instanceof Player) {
            Room room = map.getRoomPlayerIn();
            if (room != null) {
                for (SimpleCharacter monster : room.getMonsterAlive()) {
                    listEnemy.add(monster);
                }
            }
        } else if (this instanceof Monster) {
            listEnemy.add((SimpleCharacter) map.getPlayer());
        }
        return listEnemy;
    }

    public abstract void getHit(int damage, Vector2 direction, Bullet bullet);
}
