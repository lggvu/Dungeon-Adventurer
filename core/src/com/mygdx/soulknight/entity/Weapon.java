package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.SoulKnight;

import java.util.ArrayList;

public abstract class Weapon {
//    public final static ArrayList<Bullet> BULLET_ARRAY_LIST = new ArrayList<Bullet>();

    private static int ID = 1;
    private int weaponID;
    protected float elapsedSeconds = 1;
    protected float intervalSeconds = 0.5f;
    protected SimpleCharacter owner;
    protected float rangeWeapon = 1000f;
    protected Texture texture;
    public Weapon(SimpleCharacter owner) {
        this(owner, "weapon/weapon.png");
    }

    public Weapon(SimpleCharacter owner, String texturePath) {
        this(owner, texturePath, 0.5f);
    }

    public Weapon(SimpleCharacter owner, String texturePath, float intervalSeconds) {
        this.texture = new Texture(texturePath);
        this.intervalSeconds = intervalSeconds;
        this.elapsedSeconds = intervalSeconds;
        this.owner = owner;
        weaponID = ID++;
    }

    public void reset() {
        elapsedSeconds = intervalSeconds + 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Weapon) {
            return this.weaponID == ((Weapon) obj).weaponID;
        }
        return false;
    }

    public void setOwner(SimpleCharacter owner) {
        this.owner = owner;
    }

    public void update(float deltaTime) {
        this.elapsedSeconds += deltaTime;
    }

    //    When we have more weapon, we set attack function to an abstract class
    public abstract void attack(Vector2 direction);
    public abstract void draw(SpriteBatch batch);

    protected boolean isAllowedAttack() {
        if (elapsedSeconds >= intervalSeconds) {
            elapsedSeconds = 0;
            return true;
        }
        return false;
    }

    public float getRangeWeapon() {
        return rangeWeapon;
    }
}