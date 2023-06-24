package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Boss;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

import java.util.ArrayList;

public class Room {
    private int numOfMonsters=4;
    private MapLayer layer;
    private ArrayList<Monster> monsterAlive = new ArrayList<>();
    private ArrayList<Monster> monsterDie = new ArrayList<>();
    private Player player;
    private WorldMap map;
    private ArrayList<Rectangle> roomArea;
    private boolean combat = false;
    public Room(MapLayer roomLayer, WorldMap map, int numBoss) {
        this.map = map;
        this.layer = roomLayer;
        player = map.getPlayer();
        roomArea = new ArrayList<>();
        for (MapObject mapObject : layer.getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                Rectangle roomRectangle = ((RectangleMapObject) mapObject).getRectangle();
                roomArea.add(roomRectangle);
            }
        }
        for (int i = 0; i < numOfMonsters; i++) {
            Rectangle rectangle = roomArea.get(MathUtils.random(0, roomArea.size() - 1));
            while (true) {
                float x = MathUtils.random(rectangle.getX(), rectangle.getX() + rectangle.getWidth());
                float y = MathUtils.random(rectangle.getY(), rectangle.getY() + rectangle.getHeight());
                Monster monster = new Monster("knight", map, this);
                monster.setPosition(x, y);
                if (map.isMapCollision(monster.getRectangle())) {
                    continue;
                }
                if (map.isInDoor(monster.getRectangle())) {
                    continue;
                }
                boolean overlap = false;
                for (Monster monster1 : monsterAlive) {
                    overlap = monster.getRectangle().overlaps(monster1.getRectangle());
                }
                if (overlap) {
                    continue;
                }
                monsterAlive.add(monster);
                break;
            }
        }
        for (int i = 0; i < numBoss; i++) {
            Rectangle rectangle = roomArea.get(MathUtils.random(0, roomArea.size() - 1));
            while (true) {
                float x = MathUtils.random(rectangle.getX(), rectangle.getX() + rectangle.getWidth());
                float y = MathUtils.random(rectangle.getY(), rectangle.getY() + rectangle.getHeight());
                Boss boss = new Boss("teacher", map,this);
                boss.setPosition(x, y);
                if (map.isMapCollision(boss.getRectangle())) {
                    continue;
                }
                if (map.isInDoor(boss.getRectangle())) {
                    continue;
                }
                boolean overlap = false;
                for (Monster monster1 : monsterAlive) {
                    overlap = boss.getRectangle().overlaps(monster1.getRectangle());
                }
                if (overlap) {
                    continue;
                }
                monsterAlive.add(boss);
                break;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Monster monster : monsterAlive) {
            monster.draw(batch);
        }

        for (Monster monster : monsterDie) {
            Weapon weapon = monster.getCurrentWeapon();
            if (weapon instanceof Gun) {
                for (Bullet bullet : ((Gun) weapon).getBulletArrayList()) {
                    bullet.draw(batch);
                }
            }
        }
    }

    public void update(float deltaTime) {
        if (combat) {
            ArrayList<Monster> monstersKilled = new ArrayList<>();
            for (Monster monster : monsterAlive) {
                if (monster.getCurrentHP() <= 0) {
                    monstersKilled.add(monster);
                    map.addRandomPotion(monster.getX(), monster.getY());
                    continue;
                }
                monster.update(deltaTime);
            }
            monsterAlive.removeAll(monstersKilled);
            monsterDie.addAll(monstersKilled);
        } else {
            for (Monster monster : monsterAlive) {
                monster.setCurrentHP(monster.getMaxHP());
            }
        }

        for (Monster monster : monsterDie) {
            Weapon weapon = monster.getCurrentWeapon();
            weapon.update(deltaTime);
            if (weapon instanceof Gun) {
                for (Bullet bullet : ((Gun) weapon).getBulletArrayList()) {
                    bullet.update(deltaTime);
                }
            }
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
