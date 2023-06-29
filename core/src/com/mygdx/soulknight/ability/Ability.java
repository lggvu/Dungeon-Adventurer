package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

import java.util.ArrayList;

public class Ability  {
    public enum AbilityEnum implements AbilityDrawer {
        MAX_HP_INCREASE("ppp.png", 0, 0),
        MAX_ARMOR_INCREASE("hire-designer.png", 0, 0),
        DAMAGE_INCREASE("hire-designer.png", 0, 0),
        NUM_WALL_COLLIDE_INCREASE("dark_menu.png", 0, 0),
        POISON_IMMUNITY("hire-designer.png", 0, 0),
        FIRE_IMMUNITY("hire-designer.png", 0, 0),
        LIGHTNING_IMMUNITY("hire-designer.png", 0, 0),
        MAX_WEAPON_INCREASE("start_game.png", 0, 0),
        MAX_MANA_INCREASE("start_game.png", 0, 0);
        private TextureRegion texture;
        private float totalTimeCooldown;
        private float currentTimeCooldown;

        AbilityEnum(String texturePath, float totalTimeCooldown, float currentTimeCooldown) {
            this.totalTimeCooldown = totalTimeCooldown;
            this.currentTimeCooldown = currentTimeCooldown;
            texture = new TextureRegion(new Texture(texturePath));
        }
        @Override
        public TextureRegion getTextureCoolDown() {
            return texture;
        }
        @Override
        public float getTotalTimeCoolDown() {
            return totalTimeCooldown;
        }
        @Override
        public float getCurrentTimeCoolDown() {
            return currentTimeCooldown;
        }
    }

    private boolean isFireImmunity = false;
    private boolean isPoisonImmunity = false;
    private boolean isLightningImmunity = false;
    private int damageIncrease = 0;
    private int hpIncrease = 0;
    private int armorIncrease = 0;
    private int numWeaponIncrease = 0;
    private int wallCollideIncrease = 0;
    private int manaIncrease = 0;
    private ArrayList<AbilityEnum> abilityEnumArrayList = new ArrayList<>();
    public void addAbility(SimpleCharacter character, AbilityEnum abilityEnum) {
        if (abilityEnumArrayList.contains(abilityEnum)) {
            return;
        }
        switch (abilityEnum) {
            case FIRE_IMMUNITY:
                isFireImmunity = true;
            case POISON_IMMUNITY:
                isPoisonImmunity = true;
            case LIGHTNING_IMMUNITY:
                isLightningImmunity = true;
            case DAMAGE_INCREASE:
                damageIncrease += 1;
            case MAX_WEAPON_INCREASE:
                numWeaponIncrease += 1;
            case MAX_HP_INCREASE:
                hpIncrease += 1;
                character.setCurrentHP(character.getCurrentHP() + 1);
            case MAX_ARMOR_INCREASE:
                if (character instanceof Player) {
                    armorIncrease += 1;
                    ((Player) character).setCurrentArmor(((Player) character).getCurrentArmor() + 1);
                }
            case NUM_WALL_COLLIDE_INCREASE:
                wallCollideIncrease += 1;
            case MAX_MANA_INCREASE:
                if (character instanceof Player) {
                    manaIncrease += 100;
                    ((Player) character).setCurrentMana(((Player) character).getCurrentMana() + 100);
                }
        }
        abilityEnumArrayList.add(abilityEnum);
    };
    public void removeAbility(SimpleCharacter character, AbilityEnum abilityEnum) {
        if (!abilityEnumArrayList.contains(abilityEnum)) {
            return;
        }
        switch (abilityEnum) {
            case FIRE_IMMUNITY:
                isFireImmunity = false;
            case DAMAGE_INCREASE:
                damageIncrease -= 1;
            case MAX_HP_INCREASE:
                hpIncrease -= 1;
                if (character.getCurrentMaxHP() < character.getCurrentHP()) {
                    character.setCurrentHP(character.getCurrentMaxHP());
                }
            case POISON_IMMUNITY:
                isPoisonImmunity = false;
            case LIGHTNING_IMMUNITY:
                isLightningImmunity = false;
            case MAX_ARMOR_INCREASE:
                if (character instanceof Player) {
                    armorIncrease -= 1;
                    if (((Player) character).getCurrentArmor() > ((Player) character).getCurrentMaxArmor()) {
                        ((Player) character).setCurrentArmor(((Player) character).getCurrentMaxArmor());
                    }
                }
            case MAX_WEAPON_INCREASE:
                numWeaponIncrease -= 1;
            case NUM_WALL_COLLIDE_INCREASE:
                wallCollideIncrease -= 1;
            case MAX_MANA_INCREASE:
                if (character instanceof Player) {
                    manaIncrease -= 100;
                    if (((Player) character).getCurrentMaxMana() < ((Player) character).getCurrentMana()) {
                        ((Player) character).setCurrentMana(((Player) character).getCurrentMaxMana());
                    }
                }
        }
        abilityEnumArrayList.remove(abilityEnum);
    }

    public int getManaIncrease() {
        return manaIncrease;
    }

    public boolean isFireImmunity() {
        return isFireImmunity;
    }

    public boolean isPoisonImmunity() {
        return isPoisonImmunity;
    }

    public boolean isLightningImmunity() {
        return isLightningImmunity;
    }

    public int getDamageIncrease() {
        return damageIncrease;
    }

    public int getHpIncrease() {
        return hpIncrease;
    }

    public int getArmorIncrease() {
        return armorIncrease;
    }

    public int getNumWeaponIncrease() {
        return numWeaponIncrease;
    }

    public int getWallCollideIncrease() {
        return wallCollideIncrease;
    }

    public ArrayList<AbilityEnum> getAbilityEnumArrayList() {
        return abilityEnumArrayList;
    }
}
