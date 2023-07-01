package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.soulknight.Level;
import com.mygdx.soulknight.SoulKnight;
import com.mygdx.soulknight.ability.Ability;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.util.SpriteLoader;

import java.util.ArrayList;

public class SelectAbilityScreen extends ScreenAdapter {
    private Texture background = new Texture("select-character.png");
    private SoulKnight game;
    private Level level;
    private Player player;
    private ArrayList<Ability.AbilityEnum> abilityEnums = new ArrayList<>();
    private SpriteBatch batch = new SpriteBatch();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private int selectedIndex = -1;

    public SelectAbilityScreen(SoulKnight game, Level level, Player player) {
        this.level = level;
        this.game = game;
        this.player = player;

        Ability.AbilityEnum[] enums = Ability.AbilityEnum.class.getEnumConstants();
        ArrayList<Ability.AbilityEnum> abilitiesLeft = new ArrayList<>();
        for (Ability.AbilityEnum abilityEnum : enums) {
            if (!player.getAbility().getAbilityEnumArrayList().contains(abilityEnum)) {
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

        float size = 200;
        float marginY = (Gdx.graphics.getHeight() - size) / 2;
        float marginX = 200;
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
        for (int i = 0; i < abilityEnums.size(); i++) {
            float bottomLeftX = marginX + (size + spaceBetween) * i, bottemLeftY = marginY;
            if (bottomLeftX <= mouseX && mouseX <= (bottomLeftX + size) && bottemLeftY <= mouseY && mouseY <= (bottemLeftY + size)) {
//                hover
                shapeRenderer.setColor(229/255f, 229/255f, 229/255f, 0.5f);
                shapeRenderer.rect(marginX + (size + spaceBetween) * i, marginY, size, size);
            }
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose () {
        batch.dispose();
        shapeRenderer.dispose();
    }
}
