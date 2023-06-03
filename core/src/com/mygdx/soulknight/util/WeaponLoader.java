package com.mygdx.soulknight.util;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

public class WeaponLoader {
    JsonObject json;
    public WeaponLoader(String infoPath) {
        try {
            json = new Gson().fromJson(Gdx.files.internal(infoPath).reader(), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeaponLoader() {
        this("info/weapon_info.json");
    }
    public Weapon load(SimpleCharacter owner, String weaponName) {
        try {
            JsonObject source = json.get(weaponName).getAsJsonObject();
            if (source.get("type").getAsString().equals("Gun")) {
                Gun gun = new Gun(owner, source.get("gun_texture").getAsString());
                gun.setBulletTexturePath(source.get("bullet_texture").getAsString());
                return gun;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
