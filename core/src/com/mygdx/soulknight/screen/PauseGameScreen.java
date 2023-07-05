package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.soulknight.SoulKnight;

public class PauseGameScreen implements Screen {
    private Stage stage;
    private SoulKnight game;
    private Skin skin;
    private MainGameScreen gameScreen;

    public PauseGameScreen(SoulKnight game, MainGameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));
    }

	@Override
	public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

	     // Create the pause menu buttons
	     Skin skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));

	     Button settingsButton = new TextButton("Go to Settings", skin);
	     Button resumeButton = new TextButton("Resume Game", skin);
		 Button exitGameButton = new TextButton("Exit Game", skin);


		// Add click listeners to the buttons
	     settingsButton.addListener(new ClickListener() {
	         @Override
	         public void clicked(InputEvent event, float x, float y) {
	             // Handle the "Go to Settings" button click
	             // Add code to navigate to the settings screen
	        	 game.setScreen(new SettingsScreen(game,gameScreen));
	        	 dispose();
	         }
	     });
	
	     resumeButton.addListener(new ClickListener() {
	         @Override
	         public void clicked(InputEvent event, float x, float y) {
	             // Handle the "Resume Game" button click
	        	 game.setScreen(gameScreen);
	        	 gameScreen.resumeGame();
	        	 dispose();
	         }
	     });
		exitGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Handle the "Resume Game" button click
				game.setScreen(new MenuScreen(game));
				dispose();
			}
		});
	     Table table = new Table();
	     table.setFillParent(true);

	     // Add the buttons to the table
	     table.add(settingsButton).pad(10);
	     table.row();
	     table.add(resumeButton).pad(10);
		 table.row();
		 table.add(exitGameButton).pad(10);
	     // Create a background image or colored image
	     
	     stage.addActor(table);


	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
        skin.dispose();
	}


}
