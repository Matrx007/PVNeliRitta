package com.pv.neliritta;
// Written by Rainis Randmaa

import com.pv.neliritta.gui.Component;
import com.pv.neliritta.gui.states.*;
import processing.core.PConstants;

/*
 * This class draws the com.pv.neliritta.GUI and talks with 'com.pv.neliritta.backend.BackEnd' class (written by Gregor Suurvarik), which reacts to user
 * input and (if playing against the computer, ) takes turns as well.
 * */
public class GUI implements Component {

    /* Used to access the engine, this will be passed to he 'com.pv.neliritta.GUI' class through its constructor */
    public final Main main;

    /* Used to control the program flow */
    public enum State {
        MAIN_MENU,
        IN_GAME,
        LOAD_GAME,
        SAVE_GAME,
        MATCH_OPTIONS,
        IN_GAME_PAUSE_MENU
    }
    public State state = State.MAIN_MENU;

    /* Game states */
    public final MainMenu mainMenu = new MainMenu();
    public final InGame inGame = new InGame();
    public final LoadGame loadGame = new LoadGame();
    public final SaveGame saveGame = new SaveGame();
    public final MatchOptions matchOptions = new MatchOptions();
    public final InGamePauseMenu inGamePauseMenu = new InGamePauseMenu();

    public GUI(Main main) {
        this.main = main;
    }

    public void setup() {
        mainMenu.setup(this);
        inGame.setup(this);
        loadGame.setup(this);
        saveGame.setup(this);
        matchOptions.setup(this);
        inGamePauseMenu.setup(this);
    }

    @Override
    public void resize() {
        mainMenu.resize();
        inGame.resize();
        loadGame.resize();
        saveGame.resize();
        matchOptions.resize();
        inGamePauseMenu.resize();
    }

    @Override
    public void update(double deltaTime) {
        switch (state) {
            case MAIN_MENU:
                mainMenu.update(this, deltaTime);
                break;
            case IN_GAME:
                inGame.update(this, deltaTime);
                break;
            case LOAD_GAME:
                loadGame.update(this, deltaTime);
                break;
            case SAVE_GAME:
                saveGame.update(this, deltaTime);
                break;
            case MATCH_OPTIONS:
                matchOptions.update(this, deltaTime);
                break;
            case IN_GAME_PAUSE_MENU:
                inGamePauseMenu.update(this, deltaTime);
                break;
            default:
                /* Can this ever even happen? */
                System.out.println("A big OOF occurred");
                break;
        }
    }

    @Override
    public void render() {
        main.getGame().rectMode(PConstants.CENTER);
        main.getGame().ellipseMode(PConstants.CENTER);

        main.getGame().pushMatrix();
        main.getGame().translate(main.getGame().pixelWidth / 2f, main.getGame().pixelHeight / 2f);
        switch (state) {
            case MAIN_MENU:
                mainMenu.render(this);
                break;
            case IN_GAME:
                inGame.render(this);
                break;
            case LOAD_GAME:
                loadGame.render(this);
                break;
            case SAVE_GAME:
                saveGame.render(this);
                break;
            case MATCH_OPTIONS:
                matchOptions.render(this);
                break;
            case IN_GAME_PAUSE_MENU:
                inGamePauseMenu.render(this);
                break;
            default:
                /* Can this ever even happen? */
                System.out.println("A big OOF occurred");
                break;
        }

        main.getGame().popMatrix();
    }
}
