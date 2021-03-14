package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;
import com.pv.neliritta.backend.SaveManager;
import com.pv.neliritta.gui.Component;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.gui.components.Input;
import com.pv.neliritta.gui.components.Label;
import com.pv.neliritta.localization.Localization;

import java.util.HashMap;

public class SaveGame implements State {

    HashMap<String, Component> components;

    @Override
    public void setup(GUI gui) {

        /* ### CREATE UI #### */

        components = new HashMap<>();

        float fieldWidth = 320;
        float fieldHeight = 64;

        components.put("label", new Label(gui.main,
                () -> 0,
                () -> -2*fieldHeight,
                () -> fieldWidth,
                () -> fieldHeight,
                Localization.reference("save game", "save")
        ));

        components.put("name", new Input(gui.main,
                () -> 0,
                () -> 0*fieldHeight,
                () -> fieldWidth,
                () -> fieldHeight,
                Localization.reference("save game", "save_as"),
                Input.RESULT_PATTERN_FILENAME,
                Input.CHARACTER_PATTERN_FILENAME,
                () -> {
                    try {
                        SaveManager.saveGame(gui.inGame.board.backEnd, ((Input)components.get("name")).getInput());
                    } catch (Exception e) {
                        return;
                    }

                    gui.state = GUI.State.IN_GAME;
                }
        ));

        components.put("save", new Button(gui.main,
                () -> 0,
                () -> 2*fieldHeight,
                () -> fieldWidth,
                () -> fieldHeight,
                Localization.reference("save game", "save"),
                () -> {
                    try {
                        SaveManager.saveGame(gui.inGame.board.backEnd, ((Input)components.get("name")).getInput());
                    } catch (Exception e) {
                        return;
                    }

                    gui.state = GUI.State.IN_GAME;
                }
        ));

        components.put("back", new Button(gui.main,
                () -> 0,
                () -> 4*fieldHeight,
                () -> fieldWidth*0.75f,
                () -> fieldHeight,
                Localization.reference("save game", "back"),
                () -> {
                    gui.state = GUI.State.IN_GAME;
                }
        ));

    }

    @Override
    public void resize() {
        for(Component component : components.values()) {
            component.resize();
        }
    }

    @Override
    public void update(GUI gui, double deltaTime) {

        for(Component component : components.values()) {
            component.update(deltaTime);
        }
    }

    @Override
    public void render(GUI gui) {
        for(Component component : components.values()) {
            component.render();
        }
    }
}
