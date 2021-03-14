package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.localization.Localization;

import java.util.HashMap;
import java.util.Map;

public class MainMenu implements State {
    HashMap<String, Button> mainMenu_buttons;

    public void setup(GUI gui) {


        /* ### CREATE UI #### */

        mainMenu_buttons = new HashMap<>();

        int numButtons = 5;

        float buttonWidth = 384;
        float buttonHeight = 64;
        float buttonSpacing = 16;

        mainMenu_buttons.put("playerVsPlayer", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * -2,
                () -> buttonWidth, () -> buttonHeight, Localization.reference("main menu", "player_vs_player"),
                () -> {
                    gui.matchOptions.againstComputer = false;
                    gui.state = GUI.State.MATCH_OPTIONS;
                } ));

        mainMenu_buttons.put("playerVsComputer", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * -1,
                () -> buttonWidth, () -> buttonHeight, Localization.reference("main menu", "player_vs_computer"),
                () -> {
                    gui.matchOptions.againstComputer = true;
                    gui.state = GUI.State.MATCH_OPTIONS;
                } ));

        mainMenu_buttons.put("loadGame", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * 0,
                () -> buttonWidth, () -> buttonHeight,  Localization.reference("main menu", "load_game"),
                () -> {
                    gui.state = GUI.State.LOAD_GAME;
                }));

        mainMenu_buttons.put("quit", new Button(gui.main,
                () -> 0,
                () -> (buttonHeight + buttonSpacing) * 2,
                () -> buttonWidth, () -> buttonHeight,  Localization.reference("main menu", "quit"),
                () -> {
                    gui.main.getGame().exit();

                    // TODO: Save game before exit, maybe?
                }));



        // ### LANGUAGE SELECTION ###
        String[] languageChoices = Localization.languageChoices();
        boolean evenOptions = (languageChoices.length % 2) == 0;

        float offsetY = (buttonHeight + buttonSpacing) * 4;
        for (int i = 0; i < languageChoices.length; i++) {
            String languageChoice = languageChoices[i];
            final int _i = i;

            final float currentOffsetY = offsetY;
            mainMenu_buttons.put("lang_" + languageChoice.toLowerCase(), new Button(gui.main,

                    () -> (!evenOptions && _i == languageChoices.length-1) ? 0 :
                            (buttonWidth / 4f + buttonSpacing / 4f) * ((_i % 2 == 0) ? -1 : 1),

                    () -> currentOffsetY,
                    () -> buttonWidth/2 - buttonSpacing/2, () -> buttonHeight, new Localization.Term(languageChoice),
                    () -> {
                        Localization.localize(languageChoice);
                    }));

            if(i % 2 == 1) offsetY += 32 + 16;
        }
    }

    public void update(GUI gui, double deltaTime) {
        for (Map.Entry<String, Button> entry : mainMenu_buttons.entrySet()) {
            if (entry != null) {
                entry.getValue().update(deltaTime);
            }
        }
    }

    public void render(GUI gui) {
        for (Map.Entry<String, Button> entry : mainMenu_buttons.entrySet()) {
            if (entry != null) {
                entry.getValue().render();
            }
        }
    }

    public void resize() {
        mainMenu_buttons.forEach((name, button) -> {
            button.resize();
        });
    }
}
