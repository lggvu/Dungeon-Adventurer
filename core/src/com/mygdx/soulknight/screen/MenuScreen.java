package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.util.TextItem;

public class MenuScreen extends ScreenAdapter {
    SpriteBatch batch;
    SoulKnight game;
    Texture background;
    Texture startGameTexture;
    private float startBtnWidth = 250;
    private float startBtnHeight = 250;
    private float startBtnY = 50;
    private BitmapFont normalFont ;
    private BitmapFont hoverFont;
    private Array<TextItem> textItems;
    private TextItem selectedText = null;

    public MenuScreen(SoulKnight game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("dark_menu.png");
        startGameTexture = new Texture("start_game.png");
        normalFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        hoverFont = new BitmapFont(Gdx.files.internal("font/orange.fnt"));


        textItems = new Array<>();

        TextItem text1 = new TextItem("Easy", new Vector2(Gdx.graphics.getWidth()/3.8f,Gdx.graphics.getHeight()/1.9f), normalFont, hoverFont);
        TextItem text2 = new TextItem("Medium", new Vector2(Gdx.graphics.getWidth()/2.55f,Gdx.graphics.getHeight()/1.9f), normalFont, hoverFont);
        TextItem text3 = new TextItem("Hard", new Vector2(Gdx.graphics.getWidth()/1.9f,Gdx.graphics.getHeight()/1.9f), normalFont, hoverFont);
        TextItem text4 = new TextItem("Nightmare", new Vector2(Gdx.graphics.getWidth()/1.55f,Gdx.graphics.getHeight()/1.9f), normalFont, hoverFont);

        textItems.add(text1);
        textItems.add(text2);
        textItems.add(text3);
        textItems.add(text4);

    }


    @Override
    public void render(float delta) {
//        System.out.println("" + Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float x = (Gdx.graphics.getWidth() - startBtnWidth) / 2;
        batch.begin();
        if (
            (Gdx.input.getX() > x) &&
            (Gdx.input.getX() < x + startBtnWidth) &&
            (Gdx.graphics.getHeight() - Gdx.input.getY() > startBtnY) &&
            (Gdx.graphics.getHeight() - Gdx.input.getY() < startBtnHeight + startBtnY )
        ) {
            if (Gdx.input.isTouched() && selectedText != null) {
                game.setScreen(new SelectCharacterScreen(game, Level.valueOf(selectedText.getText().toUpperCase())));
            }
        }

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(startGameTexture, x, startBtnY, startBtnWidth, startBtnHeight);
        for (TextItem item : textItems) {
            BitmapFont font = item.getFont();
            Vector2 position = item.getPosition();

            // Draw the text
            font.draw(batch, item.getLayout(), position.x, position.y);
        }

        batch.end();
        Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        for (TextItem item : textItems) {
            if (Gdx.input.getX()>= item.getPosition().x && Gdx.input.getX() <= item.getPosition().x + item.getLayout().width &&
                    Gdx.input.getY() -30 >= Gdx.graphics.getHeight() - item.getPosition().y - item.getLayout().height &&
                    Gdx.input.getY() -30 <= Gdx.graphics.getHeight() - item.getPosition().y) {

                item.setHovered(true);
                if (Gdx.input.isTouched()){
                    if (selectedText != null){
                        selectedText.setSelected(false);
                    }
                    item.setSelected(true);
                    selectedText = item;

                }
            } else {
                item.setHovered(false);
            }
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        startGameTexture.dispose();
        normalFont.dispose();
        hoverFont.dispose();
        batch.dispose();
    }
}
