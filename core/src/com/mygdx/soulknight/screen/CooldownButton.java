package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.soulknight.entity.Character.Player;

public class CooldownButton extends Actor {
    static final float RADIUS = 50f; // Adjust the radius to your liking
    private Player player;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public float getCooldownTimer() {
        return player.getCoolDownTimer();
    }

    public void setCooldownTimer(float coolDownTimer) {
        player.setCoolDownTimer(coolDownTimer);
    }

    public CooldownButton(final Player player) {
        this.player = player;
        setBounds(getX(), getY(), RADIUS * 2, RADIUS * 2);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!player.isCoolingDown()) {
                    player.activateSpecialSkill();
                    return true;
                }
                return false;
            }
        });
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.P && !player.isCoolingDown()) {
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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(getX() + RADIUS, getY() + RADIUS, RADIUS);

        if (player.isCoolingDown()) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.arc(getX() + RADIUS, getY() + RADIUS, RADIUS, 90, player.getCoolDownTimer() / player.getSpecialSkillCoolDown() * 360, 30);
        }

        shapeRenderer.end();
    }

    public void disposeShapeRenderer() {
        shapeRenderer.dispose(); // Dispose ShapeRenderer
    }
}