package com.mygdx.soulknight.entity.Item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Player;


public class LifePotion implements Usable {
    private boolean autoCollect = false;
    private float x, y, width, height;
    private Texture texture;
    private int numHPHeal;
    public LifePotion(String texturePath, int numHPHeal, boolean autoCollect, float width, float height) {
        this.width = width;
        this.height = height;
        this.texture = new Texture(texturePath);
        this.numHPHeal = numHPHeal;
        this.autoCollect = autoCollect;
    }

    @Override
    public void use(Player player) {
        int hp = player.getCurrentHP() + numHPHeal;
        if (hp > player.getMaxHP()) {
            player.setCurrentHP(player.getMaxHP());
        } else {
            player.setCurrentHP(hp);
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public void draw(Batch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
}
