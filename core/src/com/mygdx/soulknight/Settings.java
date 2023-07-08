package com.mygdx.soulknight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Settings {

	public final static String STATE_DICT_PATH = "assets/info/state_dict.json";

	public enum GameButton {
		DODGE,
		SPECIAL_SKILL
	}

	private final static String SETTING_PATH = "assets/info/settings.json";

	public static HashMap<GameButton, Integer> keyboardSetting = new HashMap<>();

	public static void deleteStateDict() {
		if (new File(STATE_DICT_PATH).exists()) {
			Gdx.files.local(STATE_DICT_PATH).delete();
			System.out.println("DELETE STATE DICT");
		}
	}

	public static void loadSetting() {
		try {
			JsonObject json = new Gson().fromJson(Gdx.files.internal(SETTING_PATH).reader(), JsonObject.class);
			System.out.println("LOAD SETTING");
			fps = json.get("FPS").getAsInt();
			soundVolume = json.get("SOUND_VOLUME").getAsFloat();
			keyboardSetting.put(GameButton.DODGE, json.get("DODGE_KEY_CODE").getAsInt());
			keyboardSetting.put(GameButton.SPECIAL_SKILL, json.get("SPEC_KEY_CODE").getAsInt());
			music.setVolume(json.get("MUSIC_VOLUME").getAsFloat());
			addSound(explosionSound);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveSetting() {
		Gson gson = new Gson();
		System.out.println("SAVE SETTING");
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("FPS", new JsonPrimitive(fps));
		jsonObject.add("SPEC_KEY_CODE", new JsonPrimitive(keyboardSetting.get(GameButton.SPECIAL_SKILL)));
		jsonObject.add("DODGE_KEY_CODE", new JsonPrimitive(keyboardSetting.get(GameButton.DODGE)));
		jsonObject.add("MUSIC_VOLUME", new JsonPrimitive(music.getVolume()));
		jsonObject.add("SOUND_VOLUME", new JsonPrimitive(soundVolume));
		String json = gson.toJson(jsonObject);
		Gdx.files.local(SETTING_PATH).writeString(json, false);
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
	public static float soundVolume = 0.3f;
	public static Music music = Gdx.audio.newMusic(Gdx.files.internal("music/After-School.mp3"));
	public static Music explosionSound = Gdx.audio.newMusic(Gdx.files.internal("sound-effect/explosion.mp3"));

	private static ArrayList<Music> allSound = new ArrayList<>();

	public final static Skin skin = new Skin(Gdx.files.internal("button/freezing-ui.json"));

	public static Music addSound(Music sound) {
		allSound.add(sound);
		sound.setVolume(soundVolume);
		return sound;
	}

	public static void updateMusicVolume(float x) {
		music.setVolume(x);
	}

	public static void updateSoundVolume(float x) {
		soundVolume = x;
		for (Music sound:allSound){
			sound.setVolume(x);
		}
	}
}
