package com.mygdx.soulknight.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Sword;
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
            else if (source.get("type").getAsString().equals("Sword")) {
                System.out.println("found sword");
                Sword sword = new Sword(owner, "weapon/sword.png");
                System.out.println("loaded texture");
                Texture texture = new Texture(source.get("effect_texture").getAsString());
                System.out.println(texture.getWidth() / 2 + " and " + texture.getHeight() / 3);
                TextureRegion[][] frames = SpriteLoader.splitTexture(
                        texture, 100, 100, 24, 50,
                        16, 30, 2, 3, 0, 1
                );
                sword.setEffectFrames(frames);
                System.out.println("loaded effect");
                return sword;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
