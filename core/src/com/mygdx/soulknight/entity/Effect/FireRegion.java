package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class FireRegion extends RegionEffect {
    private final static Animation<TextureRegion> ANIMATION = new Animation<>(0.15f, SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName("effect/zone/fire_1_4.png")));
    private float timeExistLeft = 5f;
    private float timeEffectAgain = 1f;
    private SimpleCharacter owner = null;
    private HashMap<SimpleCharacter, Float> affectedCharacter = new HashMap<>();
    private int damage = 1;
    private float x, y;
    private float radius = 60;
    private float stateTime = 0;
    public FireRegion(SimpleCharacter owner, float x, float y) {
        this.owner = owner;
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean isFinish() {
        return timeExistLeft <= 0;
    }

    @Override
    public void update(float deltaTime, WorldMap map) {
        timeExistLeft -= deltaTime;
        stateTime += deltaTime;
        ArrayList<SimpleCharacter> rmList = new ArrayList<>();
        for (SimpleCharacter character : affectedCharacter.keySet()) {
            Float newValue = affectedCharacter.get(character) - deltaTime;
            affectedCharacter.replace(character, newValue);
            if (newValue <= 0) {
                rmList.add(character);
            }
        }
        for (SimpleCharacter character : rmList) {
            affectedCharacter.remove(character);
        }
        Vector2 centerPos = new Vector2(x, y);
        if (owner == null || owner instanceof Player) {
            for (Room room : map.getRooms()) {
                for (Monster monster : room.getMonsterAlive()) {
                    if (affectedCharacter.containsKey(monster)) {
                        continue;
                    }
                    Vector2 centerMonster = new Vector2(monster.getX() + monster.getWidth()/2, monster.getY() + monster.getHeight()/2);
                    if (centerPos.dst(centerMonster) <= radius) {
                        affectedCharacter.put(monster, timeEffectAgain);
                        monster.getHit(damage, DamageType.FIRE);
                    }
                }
            }
        }
        if (owner == null || owner instanceof Monster) {
            Player player = map.getPlayer();
            if (affectedCharacter.containsKey(player)) {
                return;
            }
            Vector2 playerPos = new Vector2(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2);
            if (centerPos.dst(playerPos) <= radius) {
                affectedCharacter.put(player, timeEffectAgain);
                player.getHit(damage, DamageType.FIRE);
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, 0.3f);
        TextureRegion textureRegion = ANIMATION.getKeyFrame(stateTime, true);
        batch.draw(textureRegion, x - radius,y - radius, radius*2, radius*2);
        batch.setColor(color.r, color.g, color.b, 1f);
    }
}
