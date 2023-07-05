package com.mygdx.soulknight.entity.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.entity.Character.Monster;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Effect.*;
import com.mygdx.soulknight.entity.Item.Item;
import com.mygdx.soulknight.entity.Item.Pickable;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.FontDrawer;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;

public class WorldMap {
    private OrthographicCamera camera;
    private ArrayList<RegionEffect> regionEffectArrayList = new ArrayList<>();
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private ArrayList<Rectangle> collisionRect = new ArrayList<>();
    private ArrayList<Rectangle> doorCollisionRect = new ArrayList<>();
//    private Animation<TextureRegion> teleGate;
    private TextureRegion[][] teleGateTextureRegions;
    private Player player;
    private ArrayList<Room> rooms;
    private float gateX, gateY;
    private ArrayList<Pickable> itemsOnGround = new ArrayList<>();
    private ArrayList<DestroyableObject> destroyableObjects = new ArrayList<>();
    private ArrayList<TiledMapTileMapObject> doorTiledMapObject = new ArrayList<>();
    private float stateTime = 0;
    private boolean clearFinalRoom = false;
    private boolean isOver = false;
    private Level level;
    private ArrayList<FontDrawer> fontDrawers = new ArrayList<>();

    public WorldMap(String tmxPath, Player player, Level level) {
        this.level = level;
        this.player = player;
        teleGateTextureRegions = SpriteLoader.splitTextureByFileName("tele_4_4.png");
//        teleGate = new Animation<>(0.15f, );
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (float) (Gdx.graphics.getWidth() / 1.5), (float) (Gdx.graphics.getHeight() / 1.5));
        tiledMap = new TmxMapLoader().load(tmxPath);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        MapLayer collisionLayer = tiledMap.getLayers().get("collision_layer");
        MapLayer doorCollision = tiledMap.getLayers().get("door_layer");
        MapLayer destroyableLayer = tiledMap.getLayers().get("destroyable_object");

        for (MapObject mapObject : collisionLayer.getObjects()) {
            if (mapObject instanceof RectangleMapObject) {
                collisionRect.add(((RectangleMapObject) mapObject).getRectangle());
            }
        }
        for (MapObject mapObject : destroyableLayer.getObjects()) {
            if (mapObject instanceof TiledMapTileMapObject) {
                destroyableObjects.add(new DestroyableObject((TiledMapTileMapObject) mapObject));
            }
        }

        for (MapObject mapObject : doorCollision.getObjects()) {
            if (mapObject instanceof TiledMapTileMapObject) {
                doorTiledMapObject.add((TiledMapTileMapObject) mapObject);
            } else if (mapObject instanceof RectangleMapObject) {
                doorCollisionRect.add(((RectangleMapObject) mapObject).getRectangle());
            }
        }
        
        MapGroupLayer roomLayers = (MapGroupLayer) tiledMap.getLayers().get("room");
        rooms = new ArrayList<>();

        int roomLayerCount = roomLayers.getLayers().size();
        for (int i = 0; i < roomLayerCount; i++) {
            MapGroupLayer groupLayer = (MapGroupLayer) roomLayers.getLayers().get(i);
            rooms.add(new Room(groupLayer, this));
        }

//        temp
        Item item = new Item(Item.ItemEnum.HP_STONE, 50, 400);
        itemsOnGround.add(item);

        item = new Item(Item.ItemEnum.LIFE_POTION,400, 50);
        itemsOnGround.add(item);

        item = new Item(Item.ItemEnum.MANA_STONE,200, 50);
        itemsOnGround.add(item);

        item = new Item(Item.ItemEnum.MANA_POTION,50, 200);
        itemsOnGround.add(item);

        Weapon weapon = Weapon.load("Gun Red M");
        weapon.setPosition(100, 100);
        weapon.setOnGround(true);
        itemsOnGround.add(weapon);

        weapon = Weapon.load("Gun Red XL");
        weapon.setPosition(250, 250);
        weapon.setOnGround(true);
        itemsOnGround.add(weapon);

        weapon = Weapon.load("Gun Red S");
        weapon.setPosition(110, 100);
        weapon.setOnGround(true);
        itemsOnGround.add(weapon);

