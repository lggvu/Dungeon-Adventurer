package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.ability.Ability;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Effect.*;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;


import java.util.ArrayList;

public abstract class SimpleCharacter {
    public static final String CHARACTER_INFO_PATH = "info/character_info.json";
    protected WorldMap map;
    protected Room room;
    private static int ID = 0;
    protected boolean isStunned = false;
    protected String characterName;

    protected TextureInfo staticTexture;
    protected Animation<TextureInfo> animationMovement;
    protected Animation<TextureInfo> dyingAnimation;
    private int maxHP = 10;
    protected float stateTime = 0f;
    protected int currentHP = 10;
    protected float x = 30;
    protected float y = 30;
    protected float weaponX, weaponY;
    protected float speedRun = 180f;
    protected Vector2 lastMoveDirection = new Vector2(1, 0);
    protected Vector2 currentHeadDirection = new Vector2(1, 0);
    protected int currentWeaponId = 0;
    protected ArrayList<Weapon> weapons = new ArrayList<>();
    private int maxWeaponNumber = 1;
    protected ArrayList<CharacterEffect> effectArrayList = new ArrayList<>();
    private int id;
    private Ability ability = new Ability();
    protected boolean drawCharacter = true;
    public SimpleCharacter(String characterName, WorldMap map) {
        this.characterName = characterName;
        this.map = map;
        this.load();
        id = ID++;
    }

    public int getId() {
        return id;
    }

    public Vector2 getAbsoluteWeaponPos(boolean flipX) {
        float dlX;
        float dlY = getY() + getWeaponY();
        if (flipX) {
            dlX = getX() + getWidth() - getWeaponX();
        } else {
            dlX = getX() + getWeaponX();
        }
        return new Vector2(dlX, dlY);
    }

