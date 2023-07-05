package com.mygdx.soulknight.entity.Character;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Weapon;

public class Monster extends SimpleCharacter {
    protected float attackRadius = 200;
    float speedWhenIdle; // The speed that monster will move when cannot approach the player
    float speedInRangeAttack;
    private Room room;
    public Monster(String characterName, WorldMap map, Room room) {
        super(characterName, map);
        setMaxWeaponNumber(1);
        for (Weapon weapon : weapons) {
            weapon.setDrawWeapon(false);
        }
        this.room = room;
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        speedInRangeAttack = speedRun;
        speedWhenIdle = speedInRangeAttack / 2;
        return source;
    }

    @Override
    public void update(float deltaTime) {
        for (Weapon weapon : weapons) {
            weapon.update(deltaTime);
        }

        if (!isAlive()) {
            stateTime += deltaTime;
            return;
        }

        applyEffect(deltaTime);
        if (isStunned) {
            return;
        }
        stateTime += deltaTime;
        float playerX = map.getPlayer().getX();
        float playerY = map.getPlayer().getY();
        float distance = (float) Math.sqrt(Math.pow(playerX - getX(), 2) + Math.pow(playerY - getY(), 2));
        if (distance <= this.attackRadius && map.getPlayer().isInVision(this)) {
            setSpeedRun(speedInRangeAttack);
            Vector2 direction = new Vector2(playerX - getX(), playerY - getY()).nor();
            if (direction.x != 0 || direction.y != 0) {
                stateTime -= deltaTime;
                move(direction.x, direction.y, deltaTime);
            }
            this.attack(direction);
        }
        else {
            // The monster will move randomly if the player is not in the attack radius
            setSpeedRun(speedWhenIdle);
            float testX = this.getX() + lastMoveDirection.x * speedRun * deltaTime;
            float testY = this.getY() + lastMoveDirection.y * speedRun * deltaTime;
            if (!map.isMapCollision(new Rectangle(testX, testY, getWidth(), getHeight())) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                stateTime -= deltaTime;
                move(lastMoveDirection.x, lastMoveDirection.y, deltaTime);
            } else {
                stateTime -= deltaTime;
                lastMoveDirection = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100)).nor();
            }
        }
    }
    @Override
    public void getHit(int damage, DamageType damageType, boolean isCrit) {
        getMap().addDamageNumber(damage, damageType, isCrit, x, y);
        currentHP -= damage;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        for (Weapon weapon : weapons) {
            weapon.draw(batch);
        }
    }
    public Room getRoom(){
        return this.room;
    }
}
