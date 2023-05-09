package com.mygdx.soulknight.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.soulknight.game.SoulKnight;

import java.util.ArrayList;

public class Player extends Character {
    public final static ArrayList<Bullet> BULLET_ARRAY_LIST = new ArrayList<Bullet>();
    public Player(Texture texture, SoulKnight game) {
        super(texture, game);
    }

    public Player(Texture texture, SoulKnight game, int HP, int armor, float runSpeed) {
        super(texture, game, HP, armor, runSpeed);
    }
    public void addBullet(Bullet bullet) {
        BULLET_ARRAY_LIST.add(bullet);
    }

    public void removeAllBullet(ArrayList<Bullet> bulletsRemove) {
        BULLET_ARRAY_LIST.removeAll(bulletsRemove);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void attack() {
        getCurrentWeapon().attack(getHeadDirection());
    }
}
