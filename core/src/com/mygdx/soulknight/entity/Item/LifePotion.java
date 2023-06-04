package com.mygdx.soulknight.entity.Item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Player;


public class LifePotion extends Item {
    private int numHPHeal;

    public LifePotion(String texturePath, float width, float height, boolean autoCollect, int numHPHeal) {
        super(texturePath, width, height, autoCollect);
        this.numHPHeal = numHPHeal;
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

}
