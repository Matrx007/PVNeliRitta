package com.pv.neliritta;// Written by Rainis Randmaa

import com.pv.neliritta.gui.GameComponent;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.gui.components.Label;
import com.pv.neliritta.gui.ingame.Board;
import com.ydgames.mxe.GameContainer;

import java.util.HashMap;
import java.util.Map;

/*
 * This class draws the com.pv.neliritta.GUI and talks with 'com.pv.neliritta.backend.BackEnd' class (written by Gregor Suurvarik), which reacts to user
 * input and (if playing against the computer, ) takes turns as well.
 * */
public class GUI implements GameComponent {

    /* Used to access the engine, this will be passed to he 'com.pv.neliritta.GUI' class through its constructor */
    GameContainer gameContainer;

    /* Used to control the program flow */
    enum State {
        MAIN_MENU,
        IN_GAME
    }
    State state = State.MAIN_MENU;

    /* Game board, will be modified by 'BackEnd' */
    public Board board;

    public GUI(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public void setup() {
        setup_mainMenu();
        setup_inGame();
    }

    @Override
    public void resize() {
        /* IN_GAME */
        board.resize();
        inGame_whoseTurn.resize();

        /* MAIN_MENU */
        mainMenu_buttons.forEach((name, button) -> {
            button.resize();
        });
    }

    @Override
    public void update(double deltaTime) {
        switch (state) {
            case MAIN_MENU:
                update_mainMenu(deltaTime);
                break;
            case IN_GAME:
                update_inGame(deltaTime);
                break;
            default:
                /* Can this ever even happen? */
                System.out.println("A big OOF occurred");
                break;
        }
    }

    @Override
    public void render() {
        switch (state) {
            case MAIN_MENU:
                render_mainMenu();
                break;
            case IN_GAME:
                render_inGame();
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

        int numButtons = 5;

        mainMenu_buttons.put("playerVsPlayer", new Button(gameContainer,
                () -> ( gameContainer.getGame().pixelWidth - 384 ) / 2f,
                () -> ( gameContainer.getGame().pixelHeight - ( 64 + 16 ) * numButtons ) / 2f + (64 + 16) * 0,
                () -> 384, () -> 64, "MÄNGIJA VS MÄNGIJA",
                () -> state = State.IN_GAME ));

        mainMenu_buttons.put("playerVsComputer", new Button(gameContainer,
                () -> ( gameContainer.getGame().pixelWidth - 384 ) / 2f,
                () -> ( gameContainer.getGame().pixelHeight - ( 64 + 16 ) * numButtons ) / 2f + ( 64 + 16 ) * 1,
                () -> 384, () -> 64, "MÄNGIJA VS ARVUTI", () -> {}));

        mainMenu_buttons.put("loadGame", new Button(gameContainer,
                () -> ( gameContainer.getGame().pixelWidth - 384 ) / 2f,
                () -> ( gameContainer.getGame().pixelHeight - ( 64 + 16 ) * numButtons ) / 2f + ( 64 + 16 ) * 2,
                () -> 384, () -> 64, "AVA VARASEM MÄNG", () -> {}));

        mainMenu_buttons.put("quit", new Button(gameContainer,
                () -> ( gameContainer.getGame().pixelWidth - 384 ) / 2f,
                () -> ( gameContainer.getGame().pixelHeight - ( 64 + 16 ) * numButtons ) / 2f + ( 64 + 16 ) * 4,
                () -> 384, () -> 64, "LAHKU MÄNGUST", () -> {}));
    }

    public void update_mainMenu(double deltaTime) {
        for (Map.Entry<String, Button> entry : mainMenu_buttons.entrySet()) {
            if (entry != null) {
                entry.getValue().update(deltaTime);
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

    Label inGame_whoseTurn;

    public void setup_inGame() {
        inGame_whoseTurn = new Label(gameContainer,
                () -> (gameContainer.getGame().pixelWidth - 256) / 2f,
                () -> 0, () -> 256, () -> 64, "");
    }

    public void update_inGame(double deltaTime) {
        board.update(deltaTime);

        int whoseTurn = board.getWhoseTurn();
        if(whoseTurn == 1) {
            inGame_whoseTurn.text = "Player 1";
        } else if(whoseTurn == 2) {
            inGame_whoseTurn.text = board.isAgainstComputer() ? "Computer" : "Player 2";
        }

        inGame_whoseTurn.update(deltaTime);
    }

    public void render_inGame() {
        board.render();
        inGame_whoseTurn.render();
    }
}
