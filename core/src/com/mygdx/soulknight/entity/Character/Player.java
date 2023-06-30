package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.ability.Ability;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.badlogic.gdx.Input;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Player extends SimpleCharacter {
    private int maxArmor;
    private int currentArmor;
    private int currentMana;
    private int maxMana;
    private float timeHealArmor = 1.5f;
    private float currentHealArmor = 0;
    private boolean fighting = false;
    private float visionRange = 1000f;
    private float collectRange = 30f;
    protected SpriteLoader dodgeSpriteLoader;
    protected float totalTimeDodge = 0.5f;
    protected boolean isRunningDodge = false;
    protected float specialSkillCoolDown = 5;
    protected boolean isCoolingDown = false;
    protected float coolDownTimer = 5;
    protected float totalTimeImplement = 0;
    protected float timeImplementLeft = 0;
    protected boolean isImplement = false;
    protected HashMap<SimpleCharacter, Boolean> monsterInVision = new HashMap<>();
    private Room room;
    private Vector2 actualLastMoveDirection = new Vector2(1, 0);

    public Player(String characterName, WorldMap map) {
        super(characterName, map);
        setMaxWeaponNumber(2);
        getAbility().addAbility(this, Ability.AbilityEnum.MAX_HP_INCREASE);
        getAbility().addAbility(this, Ability.AbilityEnum.NUM_WALL_COLLIDE_INCREASE);
        getAbility().addAbility(this, Ability.AbilityEnum.MAX_WEAPON_INCREASE);
        getAbility().addAbility(this, Ability.AbilityEnum.STUN_IMMUNITY);
    }


    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        this.maxArmor = source.get("armor").getAsInt();
        maxArmor = Integer.MAX_VALUE - 100;
        this.currentArmor = getCurrentMaxArmor();
        dodgeSpriteLoader = new SpriteLoader("character/img2.png", "young");
        this.maxMana = source.get("energy").getAsInt();
        setCurrentMana(maxMana);
        return source;
    }

    private void updatePlayerVision() {
        monsterInVision.clear();
        Vector2 playerPos = new Vector2(x + width / 2, y + height / 2);
        for (SimpleCharacter character : getEnemyList()) {
            Vector2 monsterPos = new Vector2(character.x + character.width / 2, character.y + character.height / 2);
            if (monsterPos.dst(playerPos) > visionRange) {
                monsterInVision.put(character, false);
                continue;
            }
            boolean collide = false;
            for (Rectangle rectangle : map.getCollisionRect()) {
                if (isLineCollideRectange(playerPos, monsterPos, rectangle)) {
                    monsterInVision.put(character, false);
                    collide = true;
                    break;
                }
            }
            if (collide) {
                continue;
            }
            for (DestroyableObject object : map.getDestroyableObjects()) {
                if (isLineCollideRectange(playerPos, monsterPos, object.getRectangle())) {
                    monsterInVision.put(character, false);
                    collide = true;
                    break;
                }
            }
            if (!collide) {
                monsterInVision.put(character, true);
            }
        }
    }

    private boolean isLineCollideRectange(Vector2 pos1, Vector2 pos2, Rectangle rectangle) {
        if (pos1.x < rectangle.x && pos2.x < rectangle.x) {
            return false;
        }
        if (pos1.x > rectangle.x + rectangle.width && pos2.x > rectangle.x + rectangle.width) {
            return false;
        }
        if (pos1.y < rectangle.y && pos2.y < rectangle.y) {
            return false;
        }
        if (pos1.y > rectangle.y + rectangle.height && pos2.y > rectangle.y + rectangle.height) {
            return false;
        }
        if (pos1.x == pos2.x) {
            return true;
        }
        float a = (pos2.y - pos1.y) / (pos2.x - pos1.x);
        float b = (pos2.x * pos1.y - pos1.x * pos2.y) / (pos2.x - pos1.x);
        float test = a * rectangle.x + b;
        if (rectangle.y <= test && test <= (rectangle.y + rectangle.height)) {
            return true;
        }
        test = a * (rectangle.x + rectangle.width) + b;
        if (rectangle.y <= test && test <= (rectangle.y + rectangle.height)) {
            return true;
        }
        test = (rectangle.y - b) / a;
        if (rectangle.x <= test && test <= rectangle.x + rectangle.width) {
            return true;
        }
        test = (rectangle.y + rectangle.height - b) / a;
        if (rectangle.x <= test && test <= rectangle.x + rectangle.width) {
            return true;
        }
        return false;
    }

    @Override
    public void getHit(int damage, DamageType damageType) {
        if (isImmunityWithDamage(damageType)) {
            return;
        }
        if (currentArmor < damage) {
            currentHP = currentHP - (damage - currentArmor);
            currentArmor = 0;
        } else {
            currentArmor -= damage;
        }
        if (currentHP < 0) {
            currentHP = 0;
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
                }
            }
        }
    }
    public abstract void applySpecialSkill(float deltaTime);
    public void activateSpecialSkill() {
        isImplement = true;
        timeImplementLeft = totalTimeImplement;
    }
    public boolean isCoolingDown() {
        return isCoolingDown;
    }
    public float getCoolDownTimer() {
        return coolDownTimer;
    }
    public float getSpecialSkillCoolDown() {
        return specialSkillCoolDown;
    }

    public boolean isInVision(SimpleCharacter character) {
        if (monsterInVision.containsKey(character)) {
            return monsterInVision.get(character);
        }
        return false;
    }

    public void disableDodgeMode() {
        stateTime = 0;
        SpriteLoader temp = spriteLoader;
        spriteLoader = dodgeSpriteLoader;
        dodgeSpriteLoader = temp;
        isRunningDodge = false;
    }

    @Override
    public void update(float deltaTime) {
        updatePlayerVision();
        if (isRunningDodge) {
            move(actualLastMoveDirection.x, actualLastMoveDirection.y, deltaTime, 400f);
            stateTime += deltaTime;
            if (stateTime > totalTimeDodge) {
                disableDodgeMode();
            }
        } else {
            applyEffect(deltaTime);
        }
        if (isCoolingDown) {
            coolDownTimer -= deltaTime;
            if (coolDownTimer <= 0) {
                isCoolingDown = false;
            }
        }
        if (!isRunningDodge && (!isStunned || getAbility().isStunImmunity())) {
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
                actualLastMoveDirection = new Vector2(moveDirection.x, moveDirection.y).nor();
            } else {
                stateTime = 0;
                texture = spriteLoader.getWalkFrames(currentHeadDirection).getKeyFrame(stateTime, true);
            }

            this.room = map.getRoomPlayerIn();
            fighting = false;

            Vector2 attackDirection = getAttackDirection(room);
            if (room != null && room.getMonsterAlive().size() > 0) {
                fighting = true;
                room.setCombat(true);
                setLastMoveDirection(attackDirection.x, attackDirection.y);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                attack(attackDirection);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                stateTime = 0;
                SpriteLoader temp = spriteLoader;
                spriteLoader = dodgeSpriteLoader;
                dodgeSpriteLoader = temp;
                isRunningDodge = true;
            }
        }

        ArrayList<Pickable> collectItem = autoCollect();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Pickable item = getNearestPickableInRange();
            if (item != null) {
                collectItem.add(item);
                map.getItemsOnGround().remove(item);
            }
            if (new Vector2(x, y).dst(map.getGateX(), map.getGateY()) < collectRange) {
                map.setOver(true);
            }
        }

