package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

import java.util.ArrayList;

public class WorldMap {
    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private MapLayer collisionLayer;
    private MapLayer doorCollision;
    private Player player;
    private ArrayList<Room> rooms;
    public WorldMap(String tmxPath, Player player) {
        this.player = player;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (float) (Gdx.graphics.getWidth() / 1.5), (float) (Gdx.graphics.getHeight() / 1.5));
        tiledMap = new TmxMapLoader().load(tmxPath);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        collisionLayer = tiledMap.getLayers().get("collision_layer");
        doorCollision = tiledMap.getLayers().get("door_layer");
        MapGroupLayer roomLayers = (MapGroupLayer) tiledMap.getLayers().get("room");
        rooms = new ArrayList<>();
        for (MapLayer roomLayer : roomLayers.getLayers()) {
            rooms.add(new Room(roomLayer,this));
        }
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        for (Room room : rooms) {
            room.update(deltaTime);
        }
    }

    public void draw(SpriteBatch batch) {
        camera.position.x = player.getX() + player.getWidth() / 2;
        camera.position.y = player.getY() + player.getHeight() / 2;
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        player.draw(batch);
        for (Room room : rooms) {
            room.draw(batch);
        }
        batch.end();
    }

    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }

    public boolean isMapCollision(Rectangle rectangle) {
        for (MapObject mapObject : collisionLayer.getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                if (rectangle.overlaps(((RectangleMapObject) mapObject).getRectangle())) {
                    return true;
                }
            }
        }

//        In case player is fighting, player can not get out the room
        if (player.isFighting()) {
            for (MapObject mapObject : getDoorCollision().getObjects()) {
                if (mapObject instanceof RectangleMapObject) {
                    if (rectangle.overlaps(((RectangleMapObject) mapObject).getRectangle())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Room getRoomPlayerIn() {
        for (MapObject mapObject : getDoorCollision().getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                if (player.getRectangle().overlaps(((RectangleMapObject) mapObject).getRectangle())) {
                    return null;
                }
            }
        }

        for (Room room : rooms) {
            for (Rectangle rect : room.getRoomArea()) {
                if (player.getRectangle().overlaps(rect)) {
                    return room;
                }
            }
        }
        return null;
    }

    public MapLayer getCollisionLayer() {
        return collisionLayer;
    }

    public MapLayer getDoorCollision() {
        return doorCollision;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
