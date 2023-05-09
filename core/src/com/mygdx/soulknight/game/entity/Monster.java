package com.mygdx.soulknight.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.game.SoulKnight;
import com.mygdx.soulknight.game.util.ReuseCode;

import java.util.ArrayList;

public class Monster extends AnimationCharecter {

    public final static ArrayList<Bullet> BULLET_ARRAY_LIST = new ArrayList<Bullet>();
    private Vector2 moveDirection;
    public Monster(Texture texture, SoulKnight game) {
        super(game, texture);
        this.moveDirection = new Vector2(headDirection.x, headDirection.y);
        while (true) {
            int x = MathUtils.random(game.getCollisionLayer().getWidth() - 1);
            int y = MathUtils.random(game.getCollisionLayer().getHeight() - 1);

            if (game.getCollisionLayer().getCell(x, y) == null) {
                Vector2 position = new Vector2(x * game.getCollisionLayer().getTileWidth(), y * game.getCollisionLayer().getTileHeight());
                this.setX(position.x);
                this.setY(position.y);
                break;
            }
        }
        this.mana = Integer.MAX_VALUE;
        this.armor = 0;
    }

    public Monster(Texture texture, SoulKnight game, int HP, int armor, float runSpeed) {
        this(texture, game);
        this.HP = HP;
        this.runSpeed = runSpeed;
        this.mana = Integer.MAX_VALUE;
        this.armor = 0;
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
        float newX, newY;
        while (true) {
            newX = getX() + deltaTime * moveDirection.x * runSpeed;
            newY = getY() + deltaTime * moveDirection.y * runSpeed;
            if (ReuseCode.isMapCollision(game, newX, newY)) {
                int deg = MathUtils.random(-180, 180);
                moveDirection = moveDirection.rotateDeg(deg).nor();
            } else {
                move(moveDirection, deltaTime);
                break;
            }
        }

    }
    @Override
    public void attack() {
        Vector2 currentPlayerPosition = new Vector2(game.getPlayer().getX(), game.getPlayer().getY());
        Vector2 shotDirection = currentPlayerPosition.sub(getX(), getY()).nor();
        getCurrentWeapon().attack(shotDirection);
    }
}
