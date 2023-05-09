package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.SoulKnight;

public class Weapon extends Sprite {
    private static int ID = 1;
    private int weaponID;
    private float elapsedSeconds = 1;
    private float intervalSeconds = 0.5f;
    private Character owner;
    private SoulKnight game;

    public Weapon(Texture texture, SoulKnight game) {
//        super(texture);
        weaponID = ID++;
        this.game = game;
    }

    public Weapon(Texture texture, SoulKnight game, float elapsedSeconds, float intervalSeconds) {
        this(texture, game);
        this.elapsedSeconds = elapsedSeconds;
        this.intervalSeconds = intervalSeconds;
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

    public void setOwner(Character owner) {
        this.owner = owner;
    }

    public void update(float deltaTime) {
        this.elapsedSeconds += deltaTime;
    }

//    When we have more weapon, we set attack function to an abstract class
    public void attack(Vector2 direction) {
        if (elapsedSeconds >= intervalSeconds) {
            elapsedSeconds = 0;
            owner.addBullet(new Bullet(new Texture("bullet.png"), game, new Vector2(owner.getX(), owner.getY()), direction));
        }
    }
}
