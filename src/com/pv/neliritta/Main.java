package com.pv.neliritta;
// Written by Rainis Randmaa

import com.pv.neliritta.gui.ingame.Board;
import com.ydgames.mxe.Game;
import com.ydgames.mxe.GameContainer;
import processing.core.PConstants;

// MX Engine is a custom (mostly) 2D game engine which is built on top of the Processing framework.
// It provides better way to handle user input and an object management system (which I will not be using in this project)
public class Main extends GameContainer {
    public static void main(String[] args) {
        Game.createGame(800, 600, new Main(), 60f, PConstants.P2D);
    }

    private GUI gui;

    @Override
    public void setup() {
        /* ### LOAD FONTS ### */
        FontManager.loadFont(this, "button-font", "assets/fonts/PottaOne-Regular.ttf", 64f);

        /* ### ENGINE STUFF ### */
        getGame().getSurface().setResizable(true);

        /* ### GAME STRUCTURE ### */
        gui = new GUI(this);
        gui.board = new Board(this, 5, 5);

        gui.setup();
    }

    /* For detecting window resize */
    int prevPixelWidth, prevPixelHeight;

    @Override
    public void update(double v) {
        if(getGame().pixelWidth != prevPixelWidth ||
                getGame().pixelHeight != prevPixelHeight) {
            gui.resize();

            prevPixelWidth = getGame().pixelWidth;
            prevPixelHeight = getGame().pixelHeight;
        }

        gui.update(v);
    }

    @Override
    public void render() {
        getGame().background(200, 200, 200);

        gui.render();
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
