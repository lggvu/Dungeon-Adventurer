package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.entity.Character.Adventurer;
import com.mygdx.soulknight.entity.Character.Assassin;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.util.TextItem;

public class SelectCharacterScreen extends ScreenAdapter {
    private final SoulKnight game;
    private Texture background;
    private BitmapFont normalFont ;
    private BitmapFont hoverFont;
    private Array<TextItem> textItems;
    private TextItem selectedText = null;
    private TextItem playText;
    private TextureRegion character1Texture;
    private TextureRegion character2Texture;
    private TextureRegion character3Texture;


    public SelectCharacterScreen(SoulKnight game){
        this.game = game;
        background = new Texture("select-character.png");
        normalFont = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));
        hoverFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        textItems = new Array<>();

        Texture texture1=new Texture(Gdx.files.internal("character/adventurer/animation.png"));
        TextureRegion[][] tmp1 = new TextureRegion(texture1).split(texture1.getWidth() / 3,texture1.getHeight() / 2);
        character1Texture = tmp1[0][0];

        Texture texture2=new Texture(Gdx.files.internal("character/jungler/animation.png"));
        TextureRegion[][] tmp2 = new TextureRegion(texture2).split(texture2.getWidth() /10,texture2.getHeight() / 2);
        character2Texture = tmp2[0][0];

        Texture texture3=new Texture(Gdx.files.internal("character/assassin/animation.png"));
        TextureRegion[][] tmp3 = new TextureRegion(texture3).split(texture3.getWidth() /8,texture3.getHeight() / 2);
        character3Texture = tmp3[0][0];

        TextItem text1 = new TextItem("Adventurer", new Vector2(Gdx.graphics.getWidth()/5.8f,Gdx.graphics.getHeight()/2.9f), normalFont, hoverFont);
        TextItem text2 = new TextItem("Jungler", new Vector2(Gdx.graphics.getWidth()/2.65f,Gdx.graphics.getHeight()/2.9f), normalFont, hoverFont);
        TextItem text3 = new TextItem("Assassin", new Vector2(Gdx.graphics.getWidth()/1.9f,Gdx.graphics.getHeight()/2.9f), normalFont, hoverFont);
        TextItem text4 = new TextItem("TrungTT", new Vector2(Gdx.graphics.getWidth()/1.45f,Gdx.graphics.getHeight()/2.9f), normalFont, hoverFont);
        playText = new TextItem("PLAY >>>", new Vector2(Gdx.graphics.getWidth()/1.2f,Gdx.graphics.getHeight()/9.1f), normalFont, hoverFont);

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
        game.getBatch().begin();
        if (Gdx.input.getX()>= playText.getPosition().x && Gdx.input.getX() <= playText.getPosition().x + playText.getLayout().width &&
                Gdx.input.getY() -30 >= Gdx.graphics.getHeight() - playText.getPosition().y - playText.getLayout().height &&
                Gdx.input.getY() -30 <= Gdx.graphics.getHeight() - playText.getPosition().y) {
            playText.setHovered(true);
            if (Gdx.input.isTouched() && selectedText != null) {
                String txt = selectedText.getText();
                if (txt.equals("Assassin")) {
                    game.setScreen(new MainGameScreen(game, new Assassin()));
                } else if (txt.equals("Adventurer")) {
                    game.setScreen(new MainGameScreen(game, new Adventurer()));
                } else {
                    game.setScreen(new MainGameScreen(game, new Assassin()));
                }
                this.dispose();
            }
        }else{
            playText.setHovered(false);
        }
        game.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().draw(character1Texture, Gdx.graphics.getWidth()/5.8f, Gdx.graphics.getHeight()/2.6f, Gdx.graphics.getWidth()/6f,Gdx.graphics.getHeight()/3.7f);
        game.getBatch().draw(character2Texture, Gdx.graphics.getWidth()/2.65f, Gdx.graphics.getHeight()/2.6f, Gdx.graphics.getWidth()/8.5f,Gdx.graphics.getHeight()/3.65f);
        game.getBatch().draw(character3Texture, Gdx.graphics.getWidth()/1.9f, Gdx.graphics.getHeight()/2.6f, Gdx.graphics.getWidth()/6f,Gdx.graphics.getHeight()/2.9f);

        for (TextItem item : textItems) {
            BitmapFont font = item.getFont();
            Vector2 position = item.getPosition();

            // Draw the text
            font.draw(game.getBatch(), item.getLayout(), position.x, position.y);
        }
        playText.getFont().draw(game.getBatch(), playText.getLayout(), playText.getPosition().x, playText.getPosition().y);

        game.getBatch().end();

        for (TextItem item : textItems) {
            if (Gdx.input.getX()>= item.getPosition().x && Gdx.input.getX() <= item.getPosition().x + item.getLayout().width &&
                    Gdx.input.getY() -30 >= Gdx.graphics.getHeight()/1.45f - item.getPosition().y - item.getLayout().height &&
                    Gdx.input.getY() -30 <= Gdx.graphics.getHeight() - item.getPosition().y ) {

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
        normalFont.dispose();
        hoverFont.dispose();
        character1Texture.getTexture().dispose();
        character2Texture.getTexture().dispose();
        character3Texture.getTexture().dispose();
    }
}
