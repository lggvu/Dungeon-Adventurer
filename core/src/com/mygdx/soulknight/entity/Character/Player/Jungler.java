package com.mygdx.soulknight.entity.Character.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

public class Jungler extends Player {

    public Jungler() {
        super("jungler", null);
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        specialSkill = new Skill(new TextureRegion(new Texture(textureSpecPath)), 3f, 5f) {
            @Override
            public void activateSkill() {
                super.activateSkill();
                setDefaultMaxArmor(getDefaultMaxArmor() * 2);
                setDefaultMaxHP(getDefaultMaxHP() * 2);
                setCurrentArmor(getCurrentArmor() * 2);
                setCurrentHP(getCurrentHP() * 2);
            }
            @Override
            public void deactivateSkill() {
                super.deactivateSkill();
                setDefaultMaxArmor(getDefaultMaxArmor() / 2);
                setDefaultMaxHP(getDefaultMaxHP() / 2);
                setCurrentArmor(getCurrentArmor() > getCurrentMaxArmor() ? getCurrentMaxArmor() : getCurrentArmor());
                setCurrentHP(getCurrentHP() > getCurrentMaxHP() ? getCurrentMaxHP() : getCurrentHP());
            }
        };
        return source;
    }
}