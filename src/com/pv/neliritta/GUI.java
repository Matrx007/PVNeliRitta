package com.pv.neliritta;// Written by Rainis Randmaa

import com.ydgames.mxe.GameContainer;
import com.pv.neliritta.gui.components.Button;

import java.util.HashMap;
import java.util.Map;

/*
 * This class draws the com.pv.neliritta.GUI and talks with 'com.pv.neliritta.BackEnd' class (written by Gregor Suurvarik), which reacts to user
 * input and (if playing against the computer, ) takes turns as well.
 * */
public class GUI {

    /* Used to access the engine, this will be passed to he 'com.pv.neliritta.GUI' class through its constructor */
    GameContainer gameContainer;

    /* Used to control the program flow */
    enum State {
        MAIN_MENU;
    }
    State state = State.MAIN_MENU;

    public GUI(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public void setup() {
        setup_mainMenu();
    }

    public void update() {
        switch (state) {
            case MAIN_MENU:
                update_mainMenu();
                break;
            default:
                /* Can this ever even happen? */
                System.out.println("A big OOF occurred");
                break;
        }
    }

    public void render() {
        switch (state) {
            case MAIN_MENU:
                render_mainMenu();
                break;
            default:
                /* Can this ever even happen? */
                System.out.println("A big OOF occurred");
                break;
        }
    }

    /* #################
    *  ### MAIN MENU ###
    * ##################
    * */

    HashMap<String, Button> mainMenu_buttons;

    public void setup_mainMenu() {


        /* ### CREATE UI #### */

        mainMenu_buttons = new HashMap<>();

        int numButtons = 4;

        int placementX = ( gameContainer.getGame().pixelWidth  - 384 ) / 2;
        int placementY = ( gameContainer.getGame().pixelHeight - ( 64 + 16 ) * numButtons ) / 2;

        mainMenu_buttons.put("playerVsPlayer", new Button(gameContainer, placementX, placementY,
                384, 64, "PLAYER VS PLAYER", () -> {}));

        placementY += 64 + 16;

        mainMenu_buttons.put("playerVsComputer", new Button(gameContainer, placementX, placementY,
                384, 64, "PLAYER VS COMPUTER", () -> {}));

        placementY += 64 + 16;

        mainMenu_buttons.put("loadGame", new Button(gameContainer, placementX, placementY,
                384, 64, "LOAD GAME", () -> {}));
    }

    public void update_mainMenu() {
        for (Map.Entry<String, Button> entry : mainMenu_buttons.entrySet()) {
            if (entry != null) {
                entry.getValue().update();
            }
        }
    }

    public void render_mainMenu() {
        for (Map.Entry<String, Button> entry : mainMenu_buttons.entrySet()) {
            if (entry != null) {
                entry.getValue().render();
            }
        }
    }

    /* #####################
     *  ### IN-GAME MENU ###
     * #####################
     * */
}
