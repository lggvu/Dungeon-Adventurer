package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class CooldownButton extends Actor {
    static final float RADIUS = 50f; // Adjust the radius to your liking
    private static final float COOLDOWN_TIME = 5.5f; // Cooldown time in seconds

    private boolean isCoolingDown;
    private float cooldownTimer;
    private Runnable task;
    private ShapeRenderer shapeRenderer = new ShapeRenderer(); 


    public boolean isCoolingDown() {
		return isCoolingDown;
	}

	public float getCooldownTimer() {
		return cooldownTimer;
	}

	public void setCooldownTimer(float cooldownTimer) {
		this.cooldownTimer = cooldownTimer;
	}

	public CooldownButton(final Runnable task) {
		this.task = task;

        setBounds(getX(), getY(), RADIUS * 2, RADIUS * 2);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isCoolingDown) {
                    isCoolingDown = true;
                    cooldownTimer = COOLDOWN_TIME;
                    task.run();
                    return true;
                }
                return false;
            }
        });
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.P && !isCoolingDown) {
                    isCoolingDown = true;
                    cooldownTimer = COOLDOWN_TIME;
                    task.run();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (isCoolingDown) {
            cooldownTimer -= delta;
            if (cooldownTimer <= 0) {
                isCoolingDown = false;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(getX() + RADIUS, getY() + RADIUS, RADIUS);

        if (isCoolingDown) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.arc(getX() + RADIUS, getY() + RADIUS, RADIUS, 90, cooldownTimer / COOLDOWN_TIME * 360, 30);
        }

        shapeRenderer.end();
    }

    public void disposeShapeRenderer() {
        shapeRenderer.dispose(); // Dispose ShapeRenderer
    }
}