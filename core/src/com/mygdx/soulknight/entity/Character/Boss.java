package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

public class Boss extends Monster {
    private float timeShotAgain = 2f;
    private float currentTimeCount = 1f;
    public Boss(String characterName, WorldMap map, Room room) {
        super(characterName, map, room);
        setMaxWeaponNumber(3);
        weapons.clear();
//        gun_1 chase (đạn chậm 100f)
//        gun_2 bắn xung quanh (đạn trung bình 300f)
//        gun_3 đạn nảy (đạn trung bình 300f, nảy 3 lần)
        Gun gun_1 = new Gun("weapon/sword.png", "bullet/bullet4.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", 2, 0, 2f, 1000, 0.3f, 100f);
        gun_1.setDegreeChangePerSec(120f);
        Gun gun_2 = new Gun("weapon/sword.png", "bullet/bullet3.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", 2, 0, 2f, 500, 0.3f, 300f);
        Gun gun_3 = new Gun("weapon/sword.png", "bullet/bullet5.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", 2, 0, 2f, 3000, 0.3f, 300f);
        gun_3.setNumWallCollide(3);
        for (int i = -30; i <= 30; i+=5) {
            gun_1.addDirectionAttack(i);
            gun_2.addDirectionAttack(i);
            gun_3.addDirectionAttack(i);
        }

        for (int i = 90; i <= 150; i+=5) {
            gun_2.addDirectionAttack(i);
        }

        for (int i = -90; i >= -150; i-=5) {
            gun_2.addDirectionAttack(i);
        }
        collectWeapon(gun_1);
        collectWeapon(gun_2);
        collectWeapon(gun_3);
        gun_1.setDrawWeapon(false);
        gun_2.setDrawWeapon(false);
        gun_3.setDrawWeapon(false);
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
        currentTimeCount += deltaTime;

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
            if (currentTimeCount >= timeShotAgain) {
                currentTimeCount = 0;
                this.attack(direction);
                switchWeapon();
                getCurrentWeapon().setDrawWeapon(false);
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
        if (currentTimeCount >= timeShotAgain) {
            currentTimeCount = 0;
            Vector2 lookAtDirection = getLookAtDirection();
            if (lookAtDirection != null && (lookAtDirection.x != 0 || lookAtDirection.y != 0)) {
                this.attack(lookAtDirection);
            } else {
                this.attack(new Vector2(1, 0));
            }
            switchWeapon();
        }
    }
}
