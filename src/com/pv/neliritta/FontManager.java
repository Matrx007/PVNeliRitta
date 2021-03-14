package com.pv.neliritta;
// Written by Rainis Randmaa

import com.ydgames.mxe.GameContainer;
import processing.core.PFont;

import java.util.HashMap;

public class FontManager {
    public static final HashMap<String, PFont> loadedFonts = new HashMap<>();

    public static void loadFont(GameContainer gameContainer, String name, String file, float size) {
        loadedFonts.put(name, gameContainer.getGame().createFont(file, size, true));
    }
}
