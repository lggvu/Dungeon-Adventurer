package com.mygdx.soulknight.util;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SpriteLoader {
    private final static String TEXTURE_INFO_PATH = "info/character_sprite_sheet_info.json";
    private int imgCharacterWidth, imgCharacterHeight;
    private int frameCols, frameRows;
    private HashMap<Integer, Integer> degree2AnimationID;
    private TextureRegion[][] textureRegions;
    public static final float ANIMATION_SPEED = 0.15f;
    public SpriteLoader(String sourceImage, String characterName) {
        this.degree2AnimationID = new HashMap<>();
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(TEXTURE_INFO_PATH).reader(), JsonObject.class);
            JsonObject source = json.get(sourceImage).getAsJsonObject();
            int gapWidth = source.get("gapWidth").getAsInt();
            int gapHeight = source.get("gapHeight").getAsInt();
            int paddingWidth = source.get("paddingWidth").getAsInt();
            int paddingHeight = source.get("paddingHeight").getAsInt();
            imgCharacterWidth = source.get("imgCharacterWidth").getAsInt();
            imgCharacterHeight = source.get("imgCharacterHeight").getAsInt();
            JsonObject character = source.get("character").getAsJsonObject().get(characterName).getAsJsonObject();

            int startCol = character.get("startCol").getAsInt();
            int startRow = character.get("startRow").getAsInt();
            frameCols = character.get("frameCols").getAsInt();
            frameRows = character.get("frameRows").getAsInt();
            JsonObject direction = character.get("direction").getAsJsonObject();

            for (String key : direction.keySet()) {
                this.degree2AnimationID.put(Integer.parseInt(key), direction.get(key).getAsInt());
            }
            textureRegions = splitTexture(new Texture(sourceImage), imgCharacterWidth, imgCharacterHeight, gapWidth, gapHeight, paddingWidth, paddingHeight, frameCols, frameRows, startCol, startRow);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TextureRegion[][] splitTextureByFileName(String path) {
        int underscoreIndex1 = path.indexOf('_');
        int underscoreIndex2 = path.lastIndexOf('_');

        // Extract the x and y substrings
        String xSubstring = path.substring(underscoreIndex1 + 1, underscoreIndex2);
        String ySubstring = path.substring(underscoreIndex2 + 1, path.lastIndexOf('.'));

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

    private int getBestFit(Vector2 direction) {
        float degree = direction.angleDeg(new Vector2(1,0));
        float minDegree = 1000f;
        int bestID = 0;
        for (Integer key : degree2AnimationID.keySet()) {
            float temp = Math.abs(degree - key);
            if (temp < minDegree) {
                minDegree = temp;
                bestID = degree2AnimationID.get(key);
            }
        }
        return bestID;
    }

    public Animation<TextureRegion> getWalkFrames(Vector2 vectorDirection, float annimationSpeed) {
        int direction = getBestFit(vectorDirection);
//        direction 0: 0, direction 1: 90, direction 2: 180, direction 3: 270
        TextureRegion[] walkFrames = new TextureRegion[frameCols];
        for (int i = 0; i < frameCols; i++) {
            walkFrames[i] = textureRegions[direction][i];
        }
        Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(annimationSpeed, walkFrames);
        return walkAnimation;
    }

    public Animation<TextureRegion> getWalkFrames(Vector2 vectorDirection) {
        return getWalkFrames(vectorDirection, ANIMATION_SPEED);
    }

    public int getImgCharacterWidth() {
        return imgCharacterWidth;
    }

    public int getImgCharacterHeight() {
        return imgCharacterHeight;
    }

}
