package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.soulknight.game.Settings;
import com.mygdx.soulknight.game.DungeonAdventurer;
import com.mygdx.soulknight.util.TextWrapper;

public class HelpScreen extends ScreenAdapter {
    private DungeonAdventurer game;
    private TextWrapper textWrapper;
    private SpriteBatch batch = new SpriteBatch();
    private Stage stage;
    private String text;
    public HelpScreen(DungeonAdventurer game) {
        this.game = game;
        FileHandle fileHandle = Gdx.files.internal("info/help.txt");
        if (fileHandle.exists()) {
            text = fileHandle.readString();
            // Read the file content as a string
            textWrapper = new TextWrapper(
                new BitmapFont(Gdx.files.internal("font/white.fnt")),
                vw(0.75f),
                0.7f,
                text
            );
        }
    }

    @Override
    public void show () {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Table table = new Table();
        table.setFillParent(true);
        Table contentTable = new Table();
//        contentTable.setFillParent(true);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = textWrapper.getFont();
        contentTable.add(new Label(textWrapper.getWrapText(text), labelStyle));
        ScrollPane scrollPane = new ScrollPane(contentTable);
        TextButton button = new TextButton("Exit", Settings.skin);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new MenuScreen(game));
            dispose();
            }
        });

        table.add(scrollPane).maxHeight(vh(0.8f)).padBottom(vh(0.025f)).row();
        table.add(button);
        stage.addActor(table);
        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
//        textWrapper.draw(batch, vw(0.1f), vh(1));
//        batch.end();
        stage.act(delta);
        stage.draw();
    }

    public float vw(float x) {
        return Gdx.graphics.getWidth() * x;
    }
    public float vh(float y) {
        return Gdx.graphics.getHeight() * y;
    }

    @Override
    public void dispose () {
        batch.dispose();
        textWrapper.dispose();
        stage.dispose();
    }
}
