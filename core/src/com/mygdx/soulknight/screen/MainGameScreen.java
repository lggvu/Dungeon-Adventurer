package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.SoulKnight;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Map.Room;
import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.WeaponLoader;

import java.util.ArrayList;

public class MainGameScreen extends ScreenAdapter {
    SoulKnight game;
    private WorldMap map;
    private Player player;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    public MainGameScreen(SoulKnight game) {
        this.game = game;
        player = new Player("king", null);
        map = new WorldMap("split_map/tmx/maps.tmx", player);
        player.setMap(map);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.update(deltaTime);
        map.draw(game.getBatch());

//        Vector2 moveDirection = new Vector2(0, 0);
//
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
//            moveDirection = moveDirection.add(-1, 0);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
//            moveDirection = moveDirection.add(1, 0);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
//            moveDirection = moveDirection.add(0, 1);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
//            moveDirection = moveDirection.add(0, -1);
//        }
//
//        if (moveDirection.x != 0 || moveDirection.y != 0) {
//            moveDirection = moveDirection.nor();
//            player.move(moveDirection.x, moveDirection.y);
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
//            player.switchWeapon();
//        }
//
//        //        Update screen
//        player.update(deltaTime);
//
//        isCombat = false;
//        Room roomPlayerIn = null;
//        if (!isPlayerInDoor(player.getRectangle())) {
//            for (Room room : rooms) {
//                if (room.isInRoom(player.getRectangle()) && room.getMonsterAlive().size() > 0) {
//                    roomPlayerIn = room;
//                    isCombat = true;
//                    room.setCombat(true);
//                }
//                room.update(deltaTime);
//            }
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && true) {
//            if (roomPlayerIn != null) {
//                float minDst = Float.MAX_VALUE;
//                Vector2 minPos = null;
//                for (Monster monster : roomPlayerIn.getMonsterAlive()) {
//                    Vector2 monsterPos = new Vector2(monster.getX(), monster.getY());
//                    float dst = monsterPos.dst(new Vector2(player.getX(), player.getY()));
////                    System.out.println(dst);
//                    if (dst < minDst) {
//                        minDst = dst;
//                        minPos = monsterPos;
//                    }
//                }
////                System.out.println(minDst);
//                if (minDst <= player.getCurrentWeapon().getRangeWeapon()) {
//                    player.attack(minPos.sub(player.getX(), player.getY()).nor());
//                } else {
//                    player.attack(player.getLastMoveDirection());
//                }
//            } else {
//                player.attack(player.getLastMoveDirection());
//            }
//        }
//
//        handleBulletCollision();
//
//        boolean noMonsterLeft = true;
//        for (Room room : rooms) {
//            if (room.getMonsterAlive().size() > 0) {
//                noMonsterLeft = false;
//            }
//        }
//        if (noMonsterLeft || player.getCurrentHP() <= 0) {
//            game.setScreen(new MenuScreen(game));
//            game.resetBatch();
//            this.dispose();
//            return;
//        }
//
//        camera.position.x = player.getX() + player.getWidth() / 2;
//        camera.position.y = player.getY() + player.getHeight() / 2;
//
//        camera.update();
//        mapRenderer.setView(camera);
//        mapRenderer.render();
//        game.getBatch().setProjectionMatrix(camera.combined);
//
//        game.getBatch().begin();
//
//        player.draw(game.getBatch());
//
//        for (Room room : rooms) {
//            room.draw(game.getBatch());
//        }
//
//        game.getBatch().end();
        drawHealthBar();
//        if (!noMonsterLeft) {
//            drawHealthBar();
//        }
    }
    @Override
    public void dispose() {
        map.dispose();
//        tiledMap.dispose();
//        mapRenderer.dispose();
    }


//    public void handleBulletCollision() {
//        ArrayList<Bullet> removeBulletList;
//        ArrayList<Monster> removeMonsterList;
//
//        for (Room room : rooms) {
//            for (Monster monster : room.getAllMonster()) {
//                Weapon monsterWeapon = monster.getCurrentWeapon();
//                if (monsterWeapon instanceof Gun) {
//                    Gun gun = (Gun) monsterWeapon;
//                    removeBulletList = new ArrayList<>();
//                    for (Bullet bullet : gun.getBulletArrayList()) {
//                        if (isMapCollision(bullet.getRectangle())) {
//                            removeBulletList.add(bullet);
//                        } else if (bullet.getRectangle().overlaps(player.getRectangle())) {
//                            player.getHit(bullet.getDmg());
//                            removeBulletList.add(bullet);
//                        }
//                    }
//                    gun.getBulletArrayList().removeAll(removeBulletList);
//                }
//
//
//            }
//        }
//
//        for (Weapon playerWeapon : player.getWeapons()) {
//            if (playerWeapon instanceof Gun) {
//                Gun gun = (Gun) playerWeapon;
//                removeBulletList = new ArrayList<>();
//                for (Bullet bullet : gun.getBulletArrayList()) {
//                    for (Room room : rooms) {
//                        for (Monster monster : room.getMonsterAlive()) {
//                            if (isMapCollision(bullet.getRectangle())) {
//                                removeBulletList.add(bullet);
//                            }else if (monster.getRectangle().overlaps(bullet.getRectangle())) {
//                                removeBulletList.add(bullet);
//                                monster.getHit(bullet.getDmg());
//                            }
//                        }
//                    }
//                }
//                gun.getBulletArrayList().removeAll(removeBulletList);
//            }
//        }
//    }
//
//    public MapLayer getCollisionLayer() {
//        return collisionLayer;
//    }
//
//    public MapLayer getDoorCollision() {
//        return doorCollision;
//    }
//
//    public Player getPlayer() {
//        return player;
//    }
//
    private void drawHealthBar() {

        float barWidth = 200;
        float barHeight = 20;
        float boardWidth = 250;
        float boardHeight = 80;

        float barX = 25;

        float barHPY = Gdx.graphics.getHeight() - 25;
        float barManaY = Gdx.graphics.getHeight() - 50;
        float barArmorY = Gdx.graphics.getHeight() - 75;
        float boardY = Gdx.graphics.getHeight() - boardHeight;

        // Calculate the width of current health
        float healthBarWidth = barWidth * ((float) player.getCurrentHP() / player.getMaxHP());
        float armorBarWidth = barWidth * ((float) player.getCurrentArmor() / player.getMaxArmor());
        float manaBarWidth = barWidth * ((float) player.getCurrentMana() / player.getMaxMana());

        // Start the shape rendering
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Set the color for the filled portion of the health bar
        shapeRenderer.setColor(new Color(234/255f,182/255f,118/255f,1));
        shapeRenderer.rect(0, boardY, boardWidth, boardHeight);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX, barHPY, barWidth, barHeight);
        shapeRenderer.rect(barX, barArmorY, barWidth, barHeight);
        shapeRenderer.rect(barX, barManaY, barWidth, barHeight);

        // Set the color for the outline of the health bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barHPY, healthBarWidth, barHeight);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(barX, barManaY, manaBarWidth, barHeight);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(barX, barArmorY, armorBarWidth, barHeight);

        shapeRenderer.end();
    }
//
//
}
