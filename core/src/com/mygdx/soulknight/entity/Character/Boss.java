package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
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
        applyEffect(deltaTime);
        currentTimeCount += deltaTime;
        for (Weapon weapon : weapons) {
            weapon.update(deltaTime);
        }
        if (isStunned || !isAlive()) {
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
            if (!map.isMapCollision(new Rectangle(testX, testY, width, height)) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                move(lastMoveDirection.x, lastMoveDirection.y, deltaTime);
            } else {
                lastMoveDirection = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100)).nor();
            }
        }
        if (currentTimeCount >= timeShotAgain) {
            currentTimeCount = 0;
            if (lastMoveDirection != null && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                this.attack(lastMoveDirection);
            } else {
                this.attack(new Vector2(1, 0));
            }
            switchWeapon();
        }
    }
}
