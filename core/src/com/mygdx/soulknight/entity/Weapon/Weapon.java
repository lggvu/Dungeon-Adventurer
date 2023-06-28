package com.mygdx.soulknight.entity.Weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.ability.AbilityDrawer;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Effect.Effect;
import com.mygdx.soulknight.entity.Effect.EffectEnum;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Weapon implements Pickable, AbilityDrawer {
//    public final static ArrayList<Bullet> BULLET_ARRAY_LIST = new ArrayList<Bullet>();
    protected String name;
    private static int ID = 1;
    private int weaponID;
    protected float elapsedSeconds = 1;
    protected float intervalSeconds = 0.5f;
    protected SimpleCharacter owner;
    protected float rangeWeapon = 1000f;
    protected TextureRegion texture;
    protected int damage;
    protected ArrayList<EffectEnum> effectsEnum = new ArrayList<>();
    protected int energyCost=0;
    protected float criticalRate=0;
    protected float x, y, width, height, origin_x, origin_y;
    protected boolean onGround = false;
    protected String texturePath;
    private TextureRegion textureForDrawer;

    public Weapon(String texturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate) {
        this.texturePath = texturePath;
        this.texture = new TextureRegion(new Texture(texturePath));
        textureForDrawer = new TextureRegion(new Texture(texturePath));
        this.intervalSeconds = intervalSeconds;
        this.elapsedSeconds = intervalSeconds;
        this.damage = damage;
        this.energyCost = energyCost;
        this.criticalRate = criticalRate;
        this.rangeWeapon = rangeWeapon;
        weaponID = ID++;
    }

    @Override
    public String toString() {
        return "Weapon " + weaponID;
    }
    public Weapon() {
//        default constructor
        weaponID = ID++;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    public void setRotateCenter(float origin_x, float origin_y) {
        this.origin_x = origin_x;
        this.origin_y = origin_y;
    }
    public void reset() {
        elapsedSeconds = intervalSeconds + 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Weapon) {
            return this.weaponID == ((Weapon) obj).weaponID;
        }
        return false;
    }

    public void setOwner(SimpleCharacter owner) {
        this.owner = owner;
    }

    public void update(float deltaTime) {
        this.elapsedSeconds += deltaTime;
        if (elapsedSeconds > intervalSeconds) {
            elapsedSeconds = intervalSeconds;
        }
    }

    //    When we have more weapon, we set attack function to an abstract class
    public abstract void attack(Vector2 direction);
    public abstract void draw(SpriteBatch batch);
    public void subOwnerMana() {
        if (owner instanceof Player) {
            ((Player) owner).setCurrentMana(((Player) owner).getCurrentMana() - energyCost);
        }
    }

    protected boolean isAllowedAttack() {
        if (owner instanceof Player) {
            if (((Player) owner).getCurrentMana() < energyCost) {
                return false;
            }
        }
        if (elapsedSeconds >= intervalSeconds) {
            return true;
        }
        return false;
    }

    public float getRangeWeapon() {
        return rangeWeapon;
    }

    public void setIntervalSeconds(float intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public TextureRegion getTextureRegion() {
        return texture;
    }

    public abstract void dealDamageMethod();

    public static Weapon load(String weaponName) {
        return load(weaponName, "info/weapon_info.json");
    }
    public static Weapon load(String weaponName, String infoPath) {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(infoPath).reader(), JsonObject.class);
            JsonObject source = json.get(weaponName).getAsJsonObject();
            JsonObject properties = source.get("properties").getAsJsonObject();
            int damage = properties.get("damage").getAsInt();
            int energyCost = properties.get("energy_cost").getAsInt();
            float attackSpeed = properties.get("attack_speed").getAsFloat();
            int range = properties.get("range").getAsInt();
            float criticalRate = properties.get("critical_rate").getAsFloat();
            float width = source.get("width").getAsFloat();
            float height = source.get("height").getAsFloat();
            float origin_x = source.get("origin_x").getAsFloat();
            float origin_y = source.get("origin_y").getAsFloat();
            Iterator<JsonElement> effects = source.get("effect").getAsJsonArray().iterator();
            ArrayList<EffectEnum> effectsEnum = new ArrayList<>();
            while (effects.hasNext()) {
                effectsEnum.add(EffectEnum.valueOf(effects.next().getAsString()));
            }

            if (source.get("type").getAsString().equals("Gun")) {
                String bulletTexturePath = source.get("bullet_texture").getAsString();
                String explosionTexturePath = source.get("explosion_texture").getAsString();
                String shotExplosionTexturePath = source.get("shot_explosion_texture").getAsString();
                float bulletSpeed = properties.get("bullet_speed").getAsFloat();
                Gun gun = new Gun(source.get("gun_texture").getAsString(), bulletTexturePath, explosionTexturePath, shotExplosionTexturePath, damage, energyCost, attackSpeed, range, criticalRate, bulletSpeed);
                gun.setSize(width, height);
//                gun.setRotateCenter(origin_x, origin_y);
                gun.addEffects(effectsEnum);
                gun.setRotateCenter(origin_x, height / 2);
                JsonElement jsonElement = source.get("attack_directions");
                if (jsonElement != null) {
                    Iterator<JsonElement> directions = jsonElement.getAsJsonArray().iterator();
                    while (directions.hasNext()) {
                        gun.addDirectionAttack(directions.next().getAsFloat());
                    }
                }
                return gun;
            }
            else if (source.get("type").getAsString().equals("Sword")) {
                Sword sword = new Sword(source.get("sword_texture").getAsString(), damage, energyCost, attackSpeed, range, criticalRate);
                JsonObject effectTexture = source.get("effect_texture").getAsJsonObject();
                Texture texture = new Texture(effectTexture.get("path").getAsString());
                TextureRegion[][] frames = SpriteLoader.splitTexture(
                        texture,
                        effectTexture.get("imgWidth").getAsInt(), effectTexture.get("imgHeight").getAsInt(),
                        effectTexture.get("gapWidth").getAsInt(), effectTexture.get("gapHeight").getAsInt(),
                        effectTexture.get("paddingWidth").getAsInt(), effectTexture.get("paddingHeight").getAsInt(),
                        effectTexture.get("frameCols").getAsInt(), effectTexture.get("frameRows").getAsInt(),
                        effectTexture.get("startCol").getAsInt(), effectTexture.get("startRow").getAsInt()
                );
                sword.setEffectFrames(frames);
                sword.setSize(width, height);
                sword.setRotateCenter(origin_x, height / 2);
//                sword.setRotateCenter(origin_x, origin_y);
                sword.addEffects(effectsEnum);
                return sword;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public float getX() {
        return x;
    };
    public float getY() {
        return y;
    };
    public float getWidth() {
        return width;
    };
    public float getHeight() {
        return height;
    };

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        if (onGround && texture.isFlipY()) {
            texture.flip(false, true);
        }
    }

    public void addEffects(ArrayList<EffectEnum> effectsEnum) {
        this.effectsEnum.addAll(effectsEnum);
    }

    public SimpleCharacter getOwner() {
        return owner;
    }

    public TextureRegion getTextureCoolDown() {
        return textureForDrawer;
    }
    public float getTotalTimeCoolDown() {
        return intervalSeconds;
    };
    public float getCurrentTimeCoolDown() {
        return elapsedSeconds;
    };
}