package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.PlayerSkill;
import com.mygdx.soulknight.util.SpriteLoader;

public class Assassin extends Player {
    private SpriteLoader immortalSpriteLoader;
    public Assassin() {
        super("assassin", null);
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        immortalSpriteLoader = new SpriteLoader(source.get("immortal_texture_path").getAsString(), characterName);
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        specialSkill = new PlayerSkill(new TextureRegion(new Texture("buff/Dodge.png")), 1f, 1f) {
            @Override
            public void activateSkill() {
                super.activateSkill();
                SpriteLoader temp = spriteLoader;
                spriteLoader = immortalSpriteLoader;
                immortalSpriteLoader = temp;
            }
            @Override
            public void deactivateSkill() {
                super.deactivateSkill();
                SpriteLoader temp = spriteLoader;
                spriteLoader = immortalSpriteLoader;
                immortalSpriteLoader = temp;
            }
        };
        return source;
    }

    @Override
    public void getHit(int damage, DamageType damageType) {
        if (specialSkill.isInProgresss()) {
            super.getHit(damage, damageType);
        }
    }
}
