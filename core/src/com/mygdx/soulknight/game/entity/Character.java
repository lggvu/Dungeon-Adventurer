package com.mygdx.soulknight.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.game.SoulKnight;
import com.mygdx.soulknight.game.util.ReuseCode;

import java.util.ArrayList;

public abstract class Character extends Sprite {
    protected int armor = 100;
    protected int HP = 100;
    protected int mana = 200;
    protected SoulKnight game;
    private Vector2 direction = new Vector2(1,0);
    private float runSpeed = 180f;
    private ArrayList<Weapon> weapons;
    private int currentWeapon = 0;

    public Character(Texture texture, SoulKnight game) {
        super(texture);
        this.weapons = new ArrayList<Weapon>();
        this.game = game;
        setPosition(60,60);
    }

    public Character(Texture texture, SoulKnight game, int HP, int armor, float runSpeed) {
        this(texture, game);
        this.HP = HP;
        this.armor = armor;
        this.runSpeed = runSpeed;
    }

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
        weapon.setOwner(this);
    }

    public void removeWeapon(Weapon weapon) {
        for (Weapon weapon1 : weapons) {
            if (weapon1.equals(weapon)) {
                weapon1.setOwner(null);
                weapon1.reset();
            }
        }
        weapons.remove(weapon);
    }

    public Weapon getCurrentWeapon() {
        if (this.weapons.size() > 0) {
            return this.weapons.get(currentWeapon);
        }
        return null;
    }

    public void switchWeapon() {
        getCurrentWeapon().reset();
        currentWeapon += 1;
        if (currentWeapon >= weapons.size()) {
            currentWeapon = 0;
        }
    }

    public void update(float deltaTime) {
        getCurrentWeapon().update(deltaTime);
    }
    public void move(Vector2 direction, float deltaTime) {
        float newX = getX() + direction.x * deltaTime * runSpeed;
        float newY = getY() + direction.y * deltaTime * runSpeed;
        if (direction.x < 0) {
            this.direction = new Vector2(-1, 0);
        } else if (direction.x > 0) {
            this.direction = new Vector2(1, 0);
        }
        if (!ReuseCode.isMapCollision(game, newX, newY)) {
            setPosition(newX, newY);
        }
    }

    public void getHit(int dmg) {
        if (this.armor > dmg) {
            this.armor -= dmg;
        }
        else {
            this.armor = 0;
            this.HP  -= (dmg - this.armor);
        }
    }

    public void render() {
        float scaleX = getWidth() / getTexture().getWidth();
        float scaleY = getHeight() / getTexture().getHeight();
        game.getBatch().draw(getTexture(), getX(), getY(), 0, 0, getTexture().getWidth(), getTexture().getHeight(), scaleX, scaleY, 0, 0, 0, getTexture().getWidth(), getTexture().getHeight(), false, false);
    }

    public Rectangle getBoundingRectangle() {
        return new Rectangle(this.getX(), getY(), getWidth(), getHeight());
    }
    public abstract void attack();

    public Vector2 getDirection() {
        return direction;
    }
}
