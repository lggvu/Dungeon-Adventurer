package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.WorldMap;

import java.util.ArrayList;

public abstract class RegionEffect extends Effect {
    public abstract void update(float deltaTime, WorldMap map);
    public abstract void draw(SpriteBatch batch);
    public static void loadRegionEffect(SimpleCharacter owner, WorldMap map, ArrayList<String> effectsName, float x, float y) {
        ArrayList<RegionEffect> effects = new ArrayList<>();
        if (effectsName.contains("explosion")) {
            effects.add(new Explosion(owner, x, y, 60, true));
        }
        map.addRegionEffect(effects);
    }
}
