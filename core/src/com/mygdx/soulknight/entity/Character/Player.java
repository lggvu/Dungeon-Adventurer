package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.badlogic.gdx.Input;
import com.mygdx.soulknight.util.Collision;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

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
    private boolean isDying = false, isMoving = false;
    private Music movingSound = Gdx.audio.newMusic(Gdx.files.internal("sound-effect/running-2.mp3"));;
    private Skill dodgeSkill = new Skill(new TextureRegion(new Texture("buff/Dodge.png")), 0.5f, 0.5f) {
        @Override
        public void activateSkill() {
            super.activateSkill();
            Animation<TextureInfo> temp = animationMovement;
            animationMovement = dodgeAnimation;
            dodgeAnimation = temp;
        }

        @Override
        public void deactivateSkill() {
            super.deactivateSkill();
            Animation<TextureInfo> temp = animationMovement;
            animationMovement = dodgeAnimation;
            dodgeAnimation = temp;
        }
    };
    protected Skill specialSkill;
    protected Animation<TextureInfo> dodgeAnimation;
    protected HashMap<SimpleCharacter, Boolean> monsterInVision = new HashMap<>();
    private Room room;

    public Player(String characterName, WorldMap map) {
        super(characterName, map);
        setMaxWeaponNumber(2);
        this.movingSound.setVolume(0.9f);
    }


    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        this.maxArmor = source.get("armor").getAsInt();
        maxArmor = Integer.MAX_VALUE - 100;
//        System.out.println(maxArmor);
        this.currentArmor = getCurrentMaxArmor();
        dodgeAnimation = new Animation<>(0.1f, SpriteLoader.loadTextureInfo(source.get("dodge_texture_path").getAsJsonArray()));
        this.maxMana = source.get("energy").getAsInt();
        setCurrentMana(maxMana);
        return source;
    }

    @Override
    public void activateDying() {
        if (!isDying) {
            isDying = true;
            super.activateDying();
        }
    }
    private void updatePlayerVision() {
        monsterInVision.clear();
        Vector2 playerPos = new Vector2(x + getWidth() / 2, y + getHeight() / 2);
        for (SimpleCharacter character : getEnemyList()) {
            Vector2 monsterPos = new Vector2(character.x + character.getWidth() / 2, character.y + character.getHeight() / 2);
            if (monsterPos.dst(playerPos) > visionRange) {
                monsterInVision.put(character, false);
                continue;
            }
            boolean collide = false;
            for (Rectangle rectangle : map.getCollisionRect()) {
                if (Collision.isLineCollideRect(playerPos, monsterPos, rectangle)) {
                    monsterInVision.put(character, false);
                    collide = true;
                    break;
                }
            }
            if (collide) {
                continue;
            }
            for (DestroyableObject object : map.getDestroyableObjects()) {
                if (Collision.isLineCollideRect(playerPos, monsterPos, object.getRectangle())) {
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

    @Override
    public void getHit(int damage, DamageType damageType, boolean isCrit) {
        if (isImmunityWithDamage(damageType)) {
            return;
        }
        getMap().addDamageNumber(damage, damageType, isCrit, x, y);
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

    public boolean isInVision(SimpleCharacter character) {
        if (monsterInVision.containsKey(character)) {
            return monsterInVision.get(character);
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {

        if (!isAlive()) {
            stateTime += deltaTime;
            return;
        }

        updatePlayerVision();
        specialSkill.update(deltaTime);
        dodgeSkill.update(deltaTime);
        if (dodgeSkill.isInProgresss()) {
            Vector2 lmdr = getLastMoveDirection();
            move(lmdr.x, lmdr.y, deltaTime, 400f);
            updateMovementAnimation(deltaTime);
        } else {
            applyEffect(deltaTime);
        }
        if (!dodgeSkill.isInProgresss() && (!isStunned || getAbility().isStunImmunity())) {
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
                updateMovementAnimation(deltaTime);
                moveDirection = moveDirection.nor();
                move(moveDirection.x, moveDirection.y, deltaTime);
                setLookAtDirection(lastMoveDirection.x, lastMoveDirection.y);
                isMoving = true;
            } else {
                stateTime = 0;
                isMoving = false;
            }

            this.room = map.getRoomPlayerIn();
            fighting = false;

            Vector2 attackDirection = getAttackDirection(room);
            if (room != null && room.getMonsterAlive().size() > 0) {
                fighting = true;
                room.setCombat(true);
                setLookAtDirection(attackDirection.x, attackDirection.y);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                attack(attackDirection);
            }
        }
        if (isMoving & !this.movingSound.isPlaying()) {
            this.movingSound.play();
        }
        if (!isMoving){
            this.movingSound.stop();
        }
        ArrayList<Pickable> collectItem = autoCollect();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Pickable item = getNearestPickableInRange();
            if (item != null) {
                collectItem.add(item);
                map.getItemsOnGround().remove(item);
            }
            if (new Vector2(x, y).dst(map.getGateX(), map.getGateY()) < collectRange * 2) {
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
            Vector2 playerPosition = new Vector2(x + getWidth() / 2, y + getHeight() / 2);
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
        return getLookAtDirection();
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

    public void setCurrentArmor(int currentArmor) {
        this.currentArmor = currentArmor;
    }

    public Skill getDodgeSkill() {
        return dodgeSkill;
    }

    public Skill getSpecialSkill() {
        return specialSkill;
    }
    public Music getMovingSound(){
        return this.movingSound;
    }
}
