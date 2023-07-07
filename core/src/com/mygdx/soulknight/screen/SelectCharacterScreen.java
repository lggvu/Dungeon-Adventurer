package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.entity.Character.Player.Adventurer;
import com.mygdx.soulknight.entity.Character.Player.Assassin;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextItem;

import java.util.ArrayList;

public class SelectCharacterScreen extends ScreenAdapter {
    private final SoulKnight game;
    private Texture background;
    private TextItem selectedText = null;
    private TextItem playText;
    private Level level;
    private SpriteBatch batch = new SpriteBatch();
    private ArrayList<CharacterImageContainer> characterImageContainers = new ArrayList<>();
    private int selectedIndex = -1;

    class CharacterImageContainer {
        private float x, y, width, height, heightImg, padding = 1;
        private Texture texture;
        private TextItem textItem;
        private boolean hover = false;
        private ShapeRenderer shapeRenderer = new ShapeRenderer();
        private SpriteBatch batch = new SpriteBatch();
        public CharacterImageContainer(String texturePath, float x, float y, float width, float height, float imgHeightRatio, TextItem textItem) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.texture = new Texture(texturePath);
            this.heightImg = imgHeightRatio * height;
            this.textItem = textItem;
            GlyphLayout layout = textItem.getLayout();
            float yPos = y + (height - heightImg + layout.height) / 2;
            this.textItem.setPosition(new Vector2(x + (width - layout.width) / 2, yPos));
        }

        public void setHover(boolean hover) {
            textItem.setHovered(hover);
            this.hover = hover;
        }

        public void draw() {
            if (hover) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(x - padding, y - padding, width + padding * 2, height + padding * 2);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(x, y, width, height);
                shapeRenderer.end();
            }
            batch.begin();
            textItem.draw(batch);
            batch.draw(texture, x, y + height - heightImg, width, heightImg);
            batch.end();
        }

        public void dispose() {
            textItem.dispose();
            texture.dispose();
            shapeRenderer.dispose();
            batch.dispose();
        }
    }

    public SelectCharacterScreen(SoulKnight game, Level level) {
        this.level = level;
        this.game = game;
        background = new Texture("black_back.png");
        float startX = 200, startY = 250, width = 250, height = 400, imgHeightRatio = 0.85f;

        float gap = (Gdx.graphics.getWidth() - startX * 2 - width * 3) / 2;
        TextItem text1 = createTextItem("Adventurer");
        TextItem text2 = createTextItem("Jungler");
        TextItem text3 = createTextItem("Assassin");
        playText = createTextItem("PLAY >>>");
        playText.setPosition(new Vector2(Gdx.graphics.getWidth()/1.2f,Gdx.graphics.getHeight()/9.1f));

        characterImageContainers.add(new CharacterImageContainer("dark_menu.png", startX, startY, width, height, imgHeightRatio, text1));
        characterImageContainers.add(new CharacterImageContainer("dark_menu.png", startX + (gap + width), startY, width, height, imgHeightRatio, text2));
        characterImageContainers.add(new CharacterImageContainer("dark_menu.png", startX + (gap + width) * 2, startY, width, height, imgHeightRatio, text3));
    }

    private TextItem createTextItem(String text) {
        BitmapFont normalFont = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));
        BitmapFont hoverFont = new BitmapFont(Gdx.files.internal("font/white.fnt"));
        return new TextItem(text, null, normalFont, hoverFont);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        float mouseX = Gdx.input.getX(), mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        int count = 0;
        for (CharacterImageContainer c : characterImageContainers) {
            if (c.x <= mouseX && mouseX <= c.x + c.width && c.y <= mouseY && mouseY <= c.y + c.height) {
                c.setHover(true);
                if (Gdx.input.isTouched()) {
                    selectedIndex = count;
                }
            } else {
                c.setHover(false);
            }
            count++;
        }

        if (selectedIndex >= 0) {
            characterImageContainers.get(selectedIndex).setHover(true);
        }

        Vector2 position = playText.getPosition();
        GlyphLayout pLayout = playText.getLayout();
        float px = position.x, py = position.y, pwidth = pLayout.width, pheight = pLayout.height;
        if (px <= mouseX && mouseX <= px + pwidth && py - pheight <= mouseY && mouseY <= py) {
            playText.setHovered(true);
            if (Gdx.input.isTouched()) {
                switch (selectedIndex) {
                    case 0:
                        game.setScreen(new SelectAbilityScreen(game, level, new Adventurer()));
                        dispose();
                        return;
                    case 1:
                    case 2:
                        game.setScreen(new SelectAbilityScreen(game, level, new Assassin()));
                        dispose();
                        return;
                }
            }
        } else {
            playText.setHovered(false);
        }
        for (CharacterImageContainer characterImageContainer : characterImageContainers) {
            characterImageContainer.draw();
        }
        batch.begin();
        playText.draw(batch);
        batch.end();
    }
    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        for (CharacterImageContainer characterImageContainer : characterImageContainers) {
            characterImageContainer.dispose();
        }
    }
}
