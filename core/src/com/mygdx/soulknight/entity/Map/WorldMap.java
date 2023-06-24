package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;

import java.util.ArrayList;

public class WorldMap {
    private OrthographicCamera camera;
    public final static ArrayList<Explosion> EXPLOSION_ARRAY_LIST = new ArrayList<>();
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private MapLayer collisionLayer;
    private MapLayer doorCollision;
    private Player player;
    private ArrayList<Room> rooms;
    private ArrayList<Pickable> itemsOnGround = new ArrayList<>();
    private ArrayList<DestroyableObject> destroyableObjects = new ArrayList<>();
    private ArrayList<TiledMapTileMapObject> doorTiledMapObject = new ArrayList<>();
    public WorldMap(String tmxPath, Player player) {
        this.player = player;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (float) (Gdx.graphics.getWidth() / 1.5), (float) (Gdx.graphics.getHeight() / 1.5));
        tiledMap = new TmxMapLoader().load(tmxPath);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        collisionLayer = tiledMap.getLayers().get("collision_layer");
        doorCollision = tiledMap.getLayers().get("door_layer");

        MapLayer destroyableLayer = tiledMap.getLayers().get("destroyable_object");
        for (MapObject mapObject : destroyableLayer.getObjects()) {
            if (mapObject instanceof TiledMapTileMapObject) {
                destroyableObjects.add(new DestroyableObject((TiledMapTileMapObject) mapObject));
            }
        }

        for (MapObject mapObject : doorCollision.getObjects()) {
            if (mapObject instanceof TiledMapTileMapObject) {
                doorTiledMapObject.add((TiledMapTileMapObject) mapObject);
            }
        }
        
        MapGroupLayer roomLayers = (MapGroupLayer) tiledMap.getLayers().get("room");
        rooms = new ArrayList<>();

        int roomLayerCount = roomLayers.getLayers().size();
        for (int i = 0; i < roomLayerCount; i++) {
            MapLayer roomLayer = roomLayers.getLayers().get(i);
        	if (i == roomLayerCount - 1) {
        		rooms.add(new Room(roomLayer, this, 1));
        	}
        	else{
	            rooms.add(new Room(roomLayer, this, 0));
        	}
        }


//        temp
        Item item = Item.load("HP Stone");
        item.setPosition(50, 400);
        itemsOnGround.add(item);

        item = Item.load("Life Potion");
        item.setPosition(400, 50);
        itemsOnGround.add(item);

        item = Item.load("Mana Stone");
        item.setPosition(200, 50);
        itemsOnGround.add(item);

        item = Item.load("Mana Potion");
        item.setPosition(50, 200);
        itemsOnGround.add(item);

        Weapon weapon = Weapon.load("Gun Red M");
        weapon.setPosition(100, 100);
        weapon.setOnGround(true);
        itemsOnGround.add(weapon);

        weapon = Weapon.load("Gun Red XL");
        weapon.setPosition(250, 250);
        weapon.setOnGround(true);
        itemsOnGround.add(weapon);
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        ArrayList<Explosion> removeExplosionList=new ArrayList<>();
        for (Explosion explosion : EXPLOSION_ARRAY_LIST) {
            explosion.update(deltaTime);
            if (explosion.isExplosionFinish()){
                removeExplosionList.add(explosion);
            }
        }
        EXPLOSION_ARRAY_LIST.removeAll(removeExplosionList);
        for (Room room : rooms) {
            room.update(deltaTime);
        }
        for (Pickable item : itemsOnGround) {
            if (item instanceof Gun) {
                ((Gun) item).update(deltaTime);
            }
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
        for (DestroyableObject object : destroyableObjects) {
            object.draw(batch);
        }
        if (player.isFighting()) {
            for (TiledMapTileMapObject object : doorTiledMapObject) {
                batch.draw(object.getTextureRegion(), object.getX(), object.getY(), 16, 16);
            }
        }
        player.draw(batch);
        for (Room room : rooms) {
            room.draw(batch);
        }


        for (Pickable item : itemsOnGround) {
            item.draw(batch);
        }

        Pickable nearestItem = player.getNearestPickableInRange();
        if (nearestItem != null) {
            Texture texture = new Texture("ppp.png");
            float widthConsider = 8;
            float heightConsider = 8;
            batch.draw(texture, nearestItem.getX() + nearestItem.getWidth() / 2 - widthConsider / 2, nearestItem.getY() + nearestItem.getWidth() + 2, widthConsider, heightConsider);
        }

        for (Explosion explosion: EXPLOSION_ARRAY_LIST){
            explosion.draw(batch);
        }

        batch.end();
    }

    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }

    public boolean isMapCollision(Rectangle rectangle) {
        return isMapCollision(rectangle, true);
    }

    public boolean isMapCollision(Rectangle rectangle, boolean checkWithDestroyableObject) {
        if (checkWithDestroyableObject) {
            for (DestroyableObject object : destroyableObjects) {
                if (rectangle.overlaps(object.getRectangle())) {
                    return true;
                }
            }
        }

        for (MapObject mapObject : collisionLayer.getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                if (rectangle.overlaps(((RectangleMapObject) mapObject).getRectangle())) {
                    return true;
                }
            }
        }

//        In case player is fighting, player can not get out the room
        if (player.isFighting()) {
            if (isInDoor(rectangle)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInDoor(Rectangle rectangle) {
        for (MapObject mapObject : getDoorCollision().getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                if (rectangle.overlaps(((RectangleMapObject) mapObject).getRectangle())) {
                    return true;
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

    public void addWeaponOnGround(Weapon weapon) {
        itemsOnGround.add(weapon);
    }

    public ArrayList<Pickable> getItemsOnGround() {
        return itemsOnGround;
    }

    public boolean isOver() {
        if (player.getCurrentHP() <= 0) {
            return true;
        }
        for (Room room : rooms) {
            if (room.getMonsterAlive().size() > 0) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<DestroyableObject> getDestroyableObjects() {
        return destroyableObjects;
    }

    public void removeDestroyableObject(ArrayList<DestroyableObject> objects) {
        for (DestroyableObject object : objects) {
            removeDestroyableObject(object);
        }
    }

    public void removeDestroyableObject(DestroyableObject object) {
        if (destroyableObjects.contains(object)) {
            destroyableObjects.remove(object);
            if (object.getName().equals("ark")) {
                addRandomPotion(object.getX(), object.getY());
            }
        }
    }

    public void addRandomPotion(float x, float y) {
        int random = MathUtils.random(100);
        Item item = null;
        if (random < 2) {
            item = Item.load("Life Potion");
        } else if (random < 20) {
            item = Item.load("HP Stone");
        } else if (random < 50) {
            item = Item.load("Mana Potion");
        } else if (random <= 100) {
            item = Item.load("Mana Stone");
        }
        item.setPosition(x, y);
        itemsOnGround.add(item);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
