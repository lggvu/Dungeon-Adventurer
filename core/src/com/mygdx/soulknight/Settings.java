package com.mygdx.soulknight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Settings {
	public static int fps = 60;
	public static Music music = Gdx.audio.newMusic(Gdx.files.internal("music/After-School.mp3"));
	public static Music sound = Gdx.audio.newMusic(Gdx.files.internal("music/After-School.mp3"));
	public static void updateMusicVolume(float x) {
		music.setVolume(x);
	}
	public static void updateSoundVolume(float x) {
		sound.setVolume(x);
	}

	public static void updateFPS(int x) {
		fps = x;
	}
	public static void updateMusic(Music m) {
		music = m;
	}
}
