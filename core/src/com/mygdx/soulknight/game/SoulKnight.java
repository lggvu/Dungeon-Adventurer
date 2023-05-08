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
import com.mygdx.soulknight.game.entity.Bullet;
import com.mygdx.soulknight.game.entity.Character;
import com.mygdx.soulknight.game.entity.Player;
import com.mygdx.soulknight.game.entity.Weapon;

import java.util.ArrayList;

public class SoulKnight extends ApplicationAdapter {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private Player player;
    private ArrayList<Bullet> bulletList;

    @Override
    public void create () {
        bulletList = new ArrayList<Bullet>();
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
            player.getCurrentWeapon().attack();
        }

        camera.position.x = player.getX() + player.getWidth() /2;
        camera.position.y = player.getY() + player.getHeight() / 2;
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.update(deltaTime);
//        Check if a bullet is destroyed or not
        ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bulletList) {
            bullet.update(deltaTime);
//           If true, remove that bullet
            if (bullet.handleCollideWithWall()) {
                bulletsRemove.add(bullet);
            }
        }
        bulletList.removeAll(bulletsRemove);

        for (Bullet bullet : bulletList) {
            bullet.render();
        }

        player.render();
        batch.end();
    }

    @Override
    public void dispose () {

    }

    public void addBullet(Bullet bullet) {
        bulletList.add(bullet);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }
}
