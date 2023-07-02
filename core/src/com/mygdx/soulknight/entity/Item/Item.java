package com.mygdx.soulknight.entity.Item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.Player;

public class Item implements Pickable {
    private enum ItemType {
        HP, MANA
    }
    public enum ItemEnum {
        HP_STONE("item/hp_stone.png", 1, ItemType.HP, 10, 10, true),
        MANA_STONE("item/mana_stone.png", 20, ItemType.MANA, 10, 10, true),
        LIFE_POTION("item/life_potion.png", 2, ItemType.HP, 20, 20, false),
        MANA_POTION("item/mana_potion.png", 40, ItemType.MANA, 20, 20, false);

        private Texture texture;
        private int numHeal;
        private ItemType type;
        private float width, height;
        private boolean autoCollect;
        ItemEnum(String texturePath, int numHeal, ItemType type, float width, float height, boolean autoCollect) {
            texture = new Texture(texturePath);
            this.numHeal = numHeal;
            this.type = type;
            this.width = width;
            this.height = height;
            this.autoCollect = autoCollect;
        }
    }
    private ItemEnum item;
    private float x, y;
    public Item(ItemEnum item, float x, float y) {
        this.item = item;
        this.x = x;
        this.y = y;
    }

    public static Item getRandomItem(float x, float y) {
        int random = MathUtils.random(100);
        Item item = null;
        if (random < 2) {
            item = new Item(Item.ItemEnum.LIFE_POTION, x, y);
        } else if (random < 20) {
            item = new Item(Item.ItemEnum.HP_STONE, x, y);
        } else if (random < 50) {
            item = new Item(Item.ItemEnum.MANA_POTION, x, y);
        } else if (random <= 100) {
            item = new Item(Item.ItemEnum.MANA_STONE, x, y);
        }
        return item;
    }
    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getWidth() {
        return item.width;
    }

    @Override
    public float getHeight() {
        return item.height;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(item.texture, x, y, item.width, item.height);
    }

    public void use(Player player) {
        switch (item.type) {
            case HP:
                int hp = player.getCurrentHP() + item.numHeal;
                if (hp > player.getCurrentMaxHP()) {
                    player.setCurrentHP(player.getCurrentMaxHP());
                } else {
                    player.setCurrentHP(hp);
                }
                return;
            case MANA:
                int mana = player.getCurrentMana() + item.numHeal;
                if (mana > player.getCurrentMaxMana()) {
                    player.setCurrentMana(player.getCurrentMaxMana());
                } else {
                    player.setCurrentMana(mana);
                }
                return;
        }
    }

    public boolean isAutoCollect() {
        return item.autoCollect;
    }

}
