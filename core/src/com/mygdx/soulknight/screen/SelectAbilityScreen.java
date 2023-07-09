package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.soulknight.game.DungeonAdventurer;
import com.mygdx.soulknight.ability.Ability;
import com.mygdx.soulknight.entity.Character.Player.Player;
import java.util.ArrayList;

public class SelectAbilityScreen extends ScreenAdapter {
    private Texture background = new Texture("select-character.png");
    private DungeonAdventurer game;
    private ArrayList<Ability.AbilityEnum> abilityEnums = new ArrayList<>();
    private SpriteBatch batch = new SpriteBatch();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/darker_gray.fnt"));
    private MainGameScreen mainGameScreen;
    public SelectAbilityScreen(DungeonAdventurer game, MainGameScreen screen) {
        this.game = game;
        mainGameScreen = screen;
        Ability.AbilityEnum[] enums = Ability.AbilityEnum.class.getEnumConstants();
        ArrayList<Ability.AbilityEnum> abilitiesLeft = new ArrayList<>();
        for (Ability.AbilityEnum abilityEnum : enums) {
            if (!screen.getPlayer().getAbility().getAbilityEnumArrayList().contains(abilityEnum)) {
                abilitiesLeft.add(abilityEnum);
            }
        }
        for (int i = 0; i < 3; i++) {
            int index = (int) (Math.random() * abilitiesLeft.size());
            abilityEnums.add(abilitiesLeft.get(index));
            abilitiesLeft.remove(index);
        }
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float size = Gdx.graphics.getHeight() / 4f;
        float marginY = (Gdx.graphics.getHeight() - size) / 2;
        float marginX = Gdx.graphics.getWidth() / 6f;
        float spaceBetween = (Gdx.graphics.getWidth() - marginX * 2 - size * abilityEnums.size()) / (abilityEnums.size() - 1);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int i = 0; i < abilityEnums.size(); i++) {
            shapeRenderer.rect(marginX + (size + spaceBetween) * i, marginY, size, size);
        }
        shapeRenderer.end();

        batch.begin();
        for (int i = 0; i < abilityEnums.size(); i++) {
            TextureRegion textureRegion = abilityEnums.get(i).getTextureCoolDown();
            batch.draw(textureRegion, marginX + (size + spaceBetween) * i, marginY, size, size);
        }
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float mouseX = Gdx.input.getX(), mouseY = Gdx.input.getY() - 30;
        int hoverIndex = -1;
        boolean isCick = false;
        for (int i = 0; i < abilityEnums.size(); i++) {
            float bottomLeftX = marginX + (size + spaceBetween) * i, bottemLeftY = marginY;
            if (bottomLeftX <= mouseX && mouseX <= (bottomLeftX + size) && bottemLeftY <= mouseY && mouseY <= (bottemLeftY + size)) {
//                hover
                shapeRenderer.setColor(229/255f, 229/255f, 229/255f, 0.25f);
                shapeRenderer.rect(marginX + (size + spaceBetween) * i, marginY, size, size);
                hoverIndex = i;
                if (Gdx.input.isTouched()) {
                    isCick = true;
                    Player player = mainGameScreen.getPlayer();
                    player.getAbility().addAbility(player, abilityEnums.get(i));
                    mainGameScreen.loadNextMap();
                    game.setScreen(mainGameScreen);
                }
            }
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        if (isCick) {
            dispose();
            return;
        }

        if (hoverIndex >= 0) {
            batch.begin();
            GlyphLayout layout = new GlyphLayout();
            layout.setText(font, getAbilityDescription(abilityEnums.get(hoverIndex)));
            font.draw(batch, layout,(Gdx.graphics.getWidth() - layout.width) / 2, (marginY + layout.height) / 2);
            batch.end();
        }
    }

    public String getAbilityDescription(Ability.AbilityEnum abilityEnum) {
        switch (abilityEnum) {
            case STUN_IMMUNITY:
                return "Immune with stun";
            case MAX_MANA_INCREASE:
                return "Maximize energy levels for player";
            case MAX_WEAPON_INCREASE:
                return "Player has more weapon slot";
            case MAX_ARMOR_INCREASE:
                return "Maximize shield levels for player";
            case LIGHTNING_IMMUNITY:
                return "Immune with lightning damage";
            case POISON_IMMUNITY:
                return "Immune with poison damage";
            case MAX_HP_INCREASE:
                return "Maximize hp levels for player";
            case DAMAGE_INCREASE:
                return "Maximize damage levels for player";
            case FIRE_IMMUNITY:
                return "Immune with fire damage";
            case NUM_WALL_COLLIDE_INCREASE:
                return "Bullet can bounce";
            default:
                return "Something went wrong here";
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
