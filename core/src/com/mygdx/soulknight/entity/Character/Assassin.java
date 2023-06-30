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
        specialSkill = new PlayerSkill(
                new TextureRegion(new Texture(textureSpecPath), 1, 388, 256, 256),
                1f, 1f);
        return source;
    }

    private class SpecialSkill extends PlayerSkill {

        public SpecialSkill(TextureRegion textureRegion, float totalTimeCoolDown, float totalTimeImplement) {
            super(textureRegion, totalTimeCoolDown, totalTimeImplement);
        }

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
    }

    @Override
    public void getHit(int damage, DamageType damageType) {
        if (specialSkill.isInProgresss()) {
            super.getHit(damage, damageType);
        }
    }
}
