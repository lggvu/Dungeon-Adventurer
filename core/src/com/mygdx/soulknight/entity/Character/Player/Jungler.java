package com.mygdx.soulknight.entity.Character.Player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import com.mygdx.soulknight.entity.Skill;
import com.mygdx.soulknight.entity.Weapon.Bullet;
import com.mygdx.soulknight.entity.Weapon.Gun;
import com.mygdx.soulknight.entity.Weapon.Weapon;
import com.mygdx.soulknight.util.SpriteLoader;
import com.mygdx.soulknight.util.TextureInfo;

public class Jungler extends Player{
    private Texture kunaiTexture=new Texture("bullet/kunai.png");
    private Gun kunai;
    private boolean kunaiFlying=false;
    public Jungler() {
        super("jungler", null);
    }
    @Override
    public JsonObject load() {
        JsonObject source = super.load();
        String textureSpecPath = source.get("cooldown_special_skill_texture_path").getAsString();
        kunai= (Gun) Weapon.load(source.get("special_gun").getAsString());
        kunai.setOwner(this);
        kunai.setDrawWeapon(false);
        specialSkill = new Skill(new TextureRegion(new Texture(textureSpecPath)), 1f, 1f) {
            @Override
            public void activateSkill() {
                System.out.println("SIu");
                if(!kunaiFlying) {
                    Vector2 direction = getLastMoveDirection();
                    kunai.shot(direction);
                    kunaiFlying=true;
                }
                else{
                        System.out.println("LEO");
                        float kunaiX = getX();
                        float kunaiY = getY();
                        for (Bullet bullet : kunai.getBulletArrayList()) {
                            kunaiX = bullet.getX();
                            kunaiY = bullet.getY();
                        }

                        setX(kunaiX);
                        setY(kunaiY);
                        kunaiFlying=false;

                    }
                }


            @Override
            public void deactivateSkill() {
                super.deactivateSkill();

            }
        };
        return source;
    }
}
