package com.mygdx.soulknight;
import java.util.ArrayList;
import java.util.Iterator;

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
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyGdxGame extends ApplicationAdapter {
    private static final float CHARACTER_SPEED = 180f;
    private final float CHARACTER_SCALE_FACTOR = 0.75f;

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private Character character;
    private SpriteBatch spriteBatch;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private Vector2 direction = new Vector2(1,0);
    private ArrayList<Bullet> monsterbullets = new ArrayList<Bullet>();
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private float elapsedSeconds =0;
    private float intervalSeconds = 2; //Monster will shoot each 3 seconds
    @Override
    public void create () {
        // Set up the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Load the map
        tiledMap = new TmxMapLoader().load("second_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collisions");

        // Load the character texture and position
        character = new Character(new Texture("bucket.png"), tiledMap,"Collisions");
        character.setSize(character.getWidth() / 2, character.getHeight() / 2);
        // Set up the sprite batch
        spriteBatch = new SpriteBatch();
        monsters = random_monsters(10);
    }

    @Override
    public void render () {
    	
    	
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        elapsedSeconds += Gdx.graphics.getDeltaTime();

        // Move the character based on user input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            character.move(-CHARACTER_SPEED * deltaTime,0);
            direction = new Vector2(-1,0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
        	character.move(CHARACTER_SPEED * deltaTime, 0);
        	direction = new Vector2(1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            character.move(0, CHARACTER_SPEED * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
        	character.move(0, -CHARACTER_SPEED * deltaTime);
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            //Create a new bullet texture and set its position to the current position of the character
            Texture bulletTexture = new Texture("bucket.png");
            Vector2 bulletPosition = new Vector2(character.getX() + character.getTexture().getWidth() /2, character.getY() + character.getTexture().getHeight() / 4);
            direction.nor();

            //Add the new bullet to the game's list of bullets
            Bullet bullet = new Bullet(bulletTexture, bulletPosition, direction);
            bullets.add(bullet);
        }
        

        // Update the camera position based on the character's position
        camera.position.x = character.getX() + character.getTexture().getWidth() /2;
        camera.position.y = character.getY() + character.getTexture().getHeight() / 2;
        camera.update();

        // Render the map and the character
        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(character.getTexture(), character.getX(), character.getY(), 0, 0, character.getTexture().getWidth(), character.getTexture().getHeight(), 0.5f, 0.5f, 0, 0, 0, character.getTexture().getWidth(), character.getTexture().getHeight(), false, false);
        
        Iterator<Monster> iteratorMonster = monsters.iterator();
        while (iteratorMonster.hasNext()){
        	Monster monster = iteratorMonster.next();
        	if (monster.HP <0) {
        		iteratorMonster.remove();
        	}
        	else {
        		spriteBatch.draw(monster.getTexture(), monster.getX(), monster.getY(), 0, 0, monster.getTexture().getWidth(), monster.getTexture().getHeight(), 0.5f, 0.5f, 0, 0, 0, monster.getTexture().getWidth(), monster.getTexture().getHeight(), false, false);
        	}
        }
        iteratorMonster = monsters.iterator();

        if (elapsedSeconds >= intervalSeconds) {
	        while (iteratorMonster.hasNext()){
	        	Monster monster = iteratorMonster.next();
        		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
                bullets = monster.shootBullets();
                monsterbullets.addAll(bullets);
	        	}

	        elapsedSeconds = 0;
	    }
    
        Iterator<Bullet> iteratorMonsterBullets = monsterbullets.iterator();
        while (iteratorMonsterBullets.hasNext()) {
            Bullet bullet = iteratorMonsterBullets.next();
            bullet.update();
            spriteBatch.draw(bullet.getTexture(), bullet.getX(), bullet.getY(), 0, 0, bullet.getTexture().getWidth(), bullet.getTexture().getHeight(), 0.2f, 0.2f, 0, 0, 0, bullet.getTexture().getWidth(), bullet.getTexture().getHeight(), false, false);

            //Check for collision between the bullet and the collision layer
            int tileX = (int) (bullet.getX() / collisionLayer.getTileWidth());
            int tileY = (int) (bullet.getY() / collisionLayer.getTileHeight());
            
            TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);

            if(cell != null){
                //Remove the bullet from the game's list of bullets if it collides with a tile in the collision layer
            	iteratorMonsterBullets.remove();
                continue;
            }
            if (bullet.getBoundingRectangle().overlaps(character.getBoundingRectangle())) {
            	character.getHit(50);
            	System.out.println(character.getHP());
            	iteratorMonsterBullets.remove();
            	continue;
            }
        }

        
        
        
        //Render the bullets
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()){
            //Update the bullet position based on its direction and speed
            Bullet bullet = iterator.next();
        	bullet.update();

            //Render the bullet
            spriteBatch.draw(bullet.getTexture(), bullet.getX(), bullet.getY(), 0, 0, bullet.getTexture().getWidth(), bullet.getTexture().getHeight(), 0.2f, 0.2f, 0, 0, 0, bullet.getTexture().getWidth(), bullet.getTexture().getHeight(), false, false);
            

            //Check for collision between the bullet and the collision layer
            int tileX = (int) (bullet.getX() / collisionLayer.getTileWidth());
            int tileY = (int) (bullet.getY() / collisionLayer.getTileHeight());
            
            TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);

            if(cell != null){
                //Remove the bullet from the game's list of bullets if it collides with a tile in the collision layer
                iterator.remove();
                continue;
            }
                // Check for collision with each monster
            Iterator<Monster> monsterIterator = monsters.iterator();
            while (monsterIterator.hasNext()) {
                Monster monster = monsterIterator.next();

                if (bullet.getBoundingRectangle().overlaps(monster.getBoundingRectangle())) {
                	monster.getHit(50);
                	iterator.remove();
                	continue;
                }
            }
            
        
        }
        
        spriteBatch.end();
    }



    @Override
    public void dispose () {
        // Dispose of resources
        tiledMap.dispose();
        mapRenderer.dispose();
        character.getTexture().dispose();
        for (Bullet bullet : bullets) {
            bullet.getTexture().dispose();
        }
        for (Monster monster : monsters) {
            monster.getTexture().dispose();
        }

        spriteBatch.dispose();
    }
    
    public ArrayList<Monster> random_monsters(int num_monster) {
    	ArrayList<Monster> monsters = new ArrayList<Monster>();
    	for (int i = 0; i < num_monster; i++) {
    		Monster monster = new Monster(new Texture("bucket.png"), tiledMap,"Collisions");
    		monsters.add(monster);
    	}
    	
    	return monsters;
    }
}


