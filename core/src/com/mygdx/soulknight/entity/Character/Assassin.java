package com.mygdx.soulknight.entity.Character;

import com.mygdx.soulknight.entity.Map.WorldMap;
import com.mygdx.soulknight.util.SpriteLoader;

public class Assassin extends Player {
    private float immortalTime = specialSkillCoolDown / 2;
    private float timeImmortalLeft = 0;
    private SpriteLoader immortalSpriteLoader = new SpriteLoader("character/assassin/special-skill.png","assassin");
    public Assassin() {
        super("assassin", null);
    }

    @Override
    public void activateSpecialSkill() {
        super.activateSpecialSkill();
        timeImmortalLeft = immortalTime;
        SpriteLoader temp = spriteLoader;
        spriteLoader = immortalSpriteLoader;
        immortalSpriteLoader = temp;
    }

    @Override
    public void applySpecialSkill(float deltaTime) {
        if (timeImmortalLeft > 0) {
            timeImmortalLeft -= deltaTime;
            if (timeImmortalLeft <= 0) {
                SpriteLoader temp = spriteLoader;
                spriteLoader = immortalSpriteLoader;
                immortalSpriteLoader = temp;
            }
        }
    }

    @Override
    public void getHit(int damage) {
        if (timeImmortalLeft <= 0) {
            super.getHit(damage);
        }
    }
}
