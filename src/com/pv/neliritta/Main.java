package com.pv.neliritta;
// Written by Rainis Randmaa

import com.pv.neliritta.gui.ingame.Board;
import com.pv.neliritta.localization.Localization;
import com.ydgames.mxe.Game;
import com.ydgames.mxe.GameContainer;
import processing.core.PConstants;
import processing.core.PImage;

// MX Engine is a custom (mostly) 2D game engine which is built on top of the Processing framework.
// It provides better way to handle user input and an object management system (which I will not be using in this project)
public class Main extends GameContainer {
    public static void main(String[] args) {
        Game.createGame(800, 600, new Main(), 60f, PConstants.P3D);
    }

    /* Engine stuff */
    private GUI gui;

    /* Appearance */
    AbstractSpaceBackground background;

    /* Localization */
    // Use the default language, English
    static {
        Localization.localize(Localization.languageChoices()[0]);
    }

    // The smallest dimension of the window
    // tl;dr: guiSize = min(windowWidth, windowHeight)
    public int guiSize;

    @Override
    public void setup() {
        /* ### LOAD FONTS ### */
        FontManager.loadFont(this, "button-font", "assets/fonts/AnnieUseYourTelescope-Regular.ttf", 64f);

        /* ### LOAD GRAPHICS ### */
        GraphicsManager.loadImage(this, "background-particle", "assets/graphics/particle.png");
        GraphicsManager.loadImage(this, "particle-circle", "assets/graphics/particle2.png");
        GraphicsManager.loadImage(this, "particle-mountain", "assets/graphics/background_components/mountain.png");

        GraphicsManager.loadImage(this, "slot", "assets/graphics/slot3.png");
        GraphicsManager.loadImage(this, "slot-bottom", "assets/graphics/slot4.png");
        GraphicsManager.loadImage(this, "slot-top", "assets/graphics/slot5.png");
        GraphicsManager.loadImage(this, "ball_red", "assets/graphics/ball_red.png");
        GraphicsManager.loadImage(this, "ball_blue", "assets/graphics/ball_blue.png");

        GraphicsManager.loadImage(this, "button", "assets/graphics/button2.png");
        GraphicsManager.loadImage(this, "button-hover", "assets/graphics/button3.png");
        GraphicsManager.loadImage(this, "button-active", "assets/graphics/button4.png");

        GraphicsManager.loadImage(this, "options-left",  "assets/graphics/left.png");
        GraphicsManager.loadImage(this, "options-left-hover",  "assets/graphics/left2.png");
        GraphicsManager.loadImage(this, "options-right", "assets/graphics/right.png");
        GraphicsManager.loadImage(this, "options-right-hover", "assets/graphics/right2.png");

        GraphicsManager.loadImage(this, "vignette", "assets/graphics/vignette.png");
        GraphicsManager.loadImage(this, "border", "assets/graphics/border.png");


        /* ### ENGINE STUFF ### */
        getGame().getSurface().setResizable(true);

        /* Appearance */
        background = new AbstractSpaceBackground(this);

        /* ### GAME STRUCTURE ### */
        gui = new GUI(this);

        gui.setup();
    }

    /* For detecting window resize */
    int prevPixelWidth, prevPixelHeight;

    @Override
    public void update(double deltaTime) {
        background.update(deltaTime);

        if(getGame().pixelWidth != prevPixelWidth ||
                getGame().pixelHeight != prevPixelHeight) {
            guiSize = Math.min(getGame().pixelWidth, getGame().pixelHeight);

            gui.resize();

            prevPixelWidth = getGame().pixelWidth;
            prevPixelHeight = getGame().pixelHeight;
        }

        gui.update(deltaTime);
    }

    @Override
    public void render() {
//        getGame().background(93, 177, 179);
//        getGame().background(0x534a75);
//        getGame().background(0xcb);
//        getGame().background(230, 196, 110);
//        getGame().background(204, 208, 222);
        getGame().background(230);

        background.render(() -> gui.render());

//        gui.render();
    }









    @Override
    public void init() {

    }

    @Override
    public void updateTick() {
//        Because this game is turn based, we don't need to update the game at a constant rate
    }

    @Override
    public void settings() {

    }
}
