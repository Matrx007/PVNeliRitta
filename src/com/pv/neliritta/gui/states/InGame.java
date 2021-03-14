package com.pv.neliritta.gui.states;
// Written by Rainis Randmaa

import com.pv.neliritta.GUI;
import com.pv.neliritta.gui.components.Button;
import com.pv.neliritta.gui.components.Label;
import com.pv.neliritta.gui.ingame.Board;
import com.pv.neliritta.localization.Localization;

public class InGame implements State {
    /* Game board, will be modified by 'BackEnd' */
    public Board board = null;

    Label inGame_whoseTurn;
    Button inGame_pause;

    boolean againstComputer;

    public void setup(GUI gui) {
        inGame_whoseTurn = new Label(gui.main,
                () -> 0,
                () -> + gui.main.guiSize / 2f + 64f, () -> 256, () -> 128, new Localization.Term(""));

        inGame_pause = new Button(gui.main,
                () -> 0,
                () -> - gui.main.guiSize / 2f - 64f, () -> 256, () -> 64, Localization.reference("in game gui", "pause"),
                () -> gui.state = GUI.State.IN_GAME_PAUSE_MENU);
    }

    public void update(GUI gui, double deltaTime) {
        board.update(deltaTime);

        // after loading save file, whoseTurn == 0
        int whoseTurn = board.getWhoseTurn();
        if(whoseTurn == 1) {
            inGame_whoseTurn.text = Localization.reference("turn", "player1");
        } else if(whoseTurn == 2) {
            inGame_whoseTurn.text = board.isAgainstComputer() ?
                    Localization.reference("turn", "computer") :
                    Localization.reference("turn", "player2");
        }

        int whoWon = board.getWhoWon();
        if(whoWon == 1) {
            inGame_whoseTurn.text = Localization.reference("turn", "player1_won");
        } else if(whoWon == 2) {
            inGame_whoseTurn.text = board.isAgainstComputer() ?
                    Localization.reference("turn", "computer_won") :
                    Localization.reference("turn", "player2_won");
        }

        inGame_whoseTurn.update(deltaTime);
        inGame_pause.update(deltaTime);
    }

    public void render(GUI gui) {
        board.render();
        inGame_whoseTurn.render();
        inGame_pause.render();
    }

    public void resize() {
        if(board != null) board.resize();

        inGame_whoseTurn.resize();
        inGame_pause.resize();
    }
}
