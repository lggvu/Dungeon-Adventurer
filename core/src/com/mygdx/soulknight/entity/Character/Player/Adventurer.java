package com.mygdx.soulknight.entity.Character.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

public class Adventurer extends Player {
    private Gun specialGun;
    public Adventurer() {
        super("adventurer", null);
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        specialGun = (Gun) Weapon.load(source.get("special_gun").getAsString());
        specialGun.setOwner(this);
        specialGun.setDrawWeapon(false);
        specialSkill = new Skill(
                new TextureRegion(new Texture(textureSpecPath)),
                1f, 1f
        );
        return source;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        specialGun.update(deltaTime);
        if (specialSkill.isInProgresss()) {
            specialGun.attack(getLookAtDirection());
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
