package com.mygdx.soulknight.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.entity.Skill;

public class CooldownButton extends Actor {
    private Skill skill;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    private float radius, margin;

    public CooldownButton(final Skill skill, float x, float y, float radius, float margin, final Settings.GameButton gameButton) {
        this.skill = skill;
        this.radius = radius;
        this.margin = margin;
        setPosition(x - radius, y - radius);
        setBounds(getX(), getY(), radius * 2, radius * 2);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (!skill.isInProgresss() && !skill.isCoolingDown()) {
                skill.activateSkill();
                return true;
            }
            return false;
            }
        });
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Settings.getKeyCode(gameButton) && !skill.isInProgresss() && !skill.isCoolingDown()) {
                skill.activateSkill();
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
        spriteBatch.draw(skill.getTextureRegion(),getX() - margin,getY() - margin,
                (radius + margin) * 2,(radius + margin) * 2);
        spriteBatch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float start = 90;
        if (skill.isInProgresss() && skill.getTotalTimeImplement() > 0) {
            shapeRenderer.setColor(173/255f, 232/255f, 244/255f, 0.5f);
            float degree = 360 - skill.getCurrentTimeImplement() / skill.getTotalTimeImplement() * 360;
            shapeRenderer.arc(getX() + radius, getY() + radius, radius,90, -degree, 30);
        }

        if (skill.isCoolingDown() && skill.getTotalTimeCoolDown() > 0) {
            shapeRenderer.setColor(229/255f, 229/255f, 229/255f, 0.5f);
            float degree = skill.getCurrentTimeCoolDown() / skill.getTotalTimeCoolDown() * 360;
            shapeRenderer.arc(getX() + radius, getY() + radius, radius,90,-degree,30);
        }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void disposeShapeRenderer() {
        shapeRenderer.dispose(); // Dispose ShapeRenderer
    }
}