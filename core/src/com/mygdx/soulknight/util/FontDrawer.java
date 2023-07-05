package com.mygdx.soulknight.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FontDrawer {
    private float timeDrawLeft = 1f;
    private float yMove = 2;
    private BitmapFont font;
    private String text;
    private float x, y;
    public FontDrawer(String text, Color color, float x, float y) {
        font = new BitmapFont(Gdx.files.internal("font/orange.fnt"));
        font.getData().setScale(0.5f);
        font.setColor(color);
        this.text = text;
        this.x = x;
        this.y = y;
    }
    public void update(float deltaTime) {
        timeDrawLeft -= deltaTime;
        y += yMove * deltaTime;
    }

    public boolean isFinished() {
        return timeDrawLeft <= 0;
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, text, x, y);
    }

    public void dispose() {font.dispose();}
}
