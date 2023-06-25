package com.mygdx.soulknight.entity.Effect;

import com.mygdx.soulknight.entity.Character.SimpleCharacter;
import com.mygdx.soulknight.entity.Map.WorldMap;

public abstract class CharacterEffect extends Effect {
    public abstract void update(float deltaTime, SimpleCharacter affectedCharacter);
}
