package com.mygdx.soulknight.entity.Item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.Player;

public abstract class Item implements Pickable {
    protected float x, y, width, height;
    protected boolean autoCollect;
    protected Texture texture;
    public Item(String texturePath, float width, float height, boolean autoCollect) {
        texture = new Texture(texturePath);
        this.width = width;
        this.height = height;
        this.autoCollect = autoCollect;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract void use(Player player);

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }


    public static Item load(String itemName) {
        return load(itemName, "info/item_info.json");
    }

    public static Item load(String itemName, String infoPath) {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(infoPath).reader(), JsonObject.class);
            JsonObject source = json.get(itemName).getAsJsonObject();
            String texturePath = source.get("texture_path").getAsString();
            float width = source.get("width").getAsFloat();
            float height = source.get("height").getAsFloat();
            boolean autoCollect = source.get("auto_collect").getAsBoolean();
            String type = source.get("type").getAsString();
            if (type.equals("Life Potion")) {
                int hp = source.get("hp").getAsInt();
                LifePotion item = new LifePotion(texturePath, width, height, autoCollect, hp);
                return item;
            } else if (type.equals("Mana Potion")) {
                int mana = source.get("mana").getAsInt();
                ManaPotion item = new ManaPotion(texturePath, width, height, autoCollect, mana);
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public float getX() {
        return x;
    };
    public float getY() {
        return y;
    };
    public float getWidth() {
        return width;
    };
    public float getHeight() {
        return height;
    };

    public boolean isAutoCollect() {
        return autoCollect;
    }
}
