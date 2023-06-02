package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.screen.MainGameScreen;

import java.util.ArrayList;

public class Room {
    private int numOfMonsters=4;
    private MapLayer layer;
    private ArrayList<Monster> monsterAlive = new ArrayList<>();
    private ArrayList<Monster> monsterDie = new ArrayList<>();
    private Player player;
    private boolean combat = false;
    public Room(MapLayer roomLayer, MainGameScreen gameScreen) {
        this.layer = roomLayer;
        player = gameScreen.getPlayer();
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        for (MapObject mapObject : gameScreen.getCollisionLayer().getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                rectangles.add(((RectangleMapObject) mapObject).getRectangle());
            }
        }
        for (MapObject mapObject : gameScreen.getDoorCollision().getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                rectangles.add(((RectangleMapObject) mapObject).getRectangle());
            }
        }

        for (int i = 0; i < numOfMonsters; i++) {

            for (MapObject mapObject : layer.getObjects()) {
                if (mapObject instanceof RectangleMapObject) {
                    Rectangle roomRectangle = ((RectangleMapObject) mapObject).getRectangle();

                    while (true) {
                        float x = MathUtils.random(roomRectangle.getX(), roomRectangle.getX() + roomRectangle.getWidth());
                        float y = MathUtils.random(roomRectangle.getY(), roomRectangle.getY() + roomRectangle.getHeight());
                        Monster monster = new Monster(gameScreen);
                        monster.setWeapon(new Gun(monster, "weapon/weapon.png", 2f, 120f));
                        monster.setPosition(x, y);

                        boolean noCollision = true;
                        for (Rectangle rectangle : rectangles) {
                            if (monster.getRectangle().overlaps(rectangle)) {
                                noCollision = false;
                                break;
                            }

                        }

                        for (Monster monster1 : monsterAlive) {
                            if (monster1.getRectangle().overlaps(monster.getRectangle())) {
                                noCollision = false;
                                break;
                            }
                        }

                        if (noCollision) {
                            monsterAlive.add(monster);
                            break;
                        }
                    }

                    break;
                }
            }
        }
    }

    public boolean isInRoom(Rectangle rectangle) {
//        if (monsterAlive.size() == 0) {
//            return false;
//        }
        for (MapObject mapObject : layer.getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                Rectangle roomRectangle = ((RectangleMapObject) mapObject).getRectangle();
                if (rectangle.overlaps(roomRectangle)) {
//                    System.out.println("Player in room with bottom-left coordinate: " + roomRectangle.getX() + " " + roomRectangle.getY());
                    return true;
                }
            }
        }
        return false;
    }



    public void draw(SpriteBatch batch) {
        for (Monster monster : monsterAlive) {
            monster.draw(batch);
            Weapon weapon = monster.getWeapon();
            weapon.draw(batch);
            if (weapon instanceof Gun) {
                for (Bullet bullet : ((Gun) weapon).getBulletArrayList()) {
                    bullet.draw(batch);
                }
            }
        }
        for (Monster monster : monsterDie) {
            Weapon weapon = monster.getWeapon();
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
                    continue;
                }
                // if monster position is N tiles far away from the player, call the update method
                float distance = (float) Math.sqrt(Math.pow(player.getX() - monster.getX(), 2) + Math.pow(player.getY() - monster.getY(),2));
                monster.update(deltaTime, player.getX(), player.getY());
//                monster.attack(new Vector2(player.getX(),player.getY()).sub(monster.getX(), monster.getY()).nor());

                Weapon weapon = monster.getWeapon();
                weapon.update(deltaTime);

                if (weapon instanceof Gun) {
                    for (Bullet bullet : ((Gun) weapon).getBulletArrayList()) {
                        bullet.update(deltaTime);
                    }
                }
            }
            monsterAlive.removeAll(monstersKilled);
            monsterDie.addAll(monstersKilled);
        } else {
            for (Monster monster : monsterAlive) {
                monster.setCurrentHP(monster.getMaxHP());
            }
        }

        for (Monster monster : monsterDie) {
            Weapon weapon = monster.getWeapon();
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
//
}
