package com.mygdx.soulknight.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextWrapper {
    private BitmapFont font;
    private float maxWidth;
    private String originText;
    private GlyphLayout layout;
    public TextWrapper(BitmapFont font, float maxWidth, float scale, String originText) {
        this.font = font;
        this.maxWidth = maxWidth;
        this.originText = originText;
        setScale(scale);
    }

    public void setScale(float scale) {
        font.getData().setScale(scale);
        layout = new GlyphLayout(font, getWrapText(originText));
    }

    public String getWrapText(String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        if (layout.width <= maxWidth) {
            return text; // No wrapping needed
        }

        StringBuilder wrappedText = new StringBuilder();
        String[] words = text.split(" ");

        for (String word : words) {
            layout.setText(font, wrappedText + " " + word);
            if (layout.width > maxWidth) {
                wrappedText.append("\n").append(word);
            } else {
                wrappedText.append(" ").append(word);
            }
        }

        return wrappedText.toString().trim();
    }

    public GlyphLayout getLayout() {
        return layout;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        font.draw(batch, layout, x, y);
    }
}
