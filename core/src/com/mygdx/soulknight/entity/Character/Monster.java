package com.mygdx.soulknight.entity.Character;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.SpriteLoader;

public class Monster extends SimpleCharacter {
    protected float attackRadius = 200;
    float speedWhenIdle; // The speed that monster will move when cannot approach the player
    float speedInRangeAttack;
    private Room room;
    public Monster(String characterName, WorldMap map, Room room) {
        super(characterName, map);
        setMaxWeaponNumber(1);
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
        applyEffect(deltaTime);
        if (isStunned) {
            return;
        }
        getCurrentWeapon().update(deltaTime);
        float playerX = map.getPlayer().getX();
        float playerY = map.getPlayer().getY();
        float distance = (float) Math.sqrt(Math.pow(playerX - getX(), 2) + Math.pow(playerY - getY(), 2));
        if (distance <= this.attackRadius && map.getPlayer().isInVision(this)) {
            setSpeedRun(speedInRangeAttack);
            Vector2 direction = new Vector2(playerX - getX(), playerY - getY()).nor();
            if (direction.x != 0 || direction.y != 0) {
                move(direction.x, direction.y, deltaTime);
            }
            this.attack(direction);

        }
        else {
            // The monster will move randomly if the player is not in the attack radius
            setSpeedRun(speedWhenIdle);
            float testX = this.getX() + lastMoveDirection.x * speedRun * deltaTime;
            float testY = this.getY() + lastMoveDirection.y * speedRun * deltaTime;
            if (!map.isMapCollision(new Rectangle(testX, testY, width, height)) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                move(lastMoveDirection.x, lastMoveDirection.y, deltaTime);
            } else {
                lastMoveDirection = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100)).nor();
            }
        }
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
    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        getCurrentWeapon().draw(batch);
    }
    @Override
    public void attack(Vector2 direction) {
        getCurrentWeapon().attack(direction);
    }
    public Room getRoom(){
        return this.room;
    }
}
