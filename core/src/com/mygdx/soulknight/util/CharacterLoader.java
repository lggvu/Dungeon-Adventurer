package com.mygdx.soulknight.util;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

public class CharacterLoader {
    JsonObject json;
    public CharacterLoader(String infoPath) {
        try {
            json = new Gson().fromJson(Gdx.files.internal(infoPath).reader(), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CharacterLoader() {
        this("info/character_info.json");
    }

    public SimpleCharacter load(String characterName) {
        try {
            String texturePath = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
