package com.mygdx.soulknight.entity.Character.Monster;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Weapon;

public class Boss extends Monster {
    public Boss(String characterName, WorldMap map, Room room) {
        super(characterName, map, room);
        attackSkill.setTotalTimeCoolDown(2f);
        attackSkill.setCurrentTimeCoolDown(1f);
    }

    @Override
    public void update(float deltaTime) {
        for (Weapon weapon : weapons) {
            weapon.update(deltaTime);
        }
        attackSkill.update(deltaTime);

        if (!isAlive()) {
            stateTime += deltaTime;
            return;
        }

        if (attackSkill.isInProgresss()) {
            return;
        }

        applyEffect(deltaTime);

        if (isStunned) {
            return;
        }

        float playerX = map.getPlayer().getX();
        float playerY = map.getPlayer().getY();
        float distance = (float) Math.sqrt(Math.pow(playerX - getX(), 2) + Math.pow(playerY - getY(), 2));
        if (distance <= this.attackRadius && map.getPlayer().isInVision(this)) {
            setSpeedRun(speedInRangeAttack);
            Vector2 direction = new Vector2(playerX - getX(), playerY - getY()).nor();
            if (direction.x != 0 || direction.y != 0) {
                move(direction.x, direction.y, deltaTime);
                setLookAtDirection(lastMoveDirection.x, lastMoveDirection.y);
                updateMovementAnimation(deltaTime);
            }
        }
        else {
            // The monster will move randomly if the player is not in the attack radius
            setSpeedRun(speedWhenIdle);
            float testX = this.getX() + lastMoveDirection.x * speedRun * deltaTime;
            float testY = this.getY() + lastMoveDirection.y * speedRun * deltaTime;
            if (!map.isMapCollision(new Rectangle(testX, testY, getWidth(), getHeight())) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                move(lastMoveDirection.x, lastMoveDirection.y, deltaTime);
                setLookAtDirection(lastMoveDirection.x, lastMoveDirection.y);
                updateMovementAnimation(deltaTime);
            } else {
                float x = MathUtils.random(-100, 100);
                float y = MathUtils.random(-100, 100);
                if (x != 0 || y != 0) {
                    lastMoveDirection = new Vector2(x, y).nor();
                }
            }
        }
        if (!attackSkill.isInProgresss() && !attackSkill.isCoolingDown()) {
            Vector2 lookAtDirection = getLookAtDirection();
            if (lookAtDirection != null && (lookAtDirection.x != 0 || lookAtDirection.y != 0)) {
                attackSkill.setAttackDirection(lookAtDirection);
            } else {
                attackSkill.setAttackDirection(new Vector2(1, 0));
            }
            attackSkill.activateSkill();
            attackSkill.setTotalTimeCoolDown(getCurrentWeapon().getTotalTimeCoolDown());
            switchWeapon();
        }
    }
}
