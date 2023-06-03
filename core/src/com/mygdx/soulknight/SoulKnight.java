package com.mygdx.soulknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.screen.MainGameScreen;


import java.util.ArrayList;

public class SoulKnight extends Game {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private Player player;
    private ArrayList<Character> monsterList;

    @Override
    public void create () {
//
//        monsterList = new ArrayList<>();
//
//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, (float) (Gdx.graphics.getWidth() / 1.5), (float) (Gdx.graphics.getHeight() / 1.5));
//
//        // Load the map
//        tiledMap = new TmxMapLoader().load("split_map/tmx/maps.tmx");
//        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
//        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("object_tile");
//
//        // Load the character texture and position
////        character = new Character(new Texture("bucket.png"), tiledMap,"Collisions");
//
//        player = new Player(this,"character/img2.png", "young");
//
//        Weapon weapon = new Weapon(null, this);
//        player.addWeapon(weapon);
//        player.setSize(16, 32);
//
//
//        ArrayList<String> names = SpriteLoader.getCharacterNameFromImage("character/img1.png");
//        for (int i = 0; i < 10; i++) {
//            int random = MathUtils.random(names.size() - 1);
//            Monster monster = new Monster(this, "character/img1.png", names.get(random));
//            Weapon monsterWeapon = new Weapon(null, this);
//            monster.addWeapon(monsterWeapon);
//            monsterList.add(monster);
//            monster.setSize(16, 32);
//        }
        // Set up the sprite batch
        batch = new SpriteBatch();
        this.setScreen(new MainGameScreen(this));
    }

    @Override
    public void render () {
        super.render();
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        float deltaTime = Gdx.graphics.getDeltaTime();
//
//        boolean keyPress = false;
//        Vector2 vectorMove = new Vector2(0,0);
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
//            vectorMove.add(-1, 0);
//            keyPress = true;
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
////            player.move(new Vector2(1,0), deltaTime);
//            vectorMove.add(1, 0);
//            keyPress = true;
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
////            player.move(new Vector2(0,1), deltaTime);
//            vectorMove.add(0,1);
//            keyPress = true;
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
////            player.move(new Vector2(0,-1), deltaTime);
//            vectorMove.add(0, -1);
//            keyPress = true;
//        }
//
//        if (keyPress) {
//            player.move(vectorMove.nor(), deltaTime);
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
//            player.attack();
//        }
//
//        camera.position.x = player.getX() + player.getWidth() /2;
//        camera.position.y = player.getY() + player.getHeight() / 2;
//        camera.update();
//        mapRenderer.setView(camera);
//        mapRenderer.render();
//
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//
////        Update charecter position
//        player.update(deltaTime);
//        for (Character monster : monsterList) {
//            monster.update(deltaTime);
//        }
//
//        handleBulletCollision(Player.BULLET_ARRAY_LIST, monsterList, deltaTime);
//        ArrayList<Character> playerArrayList = new ArrayList<>();
//        playerArrayList.add(player);
//        handleBulletCollision(Monster.BULLET_ARRAY_LIST, playerArrayList, deltaTime);
//
////        Remove all monster has HP < 0
//        ArrayList<Character> monsterRemove = new ArrayList<>();
//        for (Character monster : monsterList) {
//            Monster temp = (Monster) monster;
//            if (temp.getHP() <= 0) {
//                monsterRemove.add(monster);
//            }
//        }
//        monsterList.removeAll(monsterRemove);
//
//        for (Character monster : monsterList) {
//            monster.attack();
//            monster.render();
//        }
//
//        for (Bullet bullet : Player.BULLET_ARRAY_LIST) {
//            bullet.render();
//        }
//
//        for (Bullet bullet : Monster.BULLET_ARRAY_LIST) {
//            bullet.render();
//        }
//
//        player.render();
//        batch.end();
    }

    public ArrayList<Character> getMonsterList() {
        return monsterList;
    }

    @Override
    public void dispose () {

    }

//    public void handleBulletCollision(ArrayList<Bullet> bulletArrayList, ArrayList<Character> characterArrayList, float deltaTime) {
//        //        Check bullet shot from player
//        ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
//        for (Bullet bullet : bulletArrayList) {
//            bullet.update(deltaTime);
////           If true, remove that bullet
//            if (bullet.handleCollideWithWall()) {
//                bulletsRemove.add(bullet);
//            }
////            If collide with monster
//            for (Character character : characterArrayList) {
//                if (bullet.getBoundingRectangle().overlaps(character.getBoundingRectangle())) {
//                    character.getHit(bullet.getDmg());
//                    bulletsRemove.add(bullet);
//                    break;
//                }
//            }
//        }
//        bulletArrayList.removeAll(bulletsRemove);
//    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public Player getPlayer() {
        return player;
    }
    public void resetBatch() {
        batch.dispose();
        batch = new SpriteBatch();
    }
}
