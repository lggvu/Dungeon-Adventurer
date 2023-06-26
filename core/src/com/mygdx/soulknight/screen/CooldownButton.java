package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.soulknight.entity.Character.Player;

public class CooldownButton extends Actor {
    static final float RADIUS = 50f; // Adjust the radius to your liking
    private Player player;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();
    private TextureRegion textureRegion;

    public float getCooldownTimer() {
        return player.getCoolDownTimer();
    }

    public CooldownButton(final Player player) {
        this.player = player;
        textureRegion = new TextureRegion(new Texture("cooldown_circle.png"),1,388, 256, 256);
        setBounds(getX(), getY(), RADIUS * 2, RADIUS * 2);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!player.isCoolingDown() && !player.isImplement()) {
                    player.activateSpecialSkill();
                    return true;
                }
                return false;
            }
        });
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.P && !player.isCoolingDown() && !player.isImplement()) {
                    player.activateSpecialSkill();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());


        spriteBatch.begin();
//        spriteBatch.draw(textureRegion,0,0);
        float margin = 4f;
        spriteBatch.draw(textureRegion, getX()-margin, getY()-margin, (RADIUS+margin)*2, (RADIUS+margin)*2);
        spriteBatch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float start = 90;
        if (player.isImplement() && player.getTotalTimeImplement() > 0) {
            shapeRenderer.setColor(173/255f, 232/255f, 244/255f, 0.5f);
            float degree = 360 - player.getTimeImplementLeft() / player.getTotalTimeImplement() * 360;
            shapeRenderer.arc(getX() + RADIUS, getY() + RADIUS, RADIUS,90-degree,degree, 30);
        }

        if (player.isCoolingDown()) {
            shapeRenderer.setColor(229/255f, 229/255f, 229/255f, 0.5f);
            float degree = player.getCoolDownTimer() / player.getSpecialSkillCoolDown() * 360;
            shapeRenderer.arc(getX() + RADIUS, getY() + RADIUS, RADIUS,90-degree,degree,30);
        }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void disposeShapeRenderer() {
        shapeRenderer.dispose(); // Dispose ShapeRenderer
    }
}