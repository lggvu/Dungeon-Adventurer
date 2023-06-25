package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.soulknight.entity.Map.WorldMap;

public abstract class RegionEffect extends Effect {
    public abstract void update(float deltaTime, WorldMap map);
    public abstract void draw(SpriteBatch batch);
}
