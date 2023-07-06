package com.mygdx.soulknight.entity.Effect;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.entity.Character.Monster.Monster;
import com.mygdx.soulknight.entity.Character.Player.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Map.DestroyableObject;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;
import java.util.Arrays;

public class Explosion extends RegionEffect {
    public final static Animation<TextureRegion> NORMAL_BOOM = new Animation<>(0.05f, SpriteLoader.to1DArray(SpriteLoader.splitTextureByFileName("explosion/explosion_4_5.png")));
    private float x;
    private float y;
    private float radius;
    private float stateTime;
    private boolean dealDame;
    private final static ArrayList<EffectEnum> effects = new ArrayList<>(Arrays. asList(EffectEnum.PUSH, EffectEnum.STUN));
    private ArrayList<DestroyableObject> destroyableObjectRemoveList = new ArrayList<>();
    private SimpleCharacter owner = null;
    private Animation<TextureRegion> explosionAnimation;
    private ArrayList<SimpleCharacter> allAffected = new ArrayList<>();
    public Explosion(SimpleCharacter owner, float x, float y, float radius, Animation<TextureRegion> explosionAnimation, boolean dealDame) {
        this.radius = radius;
        this.x = x;
        this.y = y;
//        animation
        this.explosionAnimation=explosionAnimation;
        stateTime = 0f;
        this.dealDame = dealDame;
        this.owner = owner;
        if (owner == null){
            if (Settings.explosionSound.isPlaying()){
                Settings.explosionSound.stop();
            }
            Settings.explosionSound.play();
        }
    }

    public Explosion(SimpleCharacter owner, float x, float y, float radius, boolean dealDame) {
        this(owner, x, y, radius, NORMAL_BOOM, dealDame);
    }

    @Override
    public void update(float deltaTime, WorldMap map) {
        stateTime += deltaTime;
        destroyableObjectRemoveList.clear();

        if (!dealDame) {return;}
        Vector2 centerPos = new Vector2(x, y);
        if (owner == null || owner instanceof Player) {
            for (Room room : map.getRooms()) {
                for (Monster monster : room.getMonsterAlive()) {
                    if (allAffected.contains(monster)) {
                        continue;
                    }
                    Vector2 centerMonster = new Vector2(monster.getX() + monster.getWidth()/2, monster.getY() + monster.getHeight()/2);
                    if (centerPos.dst(centerMonster) <= radius) {
                        allAffected.add(monster);
                        monster.getHit(2, DamageType.PHYSIC);
                        monster.addEffects(CharacterEffect.loadEffect(effects, centerMonster.sub(centerPos)));
                    }
                }
            }
        }
        if (owner == null || owner instanceof Monster) {
            Player player = map.getPlayer();
            if (allAffected.contains(player)) {
                return;
            }
            Vector2 playerPos = new Vector2(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2);
            if (centerPos.dst(playerPos) <= radius) {
                allAffected.add(player);
                player.getHit(2, DamageType.PHYSIC);
                player.addEffects(CharacterEffect.loadEffect(effects, playerPos.sub(centerPos)));
            }
        }

        for (DestroyableObject destroyableObject : map.getDestroyableObjects()) {
            if (centerPos.dst(destroyableObject.getX(), destroyableObject.getY()) <= radius) {
                destroyableObjectRemoveList.add(destroyableObject);
            }
        }
    }
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = this.explosionAnimation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame, this.x - radius, this.y - radius, this.radius * 2, this.radius * 2);
    }
    @Override
    public boolean isFinish() {
        return this.explosionAnimation.isAnimationFinished(stateTime);
    }

    public ArrayList<DestroyableObject> getDestroyableObjectRemoveList() {
        return destroyableObjectRemoveList;
    }
}