        weapon = Weapon.load("Gun Red L");
        weapon.setPosition(190, 250);
        weapon.setOnGround(true);
        itemsOnGround.add(weapon);

    }

    public void addDamageNumber(int damage, DamageType damageType, boolean isCrit, float x, float y) {
        if (isCrit) {
            fontDrawers.add(new FontDrawer(Integer.toString(damage), Color.YELLOW, x, y));
            return;
        }
        switch (damageType) {
            case FIRE:
                fontDrawers.add(new FontDrawer(Integer.toString(damage), Color.YELLOW, x, y));
                return;
            case PHYSIC:
                fontDrawers.add(new FontDrawer(Integer.toString(damage), Color.BLACK, x, y));
                return;
            case POISON:
                fontDrawers.add(new FontDrawer(Integer.toString(damage), Color.GREEN, x, y));
                return;
            case LIGHTNING:
                fontDrawers.add(new FontDrawer(Integer.toString(damage), Color.BLUE, x, y));
        }
    }

    public Level getLevel() {
        return level;
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        if (!player.isAlive()) {
            player.activateDying();
            if (player.isFinishDying()) {
                setOver(true);
            }
            return;
        }
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

        ArrayList<FontDrawer> rmFont = new ArrayList<>();
        for (FontDrawer fontDrawer : fontDrawers) {
            fontDrawer.update(deltaTime);
            if (fontDrawer.isFinished()) {
                rmFont.add(fontDrawer);
                fontDrawer.dispose();
            }
        }
        fontDrawers.removeAll(rmFont);
    }

    public void draw(SpriteBatch batch) {
        camera.position.x = player.getX() + player.getWidth() / 2;
        camera.position.y = player.getY() + player.getHeight() / 2;
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (RegionEffect regionEffect: regionEffectArrayList){
            if (!(regionEffect instanceof Explosion)) {
                regionEffect.draw(batch);

            }
        }

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
            if (regionEffect instanceof Explosion) {
                regionEffect.draw(batch);
            }
        }

        for (FontDrawer fontDrawer : fontDrawers) {
            fontDrawer.draw(batch);
        }

        if (clearFinalRoom) {
            float teleFrameDuration = 0.15f;
            float teleSize = 256;
            Animation<TextureRegion> teleGate = new Animation<>(teleFrameDuration, teleGateTextureRegions[0]);
            if (teleGate.isAnimationFinished(stateTime)) {
                TextureRegion[] temp = new TextureRegion[] {
                        teleGateTextureRegions[1][0],
                        teleGateTextureRegions[1][1],
                        teleGateTextureRegions[1][2],
                        teleGateTextureRegions[1][3],
                        teleGateTextureRegions[2][0],
                        teleGateTextureRegions[2][1],
                        teleGateTextureRegions[2][2],
                        teleGateTextureRegions[2][3]
                };
                teleGate = new Animation<>(teleFrameDuration, temp);
            }
            TextureRegion texture = teleGate.getKeyFrame(stateTime, true);
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

        for (Rectangle rectangle1 : collisionRect) {
            if (rectangle.overlaps(rectangle1)) {
                return true;
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
        for (Rectangle rectangle1 : doorCollisionRect) {
            if (rectangle.overlaps(rectangle1)) {
                return true;
            }
        }
        return false;
    }

    public Room getRoomPlayerIn() {
        if (isInDoor(player.getRectangle())) {
            return null;
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
            if (object.getName().equals("ARK") || object.getName().equals("ARK_WEAPON")) {
                itemsOnGround.add(object.getPickableObject());
            } else if (!object.getName().equals("")) {
                RegionEffect.loadRegionEffect(null, this, EffectEnum.valueOf(object.getName()), object.getX(), object.getY());
            }
        }
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

    public ArrayList<SimpleCharacter> getAllCharacter() {
        ArrayList<SimpleCharacter> res = new ArrayList<>();
        res.add(player);
        for (Room room : getRooms()) {
            for (Monster monster : room.getMonsterAlive()) {
                res.add(monster);
            }
        }
        return res;
    }

    public ArrayList<Rectangle> getCollisionRect() {
        return collisionRect;
    }
}
