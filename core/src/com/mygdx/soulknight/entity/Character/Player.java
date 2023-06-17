package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.specialskill.SpecialSkill;
import com.mygdx.soulknight.util.SpriteLoader;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

public class Player extends SimpleCharacter {
    private int maxArmor = 100;
    private int currentArmor = maxArmor;
    private int currentMana = 200;
    private int maxMana = 200;
    private float timeHealArmor = 1.5f;
    private float currentHealArmor = 0;
    private boolean fighting = false;
    private float visionRange = 1000f;
    private float collectRange = 30f;
    private int maxWeaponNumber = 2;

    private boolean isStunned=false;
    protected boolean isPushed=false;

    protected float stunDuration=0.3f;

    protected float stunTimer;
    private SpecialSkill skill = null;
    public Player(String characterName, WorldMap map) {
        super(characterName, map);
        this.load();
    }
    private Room room;


    @Override
    public void load() {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(SimpleCharacter.CHARACTER_INFO_PATH).reader(), JsonObject.class);
            JsonObject source = json.get(characterName).getAsJsonObject();
            if (!source.get("type").getAsString().equals("hero")) {
                throw new Exception("Player must load character type hero");
            }
            spriteLoader = new SpriteLoader(source.get("texture_path").getAsString(), characterName);
            texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);
            maxHP = source.get("hp").getAsInt();
            currentHP = maxHP - 5;
            Weapon weapon = Weapon.load(source.get("default_weapon").getAsString());
            weapon.setOwner(this);
            addWeapon(weapon);
            speedRun = source.get("speed_run").getAsFloat();

            maxArmor = source.get("armor").getAsInt();
            currentArmor = maxArmor;
            maxMana = source.get("energy").getAsInt();
            setCurrentMana(maxMana - 100);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
    public void getHit(int damage, Vector2 direction, Bullet bullet) {
        if (currentArmor < damage) {
            currentHP = currentHP - (damage - currentArmor);
            currentArmor = 0;
        } else {
            currentArmor -= damage;
        }
        if (currentHP < 0) {
            currentHP = 0;
        }
        hitBulletArrayList.add(bullet);
        isPushed=true;
        isStunned=true;
        stunTimer=stunDuration;

    }
    public void pushedByBullet(float deltaTime) {
        if (!hitBulletArrayList.isEmpty()) {
            float totalPushForceX = 0.0f;
            float totalPushForceY = 0.0f;
            ArrayList<Bullet> removeBulletList = new ArrayList<>();
            for (Bullet bullet : hitBulletArrayList) {
                bullet.setPushTimer(bullet.getPushTimer() - deltaTime);
                if (bullet.getPushTimer() > 0) {
                    totalPushForceX += bullet.getImpactForce() * bullet.getDirection().x;
                    totalPushForceY += bullet.getImpactForce() * bullet.getDirection().y;
                } else {
                    removeBulletList.add(bullet);
                }
            }
            hitBulletArrayList.removeAll(removeBulletList);
            float testX = this.x + totalPushForceX;
            float testY = this.y + totalPushForceY;

            if (!map.isMapCollision(new Rectangle(testX, testY, width, height))) {
                this.x = testX;
                this.y = testY;
            }
        }
    }
    @Override
    public void attack(Vector2 direction) {
        getCurrentWeapon().attack(direction);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        for (Weapon weapon : weapons) {
            if (weapon.equals(getCurrentWeapon())) {
                weapon.draw(batch);
            } else {
                if (weapon instanceof Gun) {
                    for (Bullet bullet : ((Gun) weapon).getBulletArrayList()) {
                        bullet.draw(batch);
                    }
                    for (Explosion explosion : ((Gun) weapon).getExplosionArrayList()) {
                        explosion.draw(batch);
                    }
                }
            }
        }
        if (this.skill != null) {
        	this.skill.draw(batch);
        }
    }

    @Override
    public void getHit(int damage) {
    }

    @Override
    public void update(float deltaTime) {
        pushedByBullet(deltaTime);
        if (isStunned){
            stunTimer-=deltaTime;
            if (stunTimer<=0){
                isStunned=false;
                }}
        else{
            Vector2 moveDirection = new Vector2(0, 0);

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                moveDirection = moveDirection.add(-1, 0);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                moveDirection = moveDirection.add(1, 0);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                moveDirection = moveDirection.add(0, 1);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                moveDirection = moveDirection.add(0, -1);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                switchWeapon();
            }

            if (moveDirection.x != 0 || moveDirection.y != 0) {

                moveDirection = moveDirection.nor();
                move(moveDirection.x, moveDirection.y, deltaTime);
            }

        ArrayList<Pickable> collectItem = autoCollect();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Pickable item = getNearestPickableInRange();
            if (item != null) {
                collectItem.add(item);
                map.getItemsOnGround().remove(item);
            }
        }

        this.room = map.getRoomPlayerIn();
        fighting = false;
        if (room != null && room.getMonsterAlive().size() > 0) {
            fighting = true;
            room.setCombat(true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            attack(getAttackDirection(room));
        }

//        Armor auto heal
        currentHealArmor += deltaTime;
        if (currentArmor == maxArmor) {
            currentHealArmor = 0;
        }

        if (currentHealArmor > timeHealArmor) {
            currentHealArmor = 0;
            currentArmor++;
        }
        if (currentArmor > maxArmor) {
            currentArmor = maxArmor;
        }

        for (Weapon weapon : weapons) {
            if (weapon.equals(getCurrentWeapon())) {
                weapon.update(deltaTime);
            } else {
                if (weapon instanceof Gun) {
                    for (Bullet bullet : ((Gun) weapon).getBulletArrayList()) {
                        bullet.update(deltaTime);

                    }
                }
            }
        }

        if (this.skill.isActivate()) {
        	this.skill.activate();
        }
        this.skill.update(deltaTime);

        for (Pickable item : collectItem) {
            if (item instanceof Item) {
                ((Item) item).use(this);
            } else if (item instanceof Weapon) {
                collectWeapon((Weapon) item);
            }
        }


        }
        }

    public Vector2 getAttackDirection(Room roomPlayerIn) {
        if (roomPlayerIn != null) {
            float minDst = Float.MAX_VALUE;
            Vector2 direction = null;
            Vector2 playerPosition = new Vector2(x, y);
            for (Monster monster : roomPlayerIn.getMonsterAlive()) {
                float dst = playerPosition.dst(monster.getX(), monster.getY());
                if (dst < minDst) {
                    minDst = dst;
                    direction = new Vector2(monster.getX(), monster.getY()).sub(playerPosition);
                }
            }
            if (direction != null) {
                return direction;
            }
        }
        return lastMoveDirection;
    }

    public void collectWeapon(Weapon weapon) {
        weapon.setOwner(this);
        weapon.setOnGround(false);
        if (weapons.size() < maxWeaponNumber) {
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

    public ArrayList<Pickable> autoCollect() {
        ArrayList<Pickable> removeList = new ArrayList<>();
        ArrayList<Pickable> collectItem = new ArrayList<>();
        Vector2 posPlayer = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        for (Pickable item : map.getItemsOnGround()) {
            float dst = posPlayer.dst(item.getX() + item.getWidth() / 2, item.getY() + item.getHeight() / 2);
            if (item instanceof Item) {
                if (((Item) item).isAutoCollect() && dst <= collectRange) {
                    collectItem.add(item);
                    removeList.add(item);
                }
            }
        }
        map.getItemsOnGround().removeAll(removeList);
        return collectItem;
    }

    public void switchWeapon() {
        currentWeaponId++;
        if (currentWeaponId >= weapons.size()) {
            currentWeaponId = 0;
        }
    }

    public int getMaxArmor() {
        return maxArmor;
    }

    public int getCurrentArmor() {
        return currentArmor;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public boolean isFighting() {
        return fighting;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public Pickable getNearestPickableInRange() {
        Vector2 posPlayer = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        float minDst = Float.MAX_VALUE;
        Pickable nearestItem = null;
        for (Pickable item : map.getItemsOnGround()) {
            float dst = posPlayer.dst(item.getX() + item.getWidth() / 2, item.getY() + item.getHeight() / 2);
            if (dst < minDst) {
                minDst = dst;
                nearestItem = item;
            }
        }
        if (minDst <= collectRange) {
            return nearestItem;
        }
        return null;
    }
    public void setSkill(SpecialSkill skill) {
    	this.skill = skill;
    }
    public SpecialSkill getSkill() {
    	return this.skill;
    }
    public Room getRoom() {
        return this.room;
    }
}
