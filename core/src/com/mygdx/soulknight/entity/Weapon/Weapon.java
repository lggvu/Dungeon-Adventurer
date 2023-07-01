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
    private final static String INFO_PATH = "info/weapon_info.json";
    protected String name;
    private static int ID = 1;
    private int weaponID;
    protected float elapsedSeconds = 1;
    protected float intervalSeconds = 0.5f;
    protected SimpleCharacter owner;
    protected float rangeWeapon = 1000f;
    protected TextureRegion texture;
    private int damage;
    protected ArrayList<EffectEnum> effectsEnum = new ArrayList<>();
    protected int energyCost=0;
    protected float criticalRate=0;
    protected float x, y, width, height, originX, originY;
    protected boolean onGround = false;
    protected String texturePath;
    private TextureRegion textureForDrawer;
    protected boolean drawWeapon = true;

    public Weapon(String texturePath, int damage, int energyCost, float intervalSeconds, int rangeWeapon, float criticalRate) {
        initWeaponTexture(texturePath);
        this.intervalSeconds = intervalSeconds;
        this.elapsedSeconds = intervalSeconds;
        this.damage = damage;
        this.energyCost = energyCost;
        this.criticalRate = criticalRate;
        this.rangeWeapon = rangeWeapon;
        weaponID = ID++;
    }

    protected void initWeaponTexture(String texturePath) {
        this.texturePath = texturePath;
        this.texture = new TextureRegion(new Texture(texturePath));
        textureForDrawer = new TextureRegion(new Texture(texturePath));
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
        this.originX = origin_x;
        this.originY = origin_y;
    }
    public void reset() {
        elapsedSeconds = intervalSeconds;
    }

    public void setDrawWeapon(boolean drawWeapon) {
        this.drawWeapon = drawWeapon;
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

    public JsonObject load(JsonObject jsonObject) {
        JsonObject properties = jsonObject.get("properties").getAsJsonObject();
        this.damage = properties.get("damage").getAsInt();
        this.energyCost = properties.get("energy_cost").getAsInt();
        this.intervalSeconds = properties.get("attack_speed").getAsFloat();
        this.rangeWeapon = properties.get("range").getAsInt();
        this.criticalRate = properties.get("critical_rate").getAsFloat();
        this.width = jsonObject.get("width").getAsFloat();
        this.height = jsonObject.get("height").getAsFloat();
        this.originX = jsonObject.get("origin_x").getAsFloat();
        float originY = jsonObject.get("origin_y").getAsFloat();
        this.originY = height / 2;
        Iterator<JsonElement> effects = properties.get("effect").getAsJsonArray().iterator();
        ArrayList<EffectEnum> effectsEnum = new ArrayList<>();
        while (effects.hasNext()) {
            effectsEnum.add(EffectEnum.valueOf(effects.next().getAsString()));
        }
        this.addEffects(effectsEnum);
        return jsonObject;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static Weapon load(String weaponName) {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(INFO_PATH).reader(), JsonObject.class);
            JsonObject source = json.get(weaponName).getAsJsonObject();
            Weapon weapon = null;
            if (source.get("type").getAsString().equals("Gun")) {
                weapon = new Gun();
            }
            else if (source.get("type").getAsString().equals("Sword")) {
                weapon = new Sword();
            }
            weapon.load(source);
            weapon.setName(weaponName);
            return weapon;
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
    public TextureRegion getTextureCoolDown() {
        return textureForDrawer;
    }
    public float getTotalTimeCoolDown() {
        return intervalSeconds;
    };
    public float getCurrentTimeCoolDown() {
        return elapsedSeconds;
    };

    public int getCurrentDamage() {
        if (owner != null) {
            return damage + owner.getAbility().getDamageIncrease();
        }
        return damage;
    }
}