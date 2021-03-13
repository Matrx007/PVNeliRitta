package com.pv.neliritta.gui.states;

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
    HashMap<String, Component> fields;

    @Override
    public void setup(GUI gui) {

        /* ### CREATE UI #### */

        fields = new HashMap<>();

        int numFields = 4;

        float fieldWidth = 384;
        float fieldHeight = 64;
        float fieldSpacing = 16;

        fields.put("rows_label", new Label(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -4,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "rows")
        ));

        fields.put("rows", new Input(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -3,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "rows"),
                Pattern.compile("[0-9]+"), Input.CHARACTER_PATTERN_NUMBERS,
                () -> {
                    System.out.println("Rows: "+((Input)fields.get("rows")).getInput());
                }
        ));

        fields.put("columns_label", new Label(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -2,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "columns")
        ));

        fields.put("columns", new Input(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * -1,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "columns"),
                Pattern.compile("[0-9]+"), Input.CHARACTER_PATTERN_NUMBERS,
                () -> {
                    System.out.println("Columns: "+((Input)fields.get("columns")).getInput());
                }
        ));

        fields.put("difficulty_label", new Label(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * 0,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "difficulty")
        ));

        fields.put("difficulty", new Options(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * 1,
                () -> fieldWidth, () -> fieldHeight,
                new Localization.Term[]{
                        Localization.reference("match options", "easy"),
                        Localization.reference("match options", "normal")
                },
                () -> {
                    System.out.println("Option changed");
                }
        ));

        fields.put("start", new Button(gui.main,
                () -> 0,
                () -> (fieldHeight + fieldSpacing) * 4,
                () -> fieldWidth, () -> fieldHeight,
                Localization.reference("match options", "start"),
                () -> {
                    String columnsString = ((Input)fields.get("columns")).getInput();
                    String rowsString = ((Input)fields.get("rows")).getInput();
                    BackEnd.Difficulty difficulty;

                    if(columnsString.length() == 0 && rowsString.length() == 0) {
                        return;
                    }

                    switch (((Options)fields.get("difficulty")).getSelected().key) {
                        case "easy":
                            difficulty = BackEnd.Difficulty.EASY;
                            break;
                        case "normal":
                            difficulty = BackEnd.Difficulty.NORMAL;
                            break;
                        default:
                            return;
                    }

                    gui.inGame.board = new Board(gui.main,
                            Integer.parseInt(columnsString),
                            Integer.parseInt(rowsString),
                            difficulty
                    );
                    gui.inGame.board.againstComputer = gui.inGame.againstComputer;

                    gui.state = GUI.State.IN_GAME;
                }
        ));
    }

    @Override
    public void update(GUI gui, double deltaTime) {
        for(Component component : fields.values()) {
            component.update(deltaTime);
        }
    }

    @Override
    public void render(GUI gui) {
        for(Component component : fields.values()) {
            component.render();
        }
    }

    @Override
    public void resize() {
        for(Component component : fields.values()) {
            component.resize();
        }
    }
}
