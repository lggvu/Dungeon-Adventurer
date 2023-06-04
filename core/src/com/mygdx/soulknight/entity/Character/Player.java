package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.screen.MainGameScreen;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.WeaponLoader;
import com.badlogic.gdx.Input;

public class Player extends SimpleCharacter {
    private int maxArmor = 6;
    private int currentArmor = maxArmor;
    private int currentMana = 200;
    private int maxMana = 200;
    private float timeHealArmor = 1.5f;
    private float currentHealArmor = 0;
    private boolean fighting = false;
    private float visionRange = 1000f;

    public Player(String characterName, WorldMap map) {
        super(characterName, map);
    }


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
            currentHP = maxHP;
            Weapon weapon = Weapon.load(source.get("default_weapon").getAsString());
            weapon.setOwner(this);
            addWeapon(weapon);
            speedRun = source.get("speed_run").getAsFloat();

            maxArmor = source.get("armor").getAsInt();
            currentArmor = maxArmor;
            maxMana = source.get("energy").getAsInt();
            currentMana = maxMana;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getHit(int damage) {
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

    @Override
    public void update(float deltaTime) {

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

        Room room = map.getRoomPlayerIn();
        fighting = false;
        if (room != null && room.getMonsterAlive().size() > 0) {
            fighting = true;
            room.setCombat(true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || true) {
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


}
