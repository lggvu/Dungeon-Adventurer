package com.mygdx.soulknight.entity.Character.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Kunai;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

public class Jungler extends Player {
    private Texture kunaiTexture = new Texture("bullet/kunai.png");
    private Kunai kunai;

    public Jungler() {
        super("jungler", null);
    }

    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        kunai = new Kunai();
        kunai.setMap(map);
        kunai.setOwner(this);
//        kunai.setDrawW
        specialSkill = new Skill(new TextureRegion(new Texture(textureSpecPath)), 1f, 1f) {
            @Override
            public void activateSkill() {
                super.activateSkill();
                System.out.println("SIUUU");
                if (!kunai.isFlying) {
                    kunai.shot(getLastMoveDirection());
                    kunai.isFlying=true;


                } else {
                    setX(kunai.getX());
                    setY(kunai.getY());
                    kunai.isFlying = false;
                }
            }
        };
        return source;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        kunai.update(deltaTime);

    }
    @Override
    public void draw(SpriteBatch batch) {

        super.draw(batch);
        kunai.draw(batch);
    }
}
