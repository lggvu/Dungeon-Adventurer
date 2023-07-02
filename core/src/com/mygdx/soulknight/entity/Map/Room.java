package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Boss;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

import java.util.ArrayList;
import java.util.Iterator;

public class Room {
    private ArrayList<Monster> monsterAlive = new ArrayList<>();
    private ArrayList<Monster> monsterDie = new ArrayList<>();
    private WorldMap map;
    private ArrayList<Rectangle> roomArea;
    private String roomName;
    private boolean combat = false;
    public Room(MapGroupLayer roomLayers, WorldMap map) {
        this.map = map;
        roomName = roomLayers.getName();
        MapLayer layer = roomLayers.getLayers().get("area");
        MapLayer monsterPositionLayer = roomLayers.getLayers().get(map.getLevel().name());
        roomArea = new ArrayList<>();
        for (MapObject mapObject : layer.getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                Rectangle roomRectangle = ((RectangleMapObject) mapObject).getRectangle();
                if (roomRectangle.height != 0 || roomRectangle.width != 0) {
                    roomArea.add(roomRectangle);
                } else {
                    if (mapObject.getName().equals("gate_position")) {
                        System.out.println("Gate position: " + roomRectangle.x + " " + roomRectangle.y);
                        map.setGateX(roomRectangle.x);
                        map.setGateY(roomRectangle.y);
                    } else if (mapObject.getName().equals("start_point")) {
                        System.out.println("Init position for player: " + roomRectangle.x + " " + roomRectangle.y);
                        map.getPlayer().setPosition(roomRectangle.x, roomRectangle.y);
                    }
                }
            }
        }

        if (monsterPositionLayer == null && roomName.equals("start_room")) {
            return;
        }

        for (MapObject mapObject : monsterPositionLayer.getObjects()) {
            Rectangle monsterPos = ((RectangleMapObject) mapObject).getRectangle();
            if (!mapObject.getProperties().containsKey("monster_type")) {
                Monster monster = new Monster(mapObject.getName(), map, this);
                monster.setPosition(monsterPos.x, monsterPos.y);
                monsterAlive.add(monster);
            } else {
                System.out.println("ADD BOSS SUCCESS");
                Boss boss = new Boss(mapObject.getName(), map,this);
                boss.setPosition(monsterPos.x, monsterPos.y);
                monsterAlive.add(boss);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Monster monster : monsterAlive) {
            monster.draw(batch);
        }

        for (Monster monster : monsterDie) {
            monster.draw(batch);
        }
    }

    public void update(float deltaTime) {
        if (roomName.equals("final_room") && monsterAlive.size() == 0) {
            map.setClearFinalRoom(true);
        }
        if (combat) {
            ArrayList<Monster> monstersKilled = new ArrayList<>();
            for (Monster monster : monsterAlive) {
                if (monster.getCurrentHP() <= 0) {
                    monstersKilled.add(monster);
                    map.getItemsOnGround().add(Item.getRandomItem(monster.getX(), monster.getY()));
                    continue;
                }
                monster.update(deltaTime);
            }
            monsterAlive.removeAll(monstersKilled);
            monsterDie.addAll(monstersKilled);
            for (Monster monster : monstersKilled) {
                monster.setDie();
            }
        } else {
            for (Monster monster : monsterAlive) {
                monster.setCurrentHP(monster.getCurrentMaxHP());
            }
        }

        for (Monster monster : monsterDie) {
            monster.update(deltaTime);
        }
    }
    public ArrayList<Monster> getMonsterAlive() {
        return monsterAlive;
    }

    public ArrayList<Monster> getMonsterDie() {
        return monsterDie;
    }

    public ArrayList<Monster> getAllMonster() {
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Monster monster : getMonsterAlive()) {
            monsters.add(monster);
        }
        for (Monster monster : getMonsterDie()) {
            monsters.add(monster);
        }
        return monsters;
    }

    public void setCombat(boolean combat) {
        this.combat = combat;
    }

    public ArrayList<Rectangle> getRoomArea() {
        return roomArea;
    }

    //
}
