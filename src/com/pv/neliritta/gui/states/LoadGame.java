package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;
import com.pv.neliritta.backend.BackEnd;
import com.pv.neliritta.backend.SaveManager;
import com.pv.neliritta.gui.Component;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.gui.components.Label;
import com.pv.neliritta.gui.components.ScrollList;
import com.pv.neliritta.gui.ingame.Board;
import com.pv.neliritta.localization.Localization;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadGame implements State {

    HashMap<String, Component> components;

    @Override
    public void setup(GUI gui) {

        /* ### CREATE UI #### */

        components = new HashMap<>();

        float fieldWidth = 480;
        float fieldHeight = 64;

        components.put("label", new Label(gui.main,
                () -> 0,
                () -> -6*fieldHeight,
                () -> fieldWidth,
                () -> 64,
                Localization.reference("main menu", "load_game")
        ));

        components.put("list", new ScrollList(gui.main,
                () -> 0,
                () -> -4*fieldHeight,
                () -> fieldWidth,
                () -> 64,
                8,
                new Localization.Term[0],
                () -> {
                    ScrollList me = ((ScrollList)components.get("list"));

                    Localization.Term term = me.getCurrentEntry();
                    if(term == null) {
                        return;
                    }

                    BackEnd newBackEnd = SaveManager.loadGame(me.getCurrentEntry().value);

                    if(newBackEnd == null) {
                        System.out.println("Save file couldn't be opened");
                        return;
                    }

                    gui.inGame.board = new Board(gui.main, newBackEnd);
                    gui.state = GUI.State.IN_GAME;
                }
        ));

        components.put("refresh", new Button(gui.main,
                () -> 0,
                () -> 4*fieldHeight+fieldHeight,
                () -> fieldWidth,
                () -> 64,
                Localization.reference("load game", "refresh"),
                this::refreshFileList
        ));

        components.put("back", new Button(gui.main,
                () -> 0,
                () -> 4*fieldHeight+fieldHeight*3,
                () -> fieldWidth/2,
                () -> 64,
                Localization.reference("load game", "main_menu"),
                () -> gui.state = GUI.State.MAIN_MENU
        ));
    }

    private void refreshFileList() {
        ArrayList<String> saveFiles = SaveManager.listSaves();
        Localization.Term[] saveFileEntries = new Localization.Term[saveFiles.size()];

        int saveFileCount = saveFiles.size();
        for(int i = 0; i < saveFileCount; i++) {
            saveFileEntries[i] = new Localization.Term(saveFiles.get(i));
        }

        ((ScrollList)components.get("list")).entries = saveFileEntries;
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
