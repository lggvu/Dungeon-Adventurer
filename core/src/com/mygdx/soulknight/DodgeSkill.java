package com.mygdx.soulknight;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.PlayerSkill;

public class DodgeSkill extends PlayerSkill {
    public DodgeSkill(TextureRegion textureRegion, float totalTimeCoolDown, float totalTimeImplement) {
        super(textureRegion, totalTimeCoolDown, totalTimeImplement);
    }
}
