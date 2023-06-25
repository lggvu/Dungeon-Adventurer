package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.specialskill.Barrage;
import com.mygdx.soulknight.specialskill.SpecialSkill;
import com.mygdx.soulknight.util.SpriteLoader;

public class Boss extends Monster {
	private SpecialSkill skill;
	public Boss(String characterName, WorldMap map, Room room) {
		super(characterName, map,room);
	}

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        Barrage barrage = new Barrage(Boss.this, "bullet/bullet5.png", 5, 3f, 100f, 999999f);
        this.setSkill((SpecialSkill)barrage);
        return source;
    }

    private void setSkill(SpecialSkill skill) {
		this.skill = skill;
		
	}

	@Override
    public void update(float deltaTime) {
        getCurrentWeapon().update(deltaTime);
        float playerX = map.getPlayer().getX();
        float playerY = map.getPlayer().getY();
        float distance = (float) Math.sqrt(Math.pow(playerX - getX(), 2) + Math.pow(playerY - getY(), 2));
        if (distance <= this.attackRadius) {
            Vector2 direction = new Vector2(playerX - getX(), playerY - getY()).nor();
            if (direction.x != 0 || direction.y != 0) {
                move(direction.x, direction.y, deltaTime);
            }
            this.skill.activate();

        }
        else {
            // The monster will move randomly if the player is not in the attack radius
            for (int i = 0; i < 40; i++) {
                float testX = this.getX() + lastMoveDirection.x * speedRun * deltaTime;
                float testY = this.getY() + lastMoveDirection.y * speedRun * deltaTime;
                if (!map.isMapCollision(new Rectangle(testX, testY, width, height)) && (lastMoveDirection.x != 0 || lastMoveDirection.y != 0)) {
                    move(lastMoveDirection.x, lastMoveDirection.y, deltaTime);
                    break;
                }
                this.setSpeedRun(this.speedWhenIdle);
                lastMoveDirection = new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100)).nor();

            }
        }
        this.skill.update(deltaTime);
    }
    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        this.skill.draw(batch);
        
    }
    @Override
    public void attack(Vector2 direction) {
        
    }
    
    public SpecialSkill getSkill() {
    	return this.skill;
    }

	
}
