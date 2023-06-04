package com.mygdx.soulknight.entity.Character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class SpecialSkill {
	
	protected boolean isActivate = false;
	public boolean isActivate() {
		return isActivate;
	}
	public void setActivate(boolean isActivate) {
		this.isActivate = isActivate;
	}
	public abstract void draw(SpriteBatch batch);
	public abstract void activate();
	public abstract void update(float Deltatime);
}
