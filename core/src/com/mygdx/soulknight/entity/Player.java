package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.SoulKnight;

import java.util.ArrayList;

public class Player extends AnimationCharacter {

    public Player(SoulKnight game, String texturePath, String characterName) {
        super(game, texturePath, characterName);
    }

    public Player(SoulKnight game, String texturePath, String characterName, int HP, int armor, float runSpeed) {
        super(game, texturePath, characterName, HP, armor, runSpeed);
    }
    public final static ArrayList<Bullet> BULLET_ARRAY_LIST = new ArrayList<Bullet>();

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
        Vector2 position = getHeadDirection();
        float minLength = Integer.MAX_VALUE;
        
        for (Character monster : game.getMonsterList()) {
            float length = new Vector2(monster.getX(), monster.getY()).sub(getX(), getY()).len();
            if (length < minLength) {
                minLength = length;
                position = new Vector2(monster.getX(), monster.getY());
            }
        }
        Vector2 directionAttack = position.sub(getX(), getY()).nor();
        getCurrentWeapon().attack(directionAttack);
    }
}
