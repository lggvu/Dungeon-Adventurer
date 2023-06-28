package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.util.SpriteLoader;

public class Adventurer extends Player {
    private Gun specialGun;
    public Adventurer() {
        super("adventurer", null);
        specialGun = new Gun("weapon/sword.png", "bullet/bullet4.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", "bullet-effects/Shot3/shot3-sheet_1_8.png", 2, 0, 0.2f, 500, 0.3f, 500f);
        specialGun.addDirectionAttack(45, 90, 135, 180, 235, 270, 315);
        specialGun.setOwner(this);
        specialGun.setDrawGun(false);
        specialSkillCoolDown = 1f;
        totalTimeImplement = specialSkillCoolDown * 5;
    }
    @Override
    public void applySpecialSkill(float deltaTime) {
        specialGun.update(deltaTime);
        if (timeImplementLeft > 0) {
            timeImplementLeft -= deltaTime;
            specialGun.attack(lastMoveDirection);
            if (timeImplementLeft <= 0) {
                isImplement = false;
                isCoolingDown = true;
                coolDownTimer = specialSkillCoolDown;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (Bullet bullet : specialGun.getBulletArrayList()) {
            bullet.draw(batch);
        }
        super.draw(batch);
    }
}
