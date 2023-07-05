package com.mygdx.soulknight.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class SpriteLoader {
    public static TextureInfo[] loadTextureInfo(String sourceImage) {
        int lastDot = sourceImage.lastIndexOf('.');
        int lastUnderscore = sourceImage.lastIndexOf('_');
        String ySubstring = sourceImage.substring(lastUnderscore + 1, lastDot);
        String d = sourceImage.substring(0, lastUnderscore);
        int secLastUnderScore = d.lastIndexOf("_");
        String xSubstring = d.substring(secLastUnderScore + 1);

        // Parse the x and y values as integers
        int frameRows = Integer.parseInt(xSubstring);
        int frameCols = Integer.parseInt(ySubstring);

        Texture texture = new Texture(sourceImage);
        int frameWidth = texture.getWidth() / frameCols;
        int frameHeight = texture.getHeight() / frameRows;
        TextureRegion[] textureRegions = to1DArray(TextureRegion.split(texture, frameWidth, frameHeight));
        TextureInfo[] textureInfos = new TextureInfo[textureRegions.length];
        for (int i = 0; i < textureRegions.length; i++) {
            textureInfos[i] = new TextureInfo(textureRegions[i], frameWidth, frameHeight);
        }
        return textureInfos;
    }

    public static TextureInfo[] loadTextureInfo(JsonArray jsonArray) {
        Iterator<JsonElement> iterator = jsonArray.iterator();
        ArrayList<TextureInfo> textureInfoArrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            TextureInfo[] temp = loadTextureInfo(iterator.next().getAsString());
            for (TextureInfo textureInfo : temp) {
                textureInfoArrayList.add(textureInfo);
            }
        }
        TextureInfo[] textureInfos = new TextureInfo[textureInfoArrayList.size()];
        for (int i = 0; i < textureInfoArrayList.size(); i++) {
            textureInfos[i] = textureInfoArrayList.get(i);
        }
        return textureInfos;
    }

    public static TextureRegion[][] splitTextureByFileName(String path) {

        int lastDot = path.lastIndexOf('.');
        int lastUnderscore = path.lastIndexOf('_');
        String ySubstring = path.substring(lastUnderscore + 1, lastDot);
        String d = path.substring(0, lastUnderscore);
        int secLastUnderScore = d.lastIndexOf("_");
        String xSubstring = d.substring(secLastUnderScore + 1);

        // Parse the x and y values as integers
        int frameRows = Integer.parseInt(xSubstring);
        int frameCols = Integer.parseInt(ySubstring);

        Texture explosionSheet = new Texture(path);

        int frameWidth = explosionSheet.getWidth() / frameCols;
        int frameHeight = explosionSheet.getHeight() / frameRows;
        TextureRegion[][] temp = TextureRegion.split(explosionSheet, frameWidth, frameHeight);
        return temp;
    }

    public static TextureRegion[] to1DArray(TextureRegion[][] array) {
        TextureRegion[] res = new TextureRegion[array.length * array[0].length];
        int count = 0;
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                res[count] = array[row][col];
                count++;
            }
        }
        return res;
    }

    public static TextureRegion[][] splitTexture(Texture texture, int width, int height, int gapWidth, int gapHeight, int paddingWidth, int paddingHeight, int frame_cols, int frame_rows, int startCol, int startRow) {
        TextureRegion[][] textureRegions = new TextureRegion[frame_rows][frame_cols];

        for (int i = 0; i < frame_rows; i++) {
            for (int j = 0; j < frame_cols; j++) {
                textureRegions[i][j] = new TextureRegion(texture, paddingWidth + (gapWidth + width) * (j + startCol), paddingHeight + (gapHeight + height) * (i + startRow), width, height);
            }
        }
        return textureRegions;
    }
}
