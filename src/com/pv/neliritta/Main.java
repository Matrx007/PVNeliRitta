package com.pv.neliritta;
// Written by Rainis Randmaa

import com.pv.neliritta.gui.ingame.Board;
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

    // The smallest dimension of the window
    // tl;dr: guiSize = min(windowWidth, windowHeight)
    public int guiSize;

    @Override
    public void setup() {
        /* ### LOAD FONTS ### */
        FontManager.loadFont(this, "button-font", "assets/fonts/PottaOne-Regular.ttf", 64f);

        /* ### LOAD GRAPHICS ### */
        GraphicsManager.loadImage(this, "background-particle", "assets/graphics/particle.png");
        GraphicsManager.loadImage(this, "particle-circle", "assets/graphics/particle.png");
        GraphicsManager.loadImage(this, "particle-mountain", "assets/graphics/background_components/mountain.png");

        GraphicsManager.loadImage(this, "slot", "assets/graphics/slot.png");


        /* ### ENGINE STUFF ### */
        getGame().getSurface().setResizable(true);

        /* Appearance */
        background = new AbstractSpaceBackground(this);

        /* ### GAME STRUCTURE ### */
        gui = new GUI(this);
        gui.board = new Board(this, 5, 5);

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
        getGame().background(93, 177, 179);

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
