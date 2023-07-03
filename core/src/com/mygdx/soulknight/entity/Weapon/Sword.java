package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

import java.util.ArrayList;
import java.util.Iterator;

public class Sword extends Weapon {
    private Animation<TextureInfo> swordAnimation;
    private Vector2 attackDirection;
    private ArrayList<Slice> slices = new ArrayList<>();

    public Sword(String texturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate) {
        super(texturePath, damage, energyCost, intervalSeconds, rangeWeapon, criticalRate);
    }

    public Sword() { super(); }

    @Override
    public JsonObject load(JsonObject jsonObject) {
        jsonObject = super.load(jsonObject);
        initWeaponTexture(jsonObject.get("sword_texture").getAsString());
        JsonArray effectTexture = jsonObject.get("effect_texture").getAsJsonArray();
        TextureInfo[] frames = new TextureInfo[effectTexture.size()];
        int count = 0;
        Iterator<JsonElement> iterator = effectTexture.iterator();
        while (iterator.hasNext()) {
            frames[count++] = new TextureInfo(iterator.next().getAsString());
        }
        swordAnimation = new Animation<>(jsonObject.get("properties").getAsJsonObject().get("duration").getAsFloat(), frames);
        return jsonObject;
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Add any additional update logic for the sword here
        ArrayList<Slice> rmSlice = new ArrayList<>();
        for (Slice slice : slices) {
            slice.update(deltaTime);
            if (slice.isStop()) {
                rmSlice.add(slice);
            }
        }
        slices.removeAll(rmSlice);
    }

    @Override
    public void attack(Vector2 direction) {
        if (isAllowedAttack()) {
            elapsedSeconds = 0;
            subOwnerMana();
            slices.add(new Slice(swordAnimation, direction, owner, rangeWeapon, getCurrentDamage(), effectsEnum, criticalRate));
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (onGround) {
            batch.draw(texture, x, y, width, height);
            return;
        }

        // Draw effect when attacking
        for (Slice slice : slices) {
            slice.draw(batch);
        }

        // Draw the sword
        if (slices.size() <= 0 && drawWeapon) {
            if (owner.isFlipX() != texture.isFlipY()) {
                texture.flip(false, true);
            }
            float degree = owner.getCurrentHeadDirection().angleDeg(new Vector2(1, 0));
            Vector2 weaponPos = owner.getAbsoluteWeaponPos();
            float dlX = weaponPos.x;
            float dlY = weaponPos.y - originY;
            if (owner.isFlipX()) {
                dlX += originX;
            } else {
                dlX -= originX;
            }
            batch.draw(texture, dlX, dlY, originX, originY, width, height, 1, 1, degree);
        }
    }

    @Override
    public void setOnGround(boolean onGround) {
        super.setOnGround(onGround);
        if (onGround) {
            slices.clear();
        }
    }
}
