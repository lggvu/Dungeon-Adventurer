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
    private SpecialGun specialGun;
    public Adventurer() {
        super("adventurer", null);
        specialGun = new SpecialGun();
        specialGun.setOwner(this);
        totalTimeImplement = specialSkillCoolDown / 2;
    }

    private class SpecialGun extends Gun {
        private int numDirectionAttack = 8;
        private int numAttackLeft = numDirectionAttack;
        public SpecialGun() {
            super();
            damage = 2;
            intervalSeconds = 0.2f;
            texture = new TextureRegion(new Texture("weapon/sword.png"));
            bulletTextureRegion = new TextureRegion(new Texture("bullet/bullet4.png"));
            TextureRegion[] explosionFrames = SpriteLoader.loadTextureByFileName("bullet-effects/Shot3/shot3-sheet_1_8.png");
            TextureRegion[] shotExplosionFrames = SpriteLoader.loadTextureByFileName("bullet-effects/Shot3/shot3-sheet_1_8.png");
            this.explosionAnimation = new Animation<TextureRegion>(0.05f, explosionFrames);
            this.shotExplosionAnimation = new Animation<>(0.01f, shotExplosionFrames);
        }
        @Override
        public void update(float deltaTime) {
            if (elapsedSeconds >= intervalSeconds) {
                numAttackLeft = numDirectionAttack;
            }
            super.update(deltaTime);
        }
        @Override
        public void attack(Vector2 direction) {
            super.attack(new Vector2(1, 0));
            super.attack(new Vector2(1, 1));
            super.attack(new Vector2(0, 1));
            super.attack(new Vector2(-1,1));
            super.attack(new Vector2(-1,0));
            super.attack(new Vector2(-1,-1));
            super.attack(new Vector2(0,-1));
            super.attack(new Vector2(1,-1));
            numAttackLeft = 0;
        }
        @Override
        public boolean isAllowedAttack() {
            return numAttackLeft > 0;
        }
        @Override
        public void draw(SpriteBatch batch) {
            for (Bullet bullet : bulletArrayList) {
                bullet.draw(batch);
            }
        }
    }
    @Override
    public void applySpecialSkill(float deltaTime) {
        specialGun.update(deltaTime);
        if (timeImplementLeft > 0) {
            timeImplementLeft -= deltaTime;
            specialGun.attack(null);
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