    public Vector2 getAbsoluteWeaponPos() {
        return getAbsoluteWeaponPos(isFlipX());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SimpleCharacter) {
            return this.id == ((SimpleCharacter) object).id;
        }
        return false;
    }

    public void switchWeapon() {
        getCurrentWeapon().setDrawWeapon(false);
        currentWeaponId++;
        if (currentWeaponId >= weapons.size()) {
            currentWeaponId = 0;
        }
        getCurrentWeapon().setDrawWeapon(true);
    }

    protected void applyEffect(float deltaTime) {
        this.isStunned = false;
        ArrayList<CharacterEffect> removeList = new ArrayList<>();
        for (CharacterEffect effect : effectArrayList) {
            effect.update(deltaTime, this);
            if (effect.isFinish()) {
                removeList.add(effect);
            }
        }
        effectArrayList.removeAll(removeList);
    }

    public void addEffects(ArrayList<CharacterEffect> effects) {
//        Reset effect exist not accumulate it
        for (CharacterEffect effect : effects) {
            addEffect(effect);
        }
    }

    public void activateDying() {
        stateTime = 0;
        drawCharacter = false;
        for (Weapon weapon : weapons) {
            weapon.setDrawWeapon(false);
        }

    }

    public void addEffect(CharacterEffect effect) {
        if (isImmuneWithEffect(effect)) {
            return;
        }
        for (CharacterEffect effect1 : effectArrayList) {
            if (effect1.getClass().getName().equals(effect.getClass().getName())) {
                effectArrayList.remove(effect1);
                break;
            }
        }
        effectArrayList.add(effect);
    }

    public boolean isImmuneWithEffect(CharacterEffect effect) {
        if (getAbility().isStunImmunity() && effect instanceof Stun) {
            return true;
        } else if (getAbility().isPoisonImmunity() && effect instanceof Poison) {
            return true;
        } else if (getAbility().isFireImmunity() && effect instanceof Fire) {
            return true;
        } else if (getAbility().isLightningImmunity() && effect instanceof Lightning) {
            return true;
        }
        return false;
    }

    public boolean isAlive() {
        return currentHP > 0;
    }
    protected float maxWidth=0;
    protected float maxHeight=0;
    public JsonObject load() {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(SimpleCharacter.CHARACTER_INFO_PATH).reader(), JsonObject.class);
            JsonObject source = json.get(characterName).getAsJsonObject();
            weaponX = source.get("weapon_x").getAsFloat();
            weaponY = source.get("weapon_y").getAsFloat();
            TextureInfo[] textureInfos = SpriteLoader.loadTextureInfo(source.get("texture_path").getAsJsonArray());
            animationMovement = new Animation<>(0.15f, SpriteLoader.loadTextureInfo(source.get("texture_path").getAsJsonArray()));
            dyingAnimation = new Animation<>(0.15f, SpriteLoader.loadTextureInfo(source.get("die_texture_path").getAsJsonArray()));
            maxHP = source.get("hp").getAsInt();
            currentHP = getCurrentMaxHP();
            speedRun = source.get("speed_run").getAsFloat();
            Weapon weapon = Weapon.load(source.get("default_weapon").getAsString());
            weapon.setOwner(this);
            collectWeapon(weapon);
            for(TextureInfo textureInfo : textureInfos){
                if(textureInfo.getWidth()>this.maxWidth){
                    this.maxWidth= textureInfo.getWidth();
                }
                if(textureInfo.getHeight()>this.maxHeight){
                    this.maxHeight= textureInfo.getHeight();
                }

            }

            return source;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    };

    public abstract void update(float deltaTime);

    public void setMap(WorldMap map) {
        this.map = map;
    }

    public void move(float x, float y, float deltaTime) {
        move(x, y, deltaTime, speedRun);
    }

    public void move(float x, float y, float deltaTime, float speed) {
        stateTime += deltaTime;
        setLastMoveDirection(x, y);
        x = lastMoveDirection.x;
        y = lastMoveDirection.y;
        float testX = this.x + x * deltaTime * speed;
        float testY = this.y + y * deltaTime * speed;
        ArrayList<SimpleCharacter> allCharacter = map.getAllCharacter();
        Rectangle rectangleTest = new Rectangle(this.x, testY, this.maxWidth, this.maxHeight-this.maxHeight/2);
        if (y != 0 && !map.isMapCollision(rectangleTest) && !isCollisionWithOtherCharacter(rectangleTest, allCharacter)) {
            this.y = testY;
        }
        rectangleTest = new Rectangle(testX, this.y,this.maxWidth, this.maxHeight-this.maxHeight/2);
        if (x != 0 && !map.isMapCollision(rectangleTest) && !isCollisionWithOtherCharacter(rectangleTest, allCharacter)) {
            this.x = testX;
        }
    }

    public boolean isCollisionWithOtherCharacter(Rectangle rectangle, ArrayList<SimpleCharacter> characters) {
        for (SimpleCharacter character : characters) {
            if (!character.equals(this) && rectangle.overlaps(character.getRectangle())) {
                return true;
            }
        }
        return false;
    }

    protected void setLastMoveDirection(float x, float y) {
        lastMoveDirection = new Vector2(x, y).nor();
        float degree = lastMoveDirection.angleDeg(currentHeadDirection);
        if (degree > 90 && degree < 270) {
            currentHeadDirection = new Vector2(currentHeadDirection.x * (-1), 0);
        }
    }

    public boolean isFlipX() {
        return currentHeadDirection.x == -1;
    }

    public void draw(SpriteBatch batch) {
        if (!isAlive()) {
            if (!isFinishDying()) {
                TextureInfo textureInfo = dyingAnimation.getKeyFrame(stateTime, true);
                batch.draw(textureInfo.getTextureRegion(), x, y, textureInfo.getWidth(), textureInfo.getHeight());
            }
            return;
        }
        if (!drawCharacter) {
            return;
        }
        for (CharacterEffect effect : effectArrayList) {
            if (effect.isDrawEffect() && !isImmuneWithEffect(effect)) {
                effect.draw(batch, x + getWidth() / 2, y + getHeight() + 8);
                break;
            }
        }
        TextureInfo textureInfo = animationMovement.getKeyFrame(stateTime, true);
        TextureRegion textureRegion = textureInfo.getTextureRegion();
        if (textureRegion.isFlipX() != isFlipX()) {
            textureRegion.flip(true, false);
        }
        batch.draw(textureRegion, x, y, textureInfo.getWidth(), textureInfo.getHeight());
    }

    public boolean isFinishDying() {
        return dyingAnimation.isAnimationFinished(stateTime);
    }

    public void collectWeapon(Weapon weapon) {
        weapon.setOwner(this);
        weapon.setOnGround(false);
        if (weapons.size() < getCurrentMaxWeaponNumber()) {
            weapons.add(weapon);
        } else {
            Weapon currentWeapon = getCurrentWeapon();
            currentWeapon.reset();
            currentWeapon.setPosition(this.x, this.y);
            weapons.remove(currentWeapon);
            map.addWeaponOnGround(currentWeapon);
            currentWeapon.setOnGround(true);
            weapons.add(weapon);
        }
        currentWeaponId = weapons.size() - 1;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, getWidth(), getWidth());
    }
    public float getWidth() {
        return animationMovement.getKeyFrame(stateTime, true).getWidth();
    }

    public float getHeight() {
        return animationMovement.getKeyFrame(stateTime, true).getHeight();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getCurrentMaxHP() {
        return maxHP + getAbility().getHpIncrease();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getWeaponX() {
        return weaponX;
    }

    public float getWeaponY() {
        return weaponY;
    }

    public Weapon getCurrentWeapon() {
        if (weapons.size() > 0) {
            return weapons.get(currentWeaponId);
        }
        return null;
    };

    public void setSpeedRun(float speedRun) {
        this.speedRun = speedRun;
    }

    public void getHit(int damamge, DamageType damageType) {
        getHit(damamge, damageType, false);
    }
    public abstract void getHit(int damage, DamageType damageType, boolean isCrit);
    public abstract void attack(Vector2 direction);

    public Vector2 getLastMoveDirection() {
        return lastMoveDirection;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public Vector2 getCurrentHeadDirection() {
        return currentHeadDirection;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public WorldMap getMap() {
        return map;
    }
    @Override
    public String toString() {
        return ""+id;
    }

    public boolean isImmunityWithDamage(DamageType damageType) {
        if (damageType == DamageType.FIRE && getAbility().isFireImmunity()) {
            return true;
        } else if (damageType == DamageType.LIGHTNING && getAbility().isLightningImmunity()) {
            return true;
        } else if (damageType == DamageType.POISON && getAbility().isPoisonImmunity()) {
            return true;
        }
        return false;
    }

    public ArrayList<SimpleCharacter> getEnemyList() {
        ArrayList<SimpleCharacter> listEnemy = new ArrayList<>();
        if (this instanceof Player) {
            Room room = map.getRoomPlayerIn();
            if (room != null) {
                for (SimpleCharacter monster : room.getMonsterAlive()) {
                    listEnemy.add(monster);
                }
            }
        } else if (this instanceof Monster) {
            listEnemy.add((SimpleCharacter) map.getPlayer());
        }
        return listEnemy;
    }
    public void setStunned(boolean stunned) {
        isStunned = stunned;
    }

    public Ability getAbility() {
        return ability;
    }

    public int getCurrentMaxWeaponNumber() {
        return maxWeaponNumber + getAbility().getNumWeaponIncrease();
    }

    public void setMaxWeaponNumber(int maxWeaponNumber) {
        this.maxWeaponNumber = maxWeaponNumber;
    }
}
