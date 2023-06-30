package com.mygdx.soulknight.entity.Character;

import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

public class Assassin extends Player {
    private SpriteLoader immortalSpriteLoader;
    public Assassin() {
        super("assassin", null);
        totalTimeImplement = specialSkillCoolDown / 2;
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        immortalSpriteLoader = new SpriteLoader(source.get("immortal_texture_path").getAsString(), characterName);
        return source;
    }

    @Override
    public void activateSpecialSkill() {
        super.activateSpecialSkill();
        SpriteLoader temp = spriteLoader;
        spriteLoader = immortalSpriteLoader;
        immortalSpriteLoader = temp;
    }

    @Override
    public void applySpecialSkill(float deltaTime) {
        if (timeImplementLeft > 0) {
            timeImplementLeft -= deltaTime;
            if (timeImplementLeft <= 0) {
                SpriteLoader temp = spriteLoader;
                spriteLoader = immortalSpriteLoader;
                immortalSpriteLoader = temp;
                isImplement = false;
                isCoolingDown = true;
                coolDownTimer = specialSkillCoolDown;
            }
        }
    }

    @Override
    public void getHit(int damage, DamageType damageType) {
        if (timeImplementLeft <= 0) {
            super.getHit(damage, damageType);
        }
    }
}
