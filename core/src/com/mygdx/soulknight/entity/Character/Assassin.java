package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

public class Assassin extends Player {
    private Animation<TextureInfo> immortalAnimation;
    public Assassin() {
        super("assassin", null);
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
