package com.mygdx.soulknight.entity.Item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.soulknight.entity.Character.Player;

public interface Usable {
    public void use(Player player);
    public void draw(Batch batch);
}
