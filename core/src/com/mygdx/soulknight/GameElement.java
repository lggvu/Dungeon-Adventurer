package com.mygdx.soulknight;

import com.google.gson.JsonObject;

public interface GameElement {
    public JsonObject stateDict();
    public void loadStateDict(JsonObject jsonObject);
}
