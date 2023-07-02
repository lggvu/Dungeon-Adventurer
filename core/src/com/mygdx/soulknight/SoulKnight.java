package com.mygdx.soulknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.soulknight.screen.MenuScreen;

public class SoulKnight extends Game {

    private SpriteBatch batch;

    @Override
    public void create () {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        batch = new SpriteBatch();
        String a = "abcd/12_3_4.5_5.13.png";
        int b = a.lastIndexOf('.');
        int c = a.lastIndexOf('_');
        System.out.println(a.substring(c + 1, b));
        String d = a.substring(0, c);
        int e = d.lastIndexOf("_");
        System.out.println(d.substring(e + 1));
        System.out.println("In SoulKnight--------------------");
        this.setScreen(new MenuScreen(this));
    }
    @Override
    public void render () {
        super.render();
    }
    @Override
    public void dispose () {
        batch.dispose();
    }
    public SpriteBatch getBatch() {
        return batch;
    }
    public void resetBatch() {
        batch.dispose();
        batch = new SpriteBatch();
    }
}
