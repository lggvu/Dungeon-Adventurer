package com.mygdx.soulknight.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.soulknight.SoulKnight;

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

    public static TextureRegion[][] splitTexture(Texture texture, int width, int height, int gapWidth, int gapHeight, int frame_cols, int frame_rows) {
        TextureRegion[][] textureRegions = new TextureRegion[frame_rows][frame_cols];
        for (int i = 0; i < frame_rows; i++) {
            for (int j = 0; j < frame_cols; j++) {
                textureRegions[i][j] = new TextureRegion(texture, (gapWidth + width) * j, (gapHeight + height) * i, width, height);
            }
        }
        return textureRegions;
    }
}
