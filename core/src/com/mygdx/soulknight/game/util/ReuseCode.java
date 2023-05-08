package com.mygdx.soulknight.game.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.soulknight.game.SoulKnight;

public class ReuseCode {
    public static boolean isMapCollision(SoulKnight game, float x, float y) {
//        Trả về true nghĩa là tọa độ này gây ra collision với map, còn không là false
        TiledMapTileLayer collisionLayer = game.getCollisionLayer();
        float overlapW = collisionLayer.getTileWidth()/12f;
        float overlapH = collisionLayer.getTileHeight()/12f;

        int righttileX = (int) Math.ceil((x-overlapW) / collisionLayer.getTileWidth());
        int lefttileX = (int) Math.floor((x+overlapW) / collisionLayer.getTileWidth());
        int toptileY = (int) Math.ceil((y-overlapH) / collisionLayer.getTileHeight());
        int bottomtileY = (int) Math.floor((y+overlapH) / collisionLayer.getTileHeight());

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(lefttileX, toptileY);
        TiledMapTileLayer.Cell cell1 = collisionLayer.getCell(lefttileX, bottomtileY);
        TiledMapTileLayer.Cell cell2 = collisionLayer.getCell(righttileX, toptileY);
        TiledMapTileLayer.Cell cell3 = collisionLayer.getCell(righttileX, bottomtileY);

        if (cell == null & cell1 == null & cell2 == null & cell3 == null) {
            return false;
        }

        return true;
    }
}