//        Armor auto heal
        currentHealArmor += deltaTime;
        int currentMaxArmor = getCurrentMaxArmor();
        if (currentArmor == currentMaxArmor) {
            currentHealArmor = 0;
        }

        if (currentHealArmor > timeHealArmor) {
            currentHealArmor = 0;
            currentArmor++;
        }
        if (currentArmor > currentMaxArmor) {
            currentArmor = currentMaxArmor;
        }
        for (Weapon weapon : weapons) {
            weapon.update(deltaTime);
        }

        applySpecialSkill(deltaTime);
        for (Pickable item : collectItem) {
            if (item instanceof Item) {
                ((Item) item).use(this);
            } else if (item instanceof Weapon) {
                collectWeapon((Weapon) item);
            }
        }
    }

    protected Vector2 getAttackDirection(Room roomPlayerIn) {
        if (roomPlayerIn != null) {
            float minDst = Float.MAX_VALUE;
            Vector2 direction = null;
            Vector2 playerPosition = new Vector2(x + width / 2, y + height / 2);
            for (Monster monster : roomPlayerIn.getMonsterAlive()) {
                if (isInVision(monster)) {
                    Vector2 monsterPos = new Vector2(monster.getX() + monster.getWidth() / 2, monster.getY() + monster.getHeight() / 2);
                    float dst = playerPosition.dst(monsterPos);
                    if (dst < minDst) {
                        minDst = dst;
                        direction = monsterPos.sub(playerPosition);
                    }
                }
            }
            if (direction != null) {
                return direction;
            }
        }
        return lastMoveDirection;
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

    public int getCurrentMaxArmor() {
        return maxArmor + getAbility().getArmorIncrease();
    }

    public int getCurrentArmor() {
        return currentArmor;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getCurrentMaxMana() {
        return maxMana + getAbility().getManaIncrease();
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

    public float getTotalTimeImplement() {
        return totalTimeImplement;
    }

    public float getTimeImplementLeft() {
        return timeImplementLeft;
    }

    public boolean isImplement() {
        return isImplement;
    }

    public void setCurrentArmor(int currentArmor) {
        this.currentArmor = currentArmor;
    }
}
