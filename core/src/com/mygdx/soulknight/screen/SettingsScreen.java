package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.soulknight.game.Settings;
import com.mygdx.soulknight.game.SoulKnight;

public class SettingsScreen implements Screen {
    private Stage stage;
    private SoulKnight game;
    private Skin skin;
    private MainGameScreen gameScreen = null;
    public SettingsScreen(SoulKnight game, MainGameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));
    }
    public SettingsScreen(SoulKnight game){
        this.game = game;
        skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        final CheckBox fpsCheckbox30 = new CheckBox("30", skin);
        final CheckBox fpsCheckbox60 = new CheckBox("60", skin);
        
        Label fpsLabel = new Label("FPS:", skin);
        table.add(fpsLabel).left().padRight(10);

        fpsCheckbox30.setChecked(Settings.fps == 30);
        fpsCheckbox60.setChecked(Settings.fps == 60);
        
        // Add a listener to handle checkbox interactions
        ChangeListener checkboxListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CheckBox checkbox = (CheckBox) actor;

                if (checkbox.isChecked()) {
                    if (checkbox == fpsCheckbox30) {
                        fpsCheckbox60.removeListener(this);
                        fpsCheckbox60.setChecked(false);
                        fpsCheckbox60.addListener(this);

                        Settings.fps = 30;
                    } else if (checkbox == fpsCheckbox60) {
                        fpsCheckbox30.removeListener(this);
                        fpsCheckbox30.setChecked(false);
                        fpsCheckbox30.addListener(this);

                        Settings.fps = 60;
                    }
                }
            }
        };

        fpsCheckbox30.addListener(checkboxListener);
        fpsCheckbox60.addListener(checkboxListener);

        // Add checkboxes to the table
        table.add(fpsCheckbox30).left().padRight(10);
        table.add(fpsCheckbox60).left().padRight(10);
        table.row();

        

        Label musicLabel = new Label("Music Volume:", skin);
        table.add(musicLabel).left().padRight(10);

        final Slider musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicVolumeSlider.setValue(Settings.music.getVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.updateMusicVolume(musicVolumeSlider.getValue());
            }
        });
        table.add(musicVolumeSlider).width(200).padBottom(10).row();

        Label soundLabel = new Label("Sound Volume:", skin);
        table.add(soundLabel).left().padRight(10);

        final Slider soundVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundVolumeSlider.setValue(Settings.soundVolume);
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.updateSoundVolume(soundVolumeSlider.getValue());
            }
        });
        table.add(soundVolumeSlider).width(200).padBottom(10).row();

        final Label dodgeBtn = new Label("Dodge Button:", skin);
        table.add(dodgeBtn).left().padRight(10);
        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        final TextField field = new TextField(Input.Keys.toString(Settings.getKeyCode(Settings.GameButton.DODGE)), textFieldStyle);
        field.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                String key = Input.Keys.toString(keycode);
                if (key.length() == 1) {
                    field.setText(key.toUpperCase());
                    Settings.updateDodgeKeyCode(keycode);
                }
                return false;
            }
        });
        field.setMaxLength(1);
        table.add(field).row();

        final Label specialBtn = new Label("Special Skill Button:", skin);
        table.add(specialBtn).left().padRight(10);
        final TextField fieldSpec = new TextField(Input.Keys.toString(Settings.getKeyCode(Settings.GameButton.SPECIAL_SKILL)), textFieldStyle);
        fieldSpec.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                String key = Input.Keys.toString(keycode);
                if (key.length() == 1) {
                    fieldSpec.setText(key.toUpperCase());
                    Settings.updateSpecKeyCode(keycode);
                }
                return false;
            }
        });
        fieldSpec.setMaxLength(1);
        table.add(fieldSpec).row();

        // Add a button to go back to the main menu
        Button okButton = new TextButton("OK", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            if (gameScreen != null) {
                game.setScreen(new PauseGameScreen(game,gameScreen));
            }
            else{
                game.setScreen(new MenuScreen(game));
            }
            Settings.saveSetting();
            dispose();
            }
        });
        table.add(okButton).colspan(2).padTop(50);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // Allow navigating back to the main menu by pressing the ESC key
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (gameScreen != null) {
                game.setScreen(new PauseGameScreen(game,gameScreen));
            }
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
