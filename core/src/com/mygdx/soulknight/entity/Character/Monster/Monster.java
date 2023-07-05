package com.mygdx.soulknight.entity.Character.Monster;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

public class Monster extends SimpleCharacter {
    protected float attackRadius = 200;
    float speedWhenIdle; // The speed that monster will move when cannot approach the player
    float speedInRangeAttack;
    private Animation<TextureInfo> attackAnimation;
    protected AttackSkill attackSkill;
    private Room room;
    public Monster(String characterName, WorldMap map, Room room) {
        super(characterName, map);
        for (Weapon weapon : weapons) {
            weapon.setDrawWeapon(false);
        }
        this.room = room;
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        speedInRangeAttack = speedRun;
        speedWhenIdle = speedInRangeAttack / 2;
        TextureInfo[] temp = SpriteLoader.loadTextureInfo(source.get("attack_texture_path").getAsJsonArray());
        float frameDuration = 0.15f;
        attackAnimation = new Animation<>(frameDuration, temp);
        attackSkill = new AttackSkill(null, getCurrentWeapon().getTotalTimeCoolDown(),frameDuration * (temp.length - 1));
        return source;
    }

    protected class AttackSkill extends Skill {
        private Vector2 attackDirection;
        public AttackSkill(TextureRegion textureRegion, float totalTimeCoolDown, float totalTimeImplement) {
            super(textureRegion, totalTimeCoolDown, totalTimeImplement);
        }
        @Override
        public void activateSkill() {
            super.activateSkill();
            stateTime = 0;
            Animation<TextureInfo> temp = animationMovement;
            animationMovement = attackAnimation;
            attackAnimation = temp;
        }
        @Override
        public void deactivateSkill() {
            super.deactivateSkill();
            stateTime = 0;
            Animation<TextureInfo> temp = animationMovement;
            animationMovement = attackAnimation;
            attackAnimation = temp;
            attack(attackDirection);
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            stateTime += deltaTime;
        }

        public void setTotalTimeCoolDown(float timeCoolDown) {
            totalTimeCoolDown = timeCoolDown;
        }

        public void setCurrentTimeCoolDown(float timeCoolDown) {
            currentTimeCoolDown = timeCoolDown;
        }

        public void setAttackDirection(Vector2 direction) {
            attackDirection = direction;
        }
    }

    @Override
    public void update(float deltaTime) {
        for (Weapon weapon : weapons) {
            weapon.update(deltaTime);
        }
        attackSkill.update(deltaTime);

        if (!isAlive()) {
            stateTime += deltaTime;
            return;
        }

        if (attackSkill.isInProgresss()) {
            return;
        }

        applyEffect(deltaTime);
        if (isStunned) {
            return;
        }

        float playerX = map.getPlayer().getX();
        float playerY = map.getPlayer().getY();
        float distance = (float) Math.sqrt(Math.pow(playerX - getX(), 2) + Math.pow(playerY - getY(), 2));
        if (distance <= this.attackRadius && map.getPlayer().isInVision(this)) {
            setSpeedRun(speedInRangeAttack);
            Vector2 direction = new Vector2(playerX - getX(), playerY - getY()).nor();
            if (direction.x != 0 || direction.y != 0) {
                move(direction.x, direction.y, deltaTime);
                setLookAtDirection(lastMoveDirection.x, lastMoveDirection.y);
                updateMovementAnimation(deltaTime);
            }
            if (!attackSkill.isInProgresss() && !attackSkill.isCoolingDown()) {
                attackSkill.setAttackDirection(direction);
                attackSkill.activateSkill();
            }
        }
        else {
            // The monster will move randomly if the player is not in the attack radius
            setSpeedRun(speedWhenIdle);
            float testX = this.getX() + lastMoveDirection.x * speedRun * deltaTime;
            float testY = this.getY() + lastMoveDirection.y * speedRun * deltaTime;
            if (!map.isMapCollision(new Rectangle(testX, testY, this.maxWidth, this.maxHeight)) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                move(lastMoveDirection.x, lastMoveDirection.y, deltaTime);
                setLookAtDirection(lastMoveDirection.x, lastMoveDirection.y);
                updateMovementAnimation(deltaTime);
            } else {
                float x = MathUtils.random(-100, 100);
                float y = MathUtils.random(-100, 100);
                if (x != 0 || y != 0) {
                    lastMoveDirection = new Vector2(x, y).nor();
                }
            }
        }
    }
    @Override
    public void getHit(int damage, DamageType damageType, boolean isCrit) {
        getMap().addDamageNumber(damage, damageType, isCrit, x, y);
        currentHP -= damage;
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        for (Weapon weapon : weapons) {
            weapon.draw(batch);
        }
    }
    public Room getRoom(){
        return this.room;
    }
}
