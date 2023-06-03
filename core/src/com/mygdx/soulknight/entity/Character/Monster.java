package com.mygdx.soulknight.entity.Character;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.screen.MainGameScreen;

public class Monster extends SimpleCharacter {
    float attackRadius = 200;
    float speedWhenIdle = this.speedRun/10; // The speed that monster will move when cannot approach the player

    public Monster(MainGameScreen gameScreen) {
        super(gameScreen, "heros/monster-1.png");
        setSpeedRun(20f);
    }

    public float getAttackRadius() {
        return attackRadius;
    }

    public void setAttackRadius(float attackRadius) {
        this.attackRadius = attackRadius;
    }

    @Override
    public void getHit(int damage) {
        currentHP -= damage;
    }

//    @Override
//    public void update(float deltaTime) {
//        super.update(deltaTime);
//        while (true) {
//            float testX = getX() + lastMoveDirection.x * speedRun * deltaTime;
//            float testY = getY() + lastMoveDirection.y * speedRun * deltaTime;
//            if (!gameScreen.isMapCollision(new Rectangle(testX, testY, width, height)) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
//                move(lastMoveDirection.x, lastMoveDirection.y);
//                break;
//            }
//            lastMoveDirection = new Vector2(MathUtils.random(-10, 10), MathUtils.random(-10, 10)).nor();
//        }
//    }
    public void update(float deltaTime, float playerX, float playerY) {
        // The monster will chase the player if the player is in the attack radius
        super.update(deltaTime);
        getCurrentWeapon().update(deltaTime);
        float distance = (float) Math.sqrt(Math.pow(playerX - getX(), 2) + Math.pow(playerY - getY(), 2));
        if (distance <= this.attackRadius) {
            Vector2 direction = new Vector2(playerX - getX(), playerY - getY()).nor();
            if (direction.x != 0 || direction.y != 0) {
                move(direction.x, direction.y);
            }
            this.attack(direction);

        }
        else {
            // The monster will move randomly if the player is not in the attack radius
            while (true) {
                float testX = this.getX() + lastMoveDirection.x * speedRun * deltaTime;
                float testY = this.getY() + lastMoveDirection.y * speedRun * deltaTime;
                if (!gameScreen.isMapCollision(new Rectangle(testX, testY, width, height)) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                    move(lastMoveDirection.x, lastMoveDirection.y);
                    break;
                }
                this.setSpeedRun(this.speedWhenIdle);
                lastMoveDirection = new Vector2(MathUtils.random(-10, 10), MathUtils.random(-10, 10)).nor();

            }
        }

    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        getCurrentWeapon().draw(batch);
    }
    @Override
    public void attack(Vector2 direction) {
        getCurrentWeapon().attack(direction);
    }

}
