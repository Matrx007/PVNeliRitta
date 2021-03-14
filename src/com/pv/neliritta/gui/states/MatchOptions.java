package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;
import com.pv.neliritta.backend.BackEnd;
import com.pv.neliritta.gui.Component;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.gui.components.Input;
import com.pv.neliritta.gui.components.Label;
import com.pv.neliritta.gui.components.Options;
import com.pv.neliritta.gui.ingame.Board;
import com.pv.neliritta.localization.Localization;

import java.util.HashMap;
import java.util.regex.Pattern;

public class MatchOptions implements State {
    HashMap<String, Component> components;

    Label difficultyLabel;
    Options difficultyChooser;
    public boolean againstComputer = false;

    @Override
    public void setup(GUI gui) {

        /* ### CREATE UI #### */

        components = new HashMap<>();

        float fieldWidth = 384;
        float fieldHeight = 64;
        float fieldSpacing = 16;

        components.put("rows_label", new Label(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -4,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "rows")
        ));

        components.put("rows", new Input(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -3,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "rows"),
                Pattern.compile("[0-9]+"), Input.CHARACTER_PATTERN_NUMBERS,
                () -> {
                    start(gui);
                }
        ));

        components.put("columns_label", new Label(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -2,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "columns")
        ));

        components.put("columns", new Input(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -1,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "columns"),
                Pattern.compile("[0-9]+"), Input.CHARACTER_PATTERN_NUMBERS,
                () -> {
                    start(gui);
                }
        ));

        difficultyLabel = new Label(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * 0,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "difficulty")
        );

        difficultyChooser = new Options(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * 1,
                () -> fieldWidth, () -> fieldHeight,
                new Localization.Term[]{
                        Localization.reference("match options", "easy"),
                        Localization.reference("match options", "normal")
                },
                () -> {
                }
        );

        components.put("start", new Button(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * 3,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "start"),
                () -> {
                    start(gui);
                }
        ));

        components.put("back", new Button(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * 4,
                () -> fieldWidth*0.75f, () -> fieldHeight,
                Localization.reference("match options", "main_menu"),
                () -> {
                    gui.state = GUI.State.MAIN_MENU;
                }
        ));
    }

    private void start(GUI gui) {
        String columnsString = ((Input) components.get("columns")).getInput();
        String rowsString = ((Input) components.get("rows")).getInput();
        BackEnd.Difficulty difficulty;

        if(columnsString.length() == 0 && rowsString.length() == 0) {
            return;
        }

        Localization.Term selectedDifficulty = difficultyChooser.getSelected();
        if(selectedDifficulty == null) return;
        if(selectedDifficulty.key == null) return;

        switch (selectedDifficulty.key) {
            case "easy":
                difficulty = BackEnd.Difficulty.EASY;
                break;
            case "normal":
                difficulty = BackEnd.Difficulty.NORMAL;
                break;
            default:
                return;
        }

        System.out.println("againstComputer@MatchOptions = " + againstComputer);
        gui.inGame.board = new Board(gui.main,
                Integer.parseInt(columnsString),
                Integer.parseInt(rowsString),
                againstComputer,
                difficulty
        );

        gui.state = GUI.State.IN_GAME;
    }

    @Override
    public void update(GUI gui, double deltaTime) {
        for(Component component : components.values()) {
            component.update(deltaTime);
        }

        if(againstComputer) {
            difficultyChooser.update(deltaTime);
            difficultyLabel.update(deltaTime);
        }
    }

    @Override
    public void render(GUI gui) {
        for(Component component : components.values()) {
            component.render();
        }

        if(againstComputer) {
            difficultyChooser.render();
            difficultyLabel.render();
        }
    }

    @Override
    public void resize() {
        for(Component component : components.values()) {
            component.resize();
        }

        difficultyChooser.resize();
        difficultyLabel.resize();
    }
}
