package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Effect.Explosion;
import com.mygdx.soulknight.entity.Effect.RegionEffect;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;

public class WorldMap {
    private OrthographicCamera camera;
    private ArrayList<RegionEffect> regionEffectArrayList = new ArrayList<>();
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private MapLayer collisionLayer;
    private MapLayer doorCollision;
    private Animation<TextureRegion> teleGate;
    private Player player;
    private ArrayList<Room> rooms;
    private float gateX, gateY;
    private ArrayList<Pickable> itemsOnGround = new ArrayList<>();
    private ArrayList<DestroyableObject> destroyableObjects = new ArrayList<>();
    private ArrayList<TiledMapTileMapObject> doorTiledMapObject = new ArrayList<>();
    private float stateTime = 0;
    private boolean clearFinalRoom = false;
    private boolean isOver = false;
    public WorldMap(String tmxPath, Player player) {
        this.player = player;
        teleGate = new Animation<>(0.15f, SpriteLoader.loadTextureByFileName("tele_4_4.png"));
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
            if (!roomLayer.getName().equals("start_room")) {
                if (roomLayer.getName().equals("final_room")) {
                    rooms.add(new Room(roomLayer, this, 1));
                } else{
                    rooms.add(new Room(roomLayer, this, 0));
                }
            } else {
                boolean foundStartPoint = false;
                for (MapObject object : roomLayer.getObjects()) {
                    String name = object.getName();
                    if (name != null && name.equals("start_point") && object instanceof RectangleMapObject) {
                        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                        player.setPosition(rectangle.x, rectangle.y);
                        foundStartPoint = true;
                    }
                }
                if (!foundStartPoint) {
                    System.out.println("Start room must specific start point");
                    System.exit(0);
                }
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
        for (Room room : rooms) {
            room.update(deltaTime);
        }

        ArrayList<RegionEffect> removeRegionEffectList = new ArrayList<>();
        ArrayList<DestroyableObject> rmObject = new ArrayList<>();

        for (RegionEffect regionEffect : regionEffectArrayList) {
            regionEffect.update(deltaTime, this);
            if (regionEffect instanceof Explosion) {
                rmObject.addAll(((Explosion) regionEffect).getDestroyableObjectRemoveList());
            }
            if (regionEffect.isFinish()){
                removeRegionEffectList.add(regionEffect);
            }
        }
        
        removeDestroyableObject(rmObject);
        regionEffectArrayList.removeAll(removeRegionEffectList);
        for (Pickable item : itemsOnGround) {
            if (item instanceof Gun) {
                ((Gun) item).update(deltaTime);
            }
        }
        if (clearFinalRoom) {
            stateTime += deltaTime;
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

        for (RegionEffect regionEffect: regionEffectArrayList){
            regionEffect.draw(batch);
        }

        if (clearFinalRoom) {
            TextureRegion texture = teleGate.getKeyFrame(stateTime, true);
            float teleSize = 156;
            batch.draw(texture, gateX-teleSize/2, gateY-teleSize/2, teleSize, teleSize);
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
            isOver = true;
        }
        return isOver;
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
            } else if (object.getName().equals("boom")) {
                createAnExplosion(null, object.getX(), object.getY(),128,true);
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

    public void setClearFinalRoom(boolean clearFinalRoom) {
        this.clearFinalRoom = clearFinalRoom;
    }

    public boolean isClearFinalRoom() {
        return clearFinalRoom;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public float getGateX() {
        return gateX;
    }

    public float getGateY() {
        return gateY;
    }

    public void setGateX(float gateX) {
        this.gateX = gateX;
    }

    public void setGateY(float gateY) {
        this.gateY = gateY;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public void createAnExplosion(SimpleCharacter owner, float x, float y, float radius, Animation<TextureRegion> animation, boolean dealDame) {
        regionEffectArrayList.add(new Explosion(owner, x, y, radius, animation, dealDame));
    }

    public void createAnExplosion(SimpleCharacter owner, float x, float y, float radius, boolean dealDame) {
        regionEffectArrayList.add(new Explosion(owner, x, y, radius, dealDame));
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public void addRegionEffect(RegionEffect regionEffect) {
        regionEffectArrayList.add(regionEffect);
    }
    public void addRegionEffect(ArrayList<RegionEffect> regionEffects) {
        regionEffectArrayList.addAll(regionEffects);
    }
}
