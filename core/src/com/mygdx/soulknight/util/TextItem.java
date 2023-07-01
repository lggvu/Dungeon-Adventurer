package com.mygdx.soulknight.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;

public class TextItem {
    private String text;
    private Vector2 position;
    private BitmapFont normalFont;
    private BitmapFont hoverFont;
    private boolean selected = false;
    private boolean hovered;

    public TextItem(String text, Vector2 position, BitmapFont normalFont, BitmapFont hoverFont ){
        this.text = text;
        this.position = position;
        this.normalFont = normalFont;
        this.hoverFont = hoverFont;
    }

    public BitmapFont getFont() {
        if (selected || hovered) {
            return this.hoverFont;
        }
        else{
            return this.normalFont;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public String getText() {
        return this.text;
    }
    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public boolean isSelected(){
        return this.selected;
    }
    public void setHovered(boolean hovered){
        this.hovered = hovered;
    }
    public boolean isHovered(){
        return this.hovered;
    }
    public GlyphLayout getLayout(){
        GlyphLayout layout = new GlyphLayout();
        layout.setText(this.getFont(), text);
        return layout;
    }

    public void dispose() {
        normalFont.dispose();
        hoverFont.dispose();
    }

}
