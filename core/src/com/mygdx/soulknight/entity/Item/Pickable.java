package com.mygdx.soulknight.entity.Item;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Pickable {
    public float getX();
    public float getY();
    public float getWidth();
    public float getHeight();

    public void draw(SpriteBatch batch);

    public String getTypeString();
}
