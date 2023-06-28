package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface AbilityDrawer {
    public TextureRegion getTextureCoolDown();
    public float getTotalTimeCoolDown();
    public float getCurrentTimeCoolDown();
}
