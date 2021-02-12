package com.pv.neliritta.gui.ingame;

import com.pv.neliritta.backend.BackEnd;
import com.pv.neliritta.gui.Color;
import com.pv.neliritta.gui.GameComponent;
import com.ydgames.mxe.GameContainer;
import processing.core.PConstants;

import java.util.Arrays;

public class Board implements GameComponent {
    private GameContainer gameContainer;

    /* Board data */
    private int[][] boardState;
    private int boardWidth;
    private int boardHeight;

    /* Gameplay */

    private int whoseTurn = 0;
    private boolean againstComputer = false;

    // Used to make the computer behave more like a human by taking time to think (in seconds)
    private float computerThinkingTime = 0;

    private BackEnd backEnd;

    /* Appearance */
    public Color background = new Color(88, 88, 88);
    public Color foreground = new Color(143, 143, 143);

    public Color player1Color = new Color(92, 124, 138);
    public Color player2Color = new Color(135, 98, 91);

    /* Layout */
    float width, height, padding, topLeftX, topLeftY, bottomRightX, bottomRightY, holeSpacing;

    public Board(GameContainer gameContainer, int boardWidth, int boardHeight) {
        this.gameContainer = gameContainer;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        boardState = new int[boardWidth][boardHeight];

        this.backEnd = new BackEnd(boardWidth, boardHeight);

        whoseTurn = 1;
    }

    public Board(GameContainer gameContainer, int[][] boardState, int whoseTurn, boolean againstComputer) {
        this.gameContainer = gameContainer;
        this.boardState = boardState;
        this.whoseTurn = whoseTurn;

        this.backEnd = new BackEnd(boardState, whoseTurn);
    }

     @Override
    public void resize() {

        /* Calculate needed coordinates */
        float minimumDimension = Math.min(gameContainer.getGame().pixelWidth, gameContainer.getGame().pixelHeight);

        padding = 128f;

        width = minimumDimension - padding * 2;
        height = minimumDimension * ((float) boardHeight / boardWidth) - padding * 2;

        topLeftX = (gameContainer.getGame().pixelWidth - width) / 2f;
        topLeftY = (gameContainer.getGame().pixelHeight - height) / 2f;

        bottomRightX = (gameContainer.getGame().pixelWidth - width) / 2f + width;
        bottomRightY = (gameContainer.getGame().pixelHeight - height) / 2f + height;

        holeSpacing = width / boardWidth;
    }

    @Override
    public void update(double deltaTime) {
        /* If mouse is on top of the board */
        if(gameContainer.getGame().mouseX > topLeftX &&
                gameContainer.getGame().mouseX < bottomRightX &&
                gameContainer.getGame().mouseY < bottomRightY &&
                gameContainer.getGame().mouseY > topLeftY - padding) {
            int column = (int)((gameContainer.getGame().mouseX - topLeftX) / holeSpacing);

            if(gameContainer.getGame().input.isButtonUp(PConstants.LEFT)) {
                if(againstComputer) {
                    if(whoseTurn == 1) {
                        backEnd.executePlayerTurn(1, column);
                        System.out.println("Player 1 inserted a circle at "+column);

                        computerThinkingTime = (float) (Math.random() * 3 + 3);
                    }
                } else {
                    System.out.println("Player "+whoseTurn+" inserted a circle at "+column);
                    backEnd.executePlayerTurn(whoseTurn, column);
                    whoseTurn = 1 + (whoseTurn) % 2;

                    System.out.println(Arrays.deepToString(boardState));
                }
            }
        }

        /* Simulate computer's thinking time */
        if(againstComputer && whoseTurn == 2) {
            if(computerThinkingTime < 0) {
                int column = backEnd.executeComputerTurn();
                System.out.println("Computer inserted a circle at "+column);

                computerThinkingTime = 0;
                whoseTurn = 1;
            } else {
                computerThinkingTime -= deltaTime;
            }
        }
    }

    @Override
    public void render() {

        /* Draw board with foreground color */

        gameContainer.getGame().fill(foreground.r, foreground.g, foreground.b, foreground.a);
        gameContainer.getGame().noStroke();

        gameContainer.getGame().rect(topLeftX, topLeftY, width, height);

        /* Draw holes with background color */

        for(int i = 0; i < boardWidth; i++) {
            for(int j = 0; j < boardHeight; j++) {
                if(boardState[i][j] == 1) {
                    gameContainer.getGame().fill(player1Color.r, player1Color.g, player1Color.b, player1Color.a);
                } else if(boardState[i][j] == 2) {
                    gameContainer.getGame().fill(player2Color.r, player2Color.g, player2Color.b, player2Color.a);
                } else {
                    gameContainer.getGame().fill(background.r, background.g, background.b, background.a);
                }
                gameContainer.getGame().noStroke();
                gameContainer.getGame().circle(
                        topLeftX + holeSpacing * i + holeSpacing / 2f,
                        topLeftY + holeSpacing * j + holeSpacing / 2f,
                        holeSpacing / 2f);
            }
        }
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public boolean isAgainstComputer() {
        return againstComputer;
    }
}
