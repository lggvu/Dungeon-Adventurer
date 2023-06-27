package com.mygdx.soulknight;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;

public class DesktopLauncher {
    public static void main (String[] arg) {
        System.out.println("TEST");
        System.out.println(new Vector2(-1, 1).angleDeg(new Vector2(1, 0)));
        System.out.println(new Vector2(-1, -1).angleDeg(new Vector2(1, 0)));
        System.out.println(new Vector2(1, -1).angleDeg(new Vector2(1, 0)));
//        System.out.println();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("My Game");
        config.setWindowedMode(1200, 670);
        new Lwjgl3Application(new SoulKnight(), config);
    }
}
