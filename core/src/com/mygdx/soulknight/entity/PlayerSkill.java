package com.mygdx.soulknight.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerSkill {
    protected TextureRegion textureRegion;
    protected float currentTimeCoolDown = 0;
    protected float totalTimeCoolDown;
    protected float currentTimeImplement = 0;
    protected float totalTimeImplement;
    protected boolean justFinishImplement = false;
    protected boolean justFinishCooldown = false;

    public PlayerSkill(TextureRegion textureRegion, float totalTimeCoolDown, float totalTimeImplement) {
        this.textureRegion = textureRegion;
        this.totalTimeCoolDown = totalTimeCoolDown;
        this.totalTimeImplement = totalTimeImplement;
    }

    public void stopImplement() {
        currentTimeImplement = 0;
        currentTimeCoolDown = totalTimeCoolDown;
        justFinishImplement = true;
        deactivateSkill();
    }
    public void update(float deltaTime) {
        justFinishImplement = justFinishCooldown = false;
        if (currentTimeImplement > 0) {
            currentTimeImplement -= deltaTime;
            if (currentTimeImplement <= 0) {
                stopImplement();
            }
        }
        if (currentTimeCoolDown > 0) {
            currentTimeCoolDown -= deltaTime;
            if (currentTimeCoolDown <= 0) {
                currentTimeCoolDown = 0;
                justFinishCooldown = true;
            }
        }
    }

    public void activateSkill() {
        currentTimeImplement = totalTimeImplement;
    };
    public void deactivateSkill() {

    };
    public float getCurrentTimeCoolDown() {
        return currentTimeCoolDown;
    }

    public float getTotalTimeCoolDown() {
        return totalTimeCoolDown;
    }

    public float getCurrentTimeImplement() {
        return currentTimeImplement;
    }

    public float getTotalTimeImplement() {
        return totalTimeImplement;
    }

    public boolean isInProgresss() {
        return currentTimeImplement > 0;
    }

    public boolean isCoolingDown() {
        return currentTimeCoolDown > 0;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
