package com.mygdx.soulknight.entity.Character.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.Settings;
import com.mygdx.soulknight.entity.DamageType;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.entity.Weapon.Kunai;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

public class Assassin extends Player {
    private float timeFromThrow = 0;
    private Texture kunaiTexture = new Texture("bullet/kunai.png");
    private Kunai kunai;
    public Assassin() {
        super("assassin", null);
    }
    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        kunai = new Kunai();
        kunai.setOwner(this);
        specialSkill = new Skill(new TextureRegion(new Texture(textureSpecPath)), 1f, 2f) {
            @Override
            public void activateSkill() {
                super.activateSkill();
                timeFromThrow = 0;
                kunai.attack(getLastMoveDirection());
            }
            @Override
            public void deactivateSkill(){
                super.deactivateSkill();
                kunai.isFlying=false;
            }
        };
        return source;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (specialSkill.isInProgresss()) {
            kunai.update(deltaTime,getMap());
            if (Gdx.input.isKeyPressed(Settings.getKeyCode(Settings.GameButton.SPECIAL_SKILL)) && timeFromThrow > 0.3f) {
                setX(kunai.getX());
                setY(kunai.getY());
                specialSkill.stopImplement();
            }
            timeFromThrow += deltaTime;
        }
    }
    @Override
    public void draw(SpriteBatch batch) {

        super.draw(batch);
        if (specialSkill.isInProgresss()) {
            kunai.draw(batch);
        }
    }
}


