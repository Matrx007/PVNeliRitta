package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;
import com.pv.neliritta.backend.SaveManager;
import com.pv.neliritta.gui.Component;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.gui.components.ScrollList;
import com.pv.neliritta.localization.Localization;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadGame implements State {

    HashMap<String, Component> components;

    @Override
    public void setup(GUI gui) {

        /* ### CREATE UI #### */

        components = new HashMap<>();

        components.put("list", new ScrollList(gui.main,
                () -> 0,
                () -> -4*64,
                () -> 480,
                () -> 64,
                8,
                new Localization.Term[0],
                () -> {
//                    ScrollList me = ((ScrollList)components.get("list"));
//                    BackEnd newBackEnd = SaveManager.loadGame(me.getCurrentEntry());
//                    gui.inGame.board = new Board()
                }
        ));

        components.put("refresh", new Button(gui.main,
                () -> 0,
                () -> 4*64+64,
                () -> 480,
                () -> 64,
                Localization.reference("load game", "refresh"),
                () -> {
                    refreshFileList();
                }
        ));

        components.put("back", new Button(gui.main,
                () -> 0,
                () -> 4*64+64*3,
                () -> 240,
                () -> 64,
                Localization.reference("load game", "main_menu"),
                () -> {
                    gui.state = GUI.State.MAIN_MENU;
                }
        ));
    }

    private void refreshFileList() {
        ArrayList<String> saveFiles = SaveManager.listSaves();
        System.out.println("saveFiles = " + saveFiles);
        Localization.Term[] saveFileEntries = new Localization.Term[saveFiles.size()];

        int saveFileCount = saveFiles.size();
        for(int i = 0; i < saveFileCount; i++) {
            saveFileEntries[i] = new Localization.Term(saveFiles.get(i));
        }

        ((ScrollList)components.get("list")).entries = saveFileEntries;
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

    @Override
    public void resize() {
        for(Component component : components.values()) {
            component.resize();
        }
    }
}
