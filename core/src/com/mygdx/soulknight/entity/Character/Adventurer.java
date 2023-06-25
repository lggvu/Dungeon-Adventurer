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
    private float specialSkillTime = specialSkillCoolDown / 2;
    private float timeSpecialSkillLeft = 0;
    private SpecialGun specialGun;
    public Adventurer() {
        super("adventurer", null);
        specialGun = new SpecialGun();
        specialGun.setOwner(this);
    }

    @Override
    public void activateSpecialSkill() {
        super.activateSpecialSkill();
        timeSpecialSkillLeft = specialSkillTime;
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
        if (timeSpecialSkillLeft > 0) {
            timeSpecialSkillLeft -= deltaTime;
            specialGun.attack(null);
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
