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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.entity.Character.Player.Adventurer;
import com.mygdx.soulknight.entity.Character.Player.Assassin;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class SelectCharacterScreen extends ScreenAdapter {
    private final SoulKnight game;
    private Texture background;
    private TextItem playText;
    private Level level;
    private SpriteBatch batch = new SpriteBatch();
    private ArrayList<CharacterImageContainer> characterImageContainers = new ArrayList<>();
    private int selectedIndex = -1;
    private HashMap<String, JsonObject> charactersInfo = new HashMap<>();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Skin skin = new Skin();
    private int maxHP = -1, maxMana = -1, maxArmor = -1;

    class CharacterImageContainer {
        private float x, y, width, height, textureWidth, textureHeight, heightImg, border = 1;
        private Texture texture;
        private TextItem textItem;
        private boolean hover = false;
        private Texture background = new Texture("dark_menu.png");

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

            float padding = heightImg * 0.05f, rectWidth = width - padding * 2, rectHeight = heightImg - padding;
            float ratio = rectWidth / texture.getWidth() < rectHeight / texture.getHeight() ? rectWidth / texture.getWidth() : rectHeight / texture.getHeight();
            textureWidth = texture.getWidth() * ratio;
            textureHeight = texture.getHeight() * ratio;
        }

        public void setHover(boolean hover) {
            textItem.setHovered(hover);
            this.hover = hover;
        }

        public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
            if (hover) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(x - border, y - border, width + border * 2, height + border * 2);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(x, y, width, height);
                shapeRenderer.end();
            }
            batch.begin();
            textItem.draw(batch);
            batch.draw(background, x, y + height - heightImg, width, heightImg);
            batch.draw(texture, x + (width - textureWidth) / 2, y + height - heightImg, textureWidth, textureHeight);
            batch.end();
        }

        public void dispose() {
            textItem.dispose();
            texture.dispose();
        }
    }

    public SelectCharacterScreen(SoulKnight game, Level level) {
        this.level = level;
        this.game = game;

        skin.add("heart_icon", new Texture("hud_bar/hp_icon.png"));
        skin.add("mana_icon", new Texture("hud_bar/mana_icon.png"));
        skin.add("stamina_icon", new Texture("hud_bar/stamina_icon.png"));
        skin.add("left_right_border", new Texture("hud_bar/left_right_border.png"));
        skin.add("up_down_border", new Texture("hud_bar/up_down_border.png"));
        skin.add("armor_icon", new Texture("hud_bar/armor_icon.png"));
        skin.add("heart_icon_color", Color.RED);
        skin.add("mana_icon_color", Color.BLUE);
        skin.add("armor_icon_color", Color.GRAY);
        skin.add("stamina_icon_color", Color.GRAY);

        loadHeroInfo();
        background = new Texture("black_back.png");
        playText = createTextItem("PLAY >>>");
        playText.setPosition(new Vector2(Gdx.graphics.getWidth()/1.2f,Gdx.graphics.getHeight()/9.1f));

        float startXRatio = 0.1f, startYRatio = 0.3f, widthRatio = 0.2f, heightRatio = 0.55f, imgHeightRatio = 0.85f;
        float gapRatio = (1 - startXRatio * 2 - widthRatio * 3) / 2;
        int i = 0;
        for (String key : charactersInfo.keySet()) {
            TextItem textItem = createTextItem(key.substring(0, 1).toUpperCase() + key.substring(1));
            String idlePath = charactersInfo.get(key).get("idle_texture_path").getAsString();
            characterImageContainers.add(new CharacterImageContainer(idlePath,
                    (startXRatio + (gapRatio + widthRatio)*(i++)) * Gdx.graphics.getWidth(),
                    startYRatio * Gdx.graphics.getHeight(),
                    widthRatio * Gdx.graphics.getWidth(),
                    heightRatio * Gdx.graphics.getHeight(),
                    imgHeightRatio,
                    textItem
            ));
        }
    }
    private void loadHeroInfo() {
        try {
            JsonObject json = new Gson().fromJson(Gdx.files.internal(SimpleCharacter.CHARACTER_INFO_PATH).reader(), JsonObject.class);
            Iterator<String> strings = json.keySet().iterator();
            while (strings.hasNext()) {
                String characterName = strings.next().toLowerCase();
                JsonObject info = json.get(characterName).getAsJsonObject();
                String type = info.get("type").getAsString();
                if (type.equals("hero")) {
                    charactersInfo.put(characterName, info);
                    int stat = info.get("hp").getAsInt();
                    maxHP = maxHP > stat ? maxHP : stat;
                    stat = info.get("armor").getAsInt();
                    maxArmor = maxArmor > stat ? maxArmor : stat;
                    stat = info.get("energy").getAsInt();
                    maxMana = maxMana > stat ? maxMana : stat;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        int count = 0, hoverIndex = -1;
        for (CharacterImageContainer c : characterImageContainers) {
            if (c.x <= mouseX && mouseX <= c.x + c.width && c.y <= mouseY && mouseY <= c.y + c.height) {
                c.setHover(true);
                if (Gdx.input.isTouched()) {
                    selectedIndex = count;
                }
                hoverIndex = count;
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
                String characterName = characterImageContainers.get(selectedIndex).textItem.getText();
                switch (characterName) {
                    case "Adventurer":
                        game.setScreen(new MainGameScreen(game, new Adventurer(), level));
                        dispose();
                        return;
                    case "Jungler":
                    case "Assassin":
                        game.setScreen(new MainGameScreen(game, new Assassin(), level));
                        dispose();
                        return;
                }
            }
        } else {
            playText.setHovered(false);
        }
        for (CharacterImageContainer characterImageContainer : characterImageContainers) {
            characterImageContainer.draw(batch, shapeRenderer);
        }

        int showInfoIndex = hoverIndex >= 0 ? hoverIndex : (selectedIndex >= 0 ? selectedIndex : -1);
        if (showInfoIndex >= 0) {
            JsonObject jsonObject = charactersInfo.get(characterImageContainers.get(showInfoIndex).textItem.getText().toLowerCase());

            float maxWidthIcon = skin.get("heart_icon", Texture.class).getWidth();
            float barHeight = skin.get("left_right_border", Texture.class).getHeight();
            float scale = 1.5f;
            float barWidth = 150;

//        do not scale
            float gapWidth = 10;
            float gapHeight = 10;
            float paddingX = 50;
            float paddingY = 10;

            float offsetY = paddingY;
            float barX = paddingX + maxWidthIcon * scale + gapWidth;
            Texture icon = skin.get("stamina_icon", Texture.class);
            batch.begin();
            batch.draw(icon, paddingX, offsetY + scale, icon.getWidth() * scale, icon.getHeight() * scale);
            TextItem textItem = createTextItem(jsonObject.get("description").getAsString());
            textItem.setScale(0.7f);
            textItem.setPosition(new Vector2(barX, offsetY + textItem.getLayout().height + scale));
            textItem.draw(batch);
            batch.end();
            offsetY += (barHeight * scale + gapHeight);
            drawOneBar("armor_icon", paddingX, offsetY, barX, barWidth, scale, jsonObject.get("armor").getAsFloat() / maxArmor);
            offsetY += (barHeight * scale + gapHeight);
            drawOneBar("mana_icon", paddingX, offsetY, barX, barWidth, scale,jsonObject.get("energy").getAsFloat() / maxMana);
            offsetY += (barHeight * scale + gapHeight);
            drawOneBar("heart_icon", paddingX, offsetY, barX, barWidth, scale,jsonObject.get("hp").getAsFloat() / maxHP);
        }
        batch.begin();
        playText.draw(batch);
        batch.end();
    }
    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        skin.dispose();
        for (CharacterImageContainer characterImageContainer : characterImageContainers) {
            characterImageContainer.dispose();
        }
    }

    private void drawOneBar(String iconName, float offsetX, float offsetY, float barX, float barWidth, float scale, float ratio) {
        Texture icon = skin.get(iconName, Texture.class);
        Texture leftRight = skin.get("left_right_border", Texture.class);
        Texture upDown = skin.get("up_down_border", Texture.class);

        float cX = offsetX, barHeight = leftRight.getHeight() * scale - 2 * upDown.getHeight() * scale;
        batch.begin();
        batch.draw(icon, cX, offsetY + scale, icon.getWidth() * scale, icon.getHeight() * scale);
        cX = barX;
        batch.draw(leftRight, cX, offsetY, leftRight.getWidth() * scale, leftRight.getHeight() * scale);
        cX += leftRight.getWidth() * scale;
        batch.draw(upDown, cX, offsetY, barWidth * scale, upDown.getHeight() * scale);
        batch.draw(upDown, cX, offsetY + barHeight + upDown.getHeight() * scale, barWidth * scale, upDown.getHeight() * scale);
        cX += barWidth * scale;
        batch.draw(leftRight, cX, offsetY, leftRight.getWidth() * scale, leftRight.getHeight() * scale);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX + leftRight.getWidth() * scale, offsetY + upDown.getHeight() * scale, barWidth * scale, barHeight);
        shapeRenderer.setColor(skin.get(iconName + "_color", Color.class));
        shapeRenderer.rect(barX + leftRight.getWidth() * scale, offsetY + upDown.getHeight() * scale, barWidth * ratio * scale, barHeight);
        shapeRenderer.end();
    }
}
