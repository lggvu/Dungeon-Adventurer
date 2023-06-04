package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;


public class DestroyableObject {
    private TiledMapTileMapObject object;
    private float width = 16;
    private float height = 16;
    private static int count = 0;
    private int id;
    private Rectangle rectangle;
    public DestroyableObject(TiledMapTileMapObject object) {
        this.object = object;
        id = count++;
        rectangle = new Rectangle(object.getX(), object.getY(), width, height);
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
        batch.draw(object.getTextureRegion(), object.getX(), object.getY(), width, height);
    }
}
