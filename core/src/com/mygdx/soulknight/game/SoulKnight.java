package com.mygdx.soulknight.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.soulknight.game.entity.*;
import com.mygdx.soulknight.game.entity.Character;

import java.util.ArrayList;

public class SoulKnight extends ApplicationAdapter {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private Player player;
    private ArrayList<Character> monsterList;

    @Override
    public void create () {
        monsterList = new ArrayList<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Load the map
        tiledMap = new TmxMapLoader().load("second_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collisions");

        // Load the character texture and position
//        character = new Character(new Texture("bucket.png"), tiledMap,"Collisions");
        player = new Player(new Texture("bucket.png"), this);
        Weapon weapon = new Weapon(null, this);
        player.addWeapon(weapon);
        player.setSize(32, 32);

        for (int i = 0; i < 10; i++) {
            Monster monster = new Monster(new Texture("bucket.png"), this);
            Weapon monstorWeapon = new Weapon(null, this);
            monster.addWeapon(monstorWeapon);
            monsterList.add(monster);
            monster.setSize(32, 32);
        }
        // Set up the sprite batch
        batch = new SpriteBatch();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.move(new Vector2(-1,0), deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.move(new Vector2(1,0), deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.move(new Vector2(0,1), deltaTime);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.move(new Vector2(0,-1), deltaTime);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.attack();
        }

        camera.position.x = player.getX() + player.getWidth() /2;
        camera.position.y = player.getY() + player.getHeight() / 2;
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

//        Update charecter position
        player.update(deltaTime);
        for (Character monster : monsterList) {
            monster.update(deltaTime);
        }

        handleBulletCollision(Player.BULLET_ARRAY_LIST, monsterList, deltaTime);
        ArrayList<Character> playerArrayList = new ArrayList<>();
        playerArrayList.add(player);
        handleBulletCollision(Monster.BULLET_ARRAY_LIST, playerArrayList, deltaTime);

//        Remove all monster has HP < 0
        ArrayList<Character> monsterRemove = new ArrayList<>();
        for (Character monster : monsterList) {
            Monster temp = (Monster) monster;
            if (temp.getHP() <= 0) {
                monsterRemove.add(monster);
            }
        }
        monsterList.removeAll(monsterRemove);

        for (Character monster : monsterList) {
            monster.attack();
            monster.render();
        }

        for (Bullet bullet : Player.BULLET_ARRAY_LIST) {
            bullet.render();
        }

        for (Bullet bullet : Monster.BULLET_ARRAY_LIST) {
            bullet.render();
        }

        player.render();
        batch.end();
    }

    public ArrayList<Character> getMonsterList() {
        return monsterList;
    }

    @Override
    public void dispose () {

    }

    public void handleBulletCollision(ArrayList<Bullet> bulletArrayList, ArrayList<Character> characterArrayList, float deltaTime) {
        //        Check bullet shot from player
        ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bulletArrayList) {
            bullet.update(deltaTime);
//           If true, remove that bullet
            if (bullet.handleCollideWithWall()) {
                bulletsRemove.add(bullet);
            }
//            If collide with monster
            for (Character character : characterArrayList) {
                if (bullet.getBoundingRectangle().overlaps(character.getBoundingRectangle())) {
                    character.getHit(bullet.getDmg());
                    bulletsRemove.add(bullet);
                    break;
                }
            }
        }
        bulletArrayList.removeAll(bulletsRemove);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public Player getPlayer() {
        return player;
    }
}
