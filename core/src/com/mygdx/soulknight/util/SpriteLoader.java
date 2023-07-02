package com.mygdx.soulknight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SpriteLoader {
    private final static String TEXTURE_INFO_PATH = "info/texture_info.json";

    public static TextureInfo[] loadTextureInfo(String sourceImage) {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(TEXTURE_INFO_PATH).reader(), JsonObject.class);
            JsonObject source = json.get(sourceImage).getAsJsonObject();
            int num_col = source.get("num_col").getAsInt();
            float width = source.get("width").getAsFloat();
            float height = source.get("height").getAsFloat();
            Texture texture = new Texture(sourceImage);
            TextureRegion[][] textureRegions = TextureRegion.split(texture,texture.getWidth() / num_col, texture.getHeight());
            TextureInfo[] textureInfos = new TextureInfo[num_col];
            for (int i = 0; i < textureRegions[0].length; i++) {
                textureInfos[i] = new TextureInfo(textureRegions[0][i], width, height);
            }
            return textureInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

        System.out.println(path);
        System.out.println(frameCols);
        System.out.println(frameRows);

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
