package com.mygdx.soulknight.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureInfo {
    private TextureRegion textureRegion;
    private float width;
    private float height;

    public TextureInfo(String path) {
        int lastDot = path.lastIndexOf('.');
        int lastUnderscore = path.lastIndexOf('_');
        String ySubstring = path.substring(lastUnderscore + 1, lastDot);
        String d = path.substring(0, lastUnderscore);
        int secLastUnderScore = d.lastIndexOf("_");
        String xSubstring = d.substring(secLastUnderScore + 1);
        // Parse the x and y values as integers
        width = Integer.parseInt(xSubstring);
        height = Integer.parseInt(ySubstring);
        textureRegion = new TextureRegion(new Texture(path));
    }

    public TextureInfo(TextureRegion textureRegion, float width, float height) {
        this.textureRegion = textureRegion;
        this.width = width;
        this.height = height;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
