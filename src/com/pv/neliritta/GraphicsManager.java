package com.pv.neliritta;
// Written by Rainis Randmaa

import com.ydgames.mxe.GameContainer;
import processing.core.PImage;

import java.util.HashMap;

/*
* Used to load and access images globally.
* */
public class GraphicsManager {
    public static HashMap<String, PImage> loadedGraphics = new HashMap<>();
    public static HashMap<String, PImage[]> loadedAnimations = new HashMap<>();

    public static void loadImage(GameContainer gameContainer, String name, String file) {
        loadedGraphics.put(name, gameContainer.getGame().loadImage(file));
    }

    public static void loadAnimation(GameContainer gameContainer, String name, String ... file) {
        PImage[] frames = new PImage[file.length];
        for(int i = 0; i < file.length; i++) {
            frames[i] = gameContainer.getGame().loadImage(file[i]);
        }
        loadedAnimations.put(name, frames);
    }
}
