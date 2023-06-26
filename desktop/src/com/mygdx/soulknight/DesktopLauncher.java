package com.mygdx.soulknight;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.awt.Dimension;
import java.awt.Toolkit;


public class DesktopLauncher {
    public static void main (String[] arg) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("My Game");
        config.setWindowedMode(screenWidth, screenHeight);
        new Lwjgl3Application(new SoulKnight(), config);
    }
    
}
