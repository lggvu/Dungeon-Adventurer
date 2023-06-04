package com.mygdx.soulknight.entity.Item;

import com.mygdx.soulknight.entity.Character.Player;

public class ManaPotion extends Item {
    private int numManaHeal;
    public ManaPotion(String texturePath, float width, float height, boolean autoCollect, int numManaHeal) {
        super(texturePath, width, height, autoCollect);
        this.numManaHeal = numManaHeal;
    }

    @Override
    public void use(Player player) {
        int mana = player.getCurrentMana() + numManaHeal;
        if (mana > player.getMaxMana()) {
            player.setCurrentMana(player.getMaxMana());
        } else {
            player.setCurrentMana(mana);
        }
    }
}
