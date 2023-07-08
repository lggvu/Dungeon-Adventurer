package com.mygdx.soulknight.entity.Character.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Kunai;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

public class Jungler extends Player {
    private Animation<TextureInfo> immortalAnimation;

    public Jungler() {
        super("jungler", null);
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        immortalAnimation = new Animation<>(0.15f, SpriteLoader.loadTextureInfo(source.get("immortal_texture_path").getAsJsonArray()));
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        specialSkill = new Skill(new TextureRegion(new Texture(textureSpecPath)), 1f, 1f) {
            @Override
            public void activateSkill() {
                if (!getDodgeSkill().isInProgresss()) {
                    super.activateSkill();
                    Animation<TextureInfo> temp = animationMovement;
                    animationMovement = immortalAnimation;
                    immortalAnimation = temp;
                }
            }
            @Override
            public void deactivateSkill() {
                super.deactivateSkill();
                Animation<TextureInfo> temp = animationMovement;
                animationMovement = immortalAnimation;
                immortalAnimation = temp;
            }
        };
        return source;
    }

    @Override
    public void getHit(int damage, DamageType damageType, boolean isCrit) {
        if (!specialSkill.isInProgresss()) {
            super.getHit(damage, damageType, isCrit);
        }
    }
}