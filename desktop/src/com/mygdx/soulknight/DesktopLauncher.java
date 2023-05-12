package com.mygdx.soulknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("My Game");
        config.setWindowedMode(800, 480);
        new Lwjgl3Application(new MyGdxGame(), config);
        /*
        asdw to move
        space to shoot
        f to collect gun
        */
    }
    
}
