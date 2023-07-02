package com.mygdx.soulknight.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureInfo {
    private TextureRegion textureRegion;
    private float width;
    private float height;

    public TextureInfo(String path) {
        int underscoreIndex1 = path.indexOf('_');
        int underscoreIndex2 = path.lastIndexOf('_');
        // Extract the x and y substrings
        String xSubstring = path.substring(underscoreIndex1 + 1, underscoreIndex2);
        String ySubstring = path.substring(underscoreIndex2 + 1, path.lastIndexOf('.'));
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
