package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.ability.Ability;
import com.mygdx.soulknight.entity.Effect.CharacterEffect;
import com.mygdx.soulknight.entity.Effect.Effect;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.screen.MainGameScreen;
import com.mygdx.soulknight.util.SpriteLoader;


import java.util.ArrayList;

public abstract class SimpleCharacter {
    public static final String CHARACTER_INFO_PATH = "info/character_info.json";
    protected TextureRegion texture;
    protected WorldMap map;
    protected Room room;
    private static int ID = 0;
    protected boolean isStunned = false;
    protected String characterName;
    protected SpriteLoader spriteLoader;
    private int maxHP = 10;
    protected float stateTime = 0f;
    protected int currentHP = 10;
    protected float x = 30;
    protected float y = 30;
    protected float width = 20;
    protected float height = 32;
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
    public SimpleCharacter(String characterName, WorldMap map) {
        this.characterName = characterName;
        this.map = map;
        this.load();
        id = ID++;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SimpleCharacter) {
            return this.id == ((SimpleCharacter) object).id;
        }
        return false;
    }

    public void switchWeapon() {
        currentWeaponId++;
        if (currentWeaponId >= weapons.size()) {
            currentWeaponId = 0;
        }
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
            for (CharacterEffect effect1 : effectArrayList) {
                if (effect1.getClass().getName().equals(effect.getClass().getName())) {
                    effectArrayList.remove(effect1);
                    break;
                }
            }
            effectArrayList.add(effect);
        }
    }

    public JsonObject load() {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(SimpleCharacter.CHARACTER_INFO_PATH).reader(), JsonObject.class);
            JsonObject source = json.get(characterName).getAsJsonObject();
            width = source.get("width").getAsFloat();
            height = source.get("height").getAsFloat();
            weaponX = source.get("weapon_x").getAsFloat();
            weaponY = source.get("weapon_y").getAsFloat();
            spriteLoader = new SpriteLoader(source.get("texture_path").getAsString(), characterName);
            texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);
            maxHP = source.get("hp").getAsInt();
            currentHP = getCurrentMaxHP();
            speedRun = source.get("speed_run").getAsFloat();
            Weapon weapon = Weapon.load(source.get("default_weapon").getAsString());
            weapon.setOwner(this);
            collectWeapon(weapon);
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
        Rectangle rectangleTest = new Rectangle(this.x, testY, width, height);
        if (y != 0 && !map.isMapCollision(rectangleTest) && !isCollisionWithOtherCharacter(rectangleTest, allCharacter)) {
            this.y = testY;
        }
        rectangleTest = new Rectangle(testX, this.y, width, height);
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
        texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);
    }

    public boolean isFlipX() {
        return currentHeadDirection.x == -1;
    }

    public void draw(SpriteBatch batch) {
        for (CharacterEffect effect : effectArrayList) {
            if (effect.isDrawEffect()) {
                effect.draw(batch, x + width / 2, y + height + 8);
                break;
            }
        }
        batch.draw(texture, x, y, width, height);
    }

    public void collectWeapon(Weapon weapon) {
        weapon.setOwner(this);
        weapon.setOnGround(false);
        if (weapons.size() < getCurrentMaxWeaponNumber()) {
            weapons.add(weapon);
        } else {
            Weapon currentWeapon = getCurrentWeapon();
            currentWeapon.setPosition(this.x, this.y);
            weapons.remove(currentWeapon);
            map.addWeaponOnGround(currentWeapon);
            currentWeapon.setOnGround(true);
            weapons.add(weapon);
        }
        currentWeaponId = weapons.size() - 1;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
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

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
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

    public abstract void getHit(int damage);
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
