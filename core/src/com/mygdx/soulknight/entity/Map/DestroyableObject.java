package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.GameElement;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.entity.Weapon.Weapon;


public class DestroyableObject{
    private TextureRegion region;
    private float x, y;
    private float width = 16;
    private float height = 16;
    private static int count = 0;
    private int id;
    private Rectangle rectangle;
    private Pickable pickableObject;
    private String name;

    public static void resetID() {
        count = 0;
    }

    public DestroyableObject(TiledMapTileMapObject object) {
        region = object.getTextureRegion();
        name = object.getName();
        if (name == null) {
            name = "";
        }
        name = name.toUpperCase();
        x = object.getX();
        y = object.getY();
        id = count++;
        rectangle = new Rectangle(object.getX(), object.getY(), width, height);
        if (name.equals("ARK")) {
            pickableObject = Item.getRandomItem(getX(), getY());
        } else if (name.equals("ARK_WEAPON")) {
            String[] weaponsName = new String[] {"Gun Red M", "Gun Red L", "Gun Red XL", "Gun Venom S", "Gun Venom M", "Gun Venom L", "Shot Gun Base", "Shot Gun Pro"};
            int randomIndex = (int) (Math.random() * weaponsName.length);
            Weapon weapon = Weapon.load(weaponsName[randomIndex]);
            weapon.setPosition(getX(), getY());
            weapon.setOnGround(true);
            pickableObject = weapon;
        }
    }

    public Pickable getPickableObject() {
        return pickableObject;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DestroyableObject) {
            return this.id == ((DestroyableObject) object).id;
        }
        return false;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(region, x, y, width, height);
    }

    public int getId() {
        return id;
    }
}
