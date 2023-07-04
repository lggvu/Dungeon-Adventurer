package com.mygdx.soulknight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.FileWriter;
import java.util.HashMap;

public class Settings {
	public enum GameButton {
		DODGE,
		SPECIAL_SKILL
	}
	private final static String SETTING_PATH = "info/settings.json";

	public static HashMap<GameButton, Integer> keyboardSetting = new HashMap<>();

	public static void loadSetting() {
		try {
			JsonObject json = new Gson().fromJson(Gdx.files.internal(SETTING_PATH).reader(), JsonObject.class);
			fps = json.get("FPS").getAsInt();
			keyboardSetting.put(GameButton.DODGE, json.get("DODGE_KEY_CODE").getAsInt());
			keyboardSetting.put(GameButton.SPECIAL_SKILL, json.get("SPEC_KEY_CODE").getAsInt());
			music.setVolume(json.get("MUSIC_VOLUME").getAsFloat());
			sound.setVolume(json.get("SOUND_VOLUME").getAsFloat());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveSetting() {
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("FPS", new JsonPrimitive(fps));
		jsonObject.add("SPEC_KEY_CODE", new JsonPrimitive(keyboardSetting.get(GameButton.SPECIAL_SKILL)));
		jsonObject.add("DODGE_KEY_CODE", new JsonPrimitive(keyboardSetting.get(GameButton.DODGE)));
		jsonObject.add("MUSIC_VOLUME", new JsonPrimitive(music.getVolume()));
		jsonObject.add("SOUND_VOLUME", new JsonPrimitive(sound.getVolume()));
		String json = gson.toJson(jsonObject);
		Gdx.files.external(SETTING_PATH).writeString(json, false);
	}

	public static void updateDodgeKeyCode(int keyCode) {
		keyboardSetting.remove(GameButton.DODGE);
		keyboardSetting.put(GameButton.DODGE, keyCode);
	}

	public static void updateSpecKeyCode(int keyCode) {
		keyboardSetting.remove(GameButton.SPECIAL_SKILL);
		keyboardSetting.put(GameButton.SPECIAL_SKILL, keyCode);
	}

	public static int getKeyCode(GameButton btn) {
		return keyboardSetting.get(btn);
	}

	public static int fps = 60;
	public static Music music = Gdx.audio.newMusic(Gdx.files.internal("music/After-School.mp3"));
	public static Music sound = Gdx.audio.newMusic(Gdx.files.internal("music/After-School.mp3"));

	public static void updateMusicVolume(float x) {
		music.setVolume(x);
	}

	public static void updateSoundVolume(float x) {
		sound.setVolume(x);
	}
}
