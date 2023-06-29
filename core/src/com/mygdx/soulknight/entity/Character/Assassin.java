package com.mygdx.soulknight.entity.Character;

import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

public class Assassin extends Player {
    private SpriteLoader immortalSpriteLoader = new SpriteLoader("character/assassin/special-skill.png","assassin");
    public Assassin() {
        super("assassin", null);
        totalTimeImplement = specialSkillCoolDown / 2;
    }

    @Override
    public void activateSpecialSkill() {
        super.activateSpecialSkill();
        SpriteLoader temp = spriteLoader;
        spriteLoader = immortalSpriteLoader;
        immortalSpriteLoader = temp;
    }

    @Override
    public void applySpecialSkill(float deltaTime) {
        if (timeImplementLeft > 0) {
            timeImplementLeft -= deltaTime;
            if (timeImplementLeft <= 0) {
                SpriteLoader temp = spriteLoader;
                spriteLoader = immortalSpriteLoader;
                immortalSpriteLoader = temp;
                isImplement = false;
                isCoolingDown = true;
                coolDownTimer = specialSkillCoolDown;
            }
        }
    }

    @Override
    public void getHit(int damage) {
        if (timeImplementLeft <= 0) {
            super.getHit(damage);
        }
    }
}
