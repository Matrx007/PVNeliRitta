package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.localization.Localization;

import java.util.HashMap;
import java.util.Map;

public class InGamePauseMenu {
    HashMap<String, Button> inGamePauseMenu_buttons;

    public void setup(GUI gui) {

        /* ### CREATE UI #### */

        inGamePauseMenu_buttons = new HashMap<>();

        float buttonWidth = 384;
        float buttonHeight = 64;
        float buttonSpacing = 16;

        inGamePauseMenu_buttons.put("continue", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * -2,
                () -> buttonWidth, () -> buttonHeight, Localization.reference("pause menu", "continue"),
                () -> gui.state = GUI.State.IN_GAME));

        inGamePauseMenu_buttons.put("new_game", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * 0,
                () -> buttonWidth, () -> buttonHeight, Localization.reference("pause menu", "new_game"),
                () -> {
                    gui.inGame.board.reset();
                    gui.state = GUI.State.IN_GAME;
                } ));

        inGamePauseMenu_buttons.put("save", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * 1,
                () -> buttonWidth, () -> buttonHeight,  Localization.reference("pause menu", "save"),
                () -> {
                    gui.state = GUI.State.SAVE_GAME;
                }));

        inGamePauseMenu_buttons.put("main_menu", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * 2,
                () -> buttonWidth, () -> buttonHeight,  Localization.reference("pause menu", "main_menu"),
                () -> {
                    gui.inGame.board.reset();
                    gui.state = GUI.State.MAIN_MENU;
                }));
    }

    public void update(GUI gui, double deltaTime) {
        for (Map.Entry<String, Button> entry : inGamePauseMenu_buttons.entrySet()) {
            if (entry != null) {
                entry.getValue().update(deltaTime);
            }
        }
    }

    public void render(GUI gui) {
        gui.main.getGame().translate(0, 0, 40f);

        for (Map.Entry<String, Button> entry : inGamePauseMenu_buttons.entrySet()) {
            if (entry != null) {
                entry.getValue().render();
            }
        }

        gui.main.getGame().translate(0, 0, -40f);
    }

    public void resize() {
        /* IN_GAME_PAUSE_MENU */
        inGamePauseMenu_buttons.forEach((name, button) -> button.resize());
    }
}
