package com.mygdx.soulknight.ability;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.soulknight.entity.Character.Player.Player;
import com.mygdx.soulknight.entity.Character.SimpleCharacter;

import java.util.ArrayList;

public class Ability  {
    public enum AbilityEnum implements AbilityDrawer {
        MAX_HP_INCREASE("buff/Incease Max Health.png", 0, 0),
        MAX_ARMOR_INCREASE("buff/Increase Shield.png", 0, 0),
        DAMAGE_INCREASE("buff/Increase Damage.png", 0, 0),
        NUM_WALL_COLLIDE_INCREASE("buff/Buff Bullet Bounce.png", 0, 0),
        POISON_IMMUNITY("buff/Poison Immune.png", 0, 0),
        FIRE_IMMUNITY("buff/Fire Immune.png", 0, 0),
        LIGHTNING_IMMUNITY("buff/Light Immune.png", 0, 0),
        STUN_IMMUNITY("buff/Stun Immune.png", 0, 0),
        MAX_WEAPON_INCREASE("buff/Increase Weapon Slot.png", 0, 0),
        MAX_MANA_INCREASE("buff/Increase Max Mana.png", 0, 0);
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
    private boolean isStunImmunity = false;
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
        abilityEnumArrayList.add(abilityEnum);
        switch (abilityEnum) {
            case FIRE_IMMUNITY:
                isFireImmunity = true;
                return;
            case POISON_IMMUNITY:
                isPoisonImmunity = true;
                return;
            case LIGHTNING_IMMUNITY:
                isLightningImmunity = true;
                return;
            case STUN_IMMUNITY:
                isStunImmunity = true;
                return;
            case DAMAGE_INCREASE:
                damageIncrease += 1;
                return;
            case MAX_WEAPON_INCREASE:
                numWeaponIncrease += 1;
                return;
            case MAX_HP_INCREASE:
                hpIncrease += 1;
                character.setCurrentHP(character.getCurrentHP() + 1);
                return;
            case MAX_ARMOR_INCREASE:
                if (character instanceof Player) {
                    armorIncrease += 1;
                    ((Player) character).setCurrentArmor(((Player) character).getCurrentArmor() + 1);
                }
                return;
            case NUM_WALL_COLLIDE_INCREASE:
                wallCollideIncrease += 1;
                return;
            case MAX_MANA_INCREASE:
                if (character instanceof Player) {
                    manaIncrease += 100;
                    ((Player) character).setCurrentMana(((Player) character).getCurrentMana() + 100);
                }
                return;
        }
    };
    public void removeAbility(SimpleCharacter character, AbilityEnum abilityEnum) {
        if (!abilityEnumArrayList.contains(abilityEnum)) {
            return;
        }
        abilityEnumArrayList.remove(abilityEnum);
        switch (abilityEnum) {
            case FIRE_IMMUNITY:
                isFireImmunity = false;
                return;
            case DAMAGE_INCREASE:
                damageIncrease -= 1;
                return;
            case MAX_HP_INCREASE:
                hpIncrease -= 1;
                if (character.getCurrentMaxHP() < character.getCurrentHP()) {
                    character.setCurrentHP(character.getCurrentMaxHP());
                }
                return;
            case POISON_IMMUNITY:
                isPoisonImmunity = false;
                return;
            case LIGHTNING_IMMUNITY:
                isLightningImmunity = false;
                return;
            case STUN_IMMUNITY:
                isStunImmunity = false;
                return;
            case MAX_ARMOR_INCREASE:
                if (character instanceof Player) {
                    armorIncrease -= 1;
                    if (((Player) character).getCurrentArmor() > ((Player) character).getCurrentMaxArmor()) {
                        ((Player) character).setCurrentArmor(((Player) character).getCurrentMaxArmor());
                    }
                }
                return;
            case MAX_WEAPON_INCREASE:
                numWeaponIncrease -= 1;
                return;
            case NUM_WALL_COLLIDE_INCREASE:
                wallCollideIncrease -= 1;
                return;
            case MAX_MANA_INCREASE:
                if (character instanceof Player) {
                    manaIncrease -= 100;
                    if (((Player) character).getCurrentMaxMana() < ((Player) character).getCurrentMana()) {
                        ((Player) character).setCurrentMana(((Player) character).getCurrentMaxMana());
                    }
                }
                return;
        }
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

    public boolean isStunImmunity() {
        return isStunImmunity;
    }
}
