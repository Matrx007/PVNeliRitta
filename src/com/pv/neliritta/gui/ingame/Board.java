package com.pv.neliritta.gui.ingame;

import com.pv.neliritta.Utilities;
import com.pv.neliritta.GraphicsManager;
import com.pv.neliritta.Main;
import com.pv.neliritta.backend.BackEnd;
import com.pv.neliritta.gui.Color;
import com.pv.neliritta.gui.GameComponent;
import processing.core.PConstants;
import processing.core.PImage;

public class Board implements GameComponent {
    private Main main;

    /* Board data */
    private int[][] boardState;
    private int boardWidth;
    private int boardHeight;

    /* Gameplay */

    private int whoseTurn = 0;
    private int whoWon = 0;
    public boolean againstComputer = false;

    // Used to make the computer behave more like a human by taking time to think (in seconds)
    private float computerThinkingTime = 0;

    private BackEnd backEnd;

    /* Appearance */
    public Color background = new Color(88, 88, 88);
    public Color foreground = new Color(143, 143, 143);

    private PImage slotImage;

    public Color player1Color = new Color(92, 124, 138);
    public Color player2Color = new Color(135, 98, 91);

    private int mouseOverColumn = -1;

    /* Layout */
    float width, height, padding, topLeftX, topLeftY, bottomRightX, bottomRightY, holeSpacing;
    float startX, startY, totalWidth, totalHeight;

    public Board(Main main, int boardWidth, int boardHeight) {
        this.main = main;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        boardState = new int[boardWidth][boardHeight];

        this.backEnd = new BackEnd(boardWidth, boardHeight);

        slotImage = GraphicsManager.loadedGraphics.get("slot");

        whoseTurn = 1;
    }

    public Board(Main main, int[][] boardState, int whoseTurn, boolean againstComputer) {
        this.main = main;
        this.boardState = boardState;
        this.whoseTurn = whoseTurn;

        this.backEnd = new BackEnd(boardState, whoseTurn);
    }

     @Override
    public void resize() {

        /* Calculate needed coordinates */
        /*float minimumDimension = Math.min(main.getGame().pixelWidth, main.getGame().pixelHeight);

        padding = 128f;

        width = minimumDimension - padding * 2;
        height = minimumDimension * ((float) boardHeight / boardWidth) - padding * 2;

        topLeftX = (main.getGame().pixelWidth - width) / 2f;
        topLeftY = (main.getGame().pixelHeight - height) / 2f;

        bottomRightX = (main.getGame().pixelWidth - width) / 2f + width;
        bottomRightY = (main.getGame().pixelHeight - height) / 2f + height;

        holeSpacing = width / boardWidth;*/

         float padding = 128f;

         holeSpacing = (main.guiSize - padding * 2f) / (float)Math.max(boardWidth, boardHeight);

         totalWidth = (boardWidth * holeSpacing);
         totalHeight = (boardHeight * holeSpacing);

         startX = -totalWidth  / 2f;
         startY = -totalHeight / 2f;

         System.out.println("width: "+boardWidth * holeSpacing);
         System.out.println("totalWidth = "+totalWidth);
         System.out.println("startX = "+startX);
    }

    @Override
    public void update(double deltaTime) {
        if(whoWon != 0) return;

        /* If mouse is on top of the board */
        if(mouseOverColumn > -1) {
            if(main.getGame().input.isButtonUp(PConstants.LEFT)) {
                if(againstComputer) {
                    if(whoseTurn == 1) {
                        boolean result = backEnd.executePlayerTurn(1, mouseOverColumn);

                        // If the turn was successful, computer will take a turn now
                        if(result) {
                            computerThinkingTime = (float) (Math.random() * 2 + 2);

                            whoseTurn = 2;

                            whoWon = backEnd.whoWon();
                        }
                    }
                } else {
                    boolean result = backEnd.executePlayerTurn(whoseTurn, mouseOverColumn);

                    // If the turn was successful, computer will take a turn now
                    if(result) {
                        whoseTurn = 1 + (whoseTurn) % 2;

                        whoWon = backEnd.whoWon();
                    }
                }
            }
        }

        /* Simulate computer's thinking time */
        if(againstComputer && whoseTurn == 2) {
            if(computerThinkingTime < 0) {
                int column = backEnd.executeComputerTurn();

                // If the turn was successful, computer will take a turn now
                if(column > -1) {

                    whoWon = backEnd.whoWon();

                    computerThinkingTime = 0;
                    whoseTurn = 1;
                }
            } else {
                computerThinkingTime -= deltaTime;
            }
        }
    }

    @Override
    public void render() {
        /* Draw holes with background color */
        mouseOverColumn = -1;

        for(int i = 0; i < boardWidth; i++) {
            for(int j = 0; j < boardHeight; j++) {
                main.getGame().pushMatrix();

                if(!(againstComputer && whoseTurn == 2) &&
                        Utilities.isPointInsidePerspectiveRectangle(main,
                                startX + holeSpacing * i,
                                startY,
                                startX + holeSpacing * i + holeSpacing,
                                startY + boardHeight * holeSpacing)) {
                    mouseOverColumn = i;
                    main.getGame().tint(255, 96f);
                } else {
                    main.getGame().tint(255, 64f);
                }
                main.getGame().image(slotImage, startX + holeSpacing * i, startX + holeSpacing * j, holeSpacing, holeSpacing);
                main.getGame().popMatrix();

                if(backEnd.currentBoardState()[i][(boardHeight-1)-j] == 1) {
                    main.getGame().fill(player1Color.r, player1Color.g, player1Color.b, player1Color.a);

                    main.getGame().noStroke();
                    main.getGame().circle(
                            startX + holeSpacing * i + holeSpacing / 2f,
                            startY + holeSpacing * j + holeSpacing / 2f,
                            holeSpacing / 4f * 3f);
                } else if(backEnd.currentBoardState()[i][(boardHeight-1)-j] == 2) {
                    main.getGame().fill(player2Color.r, player2Color.g, player2Color.b, player2Color.a);

                    main.getGame().noStroke();
                    main.getGame().circle(
                            startX + holeSpacing * i + holeSpacing / 2f,
                            startY + holeSpacing * j + holeSpacing / 2f,
                            holeSpacing / 4f * 3f);
                }




                main.getGame().fill(255*0.5f, 255*0.5f, 255*0.5f, 255);

                /*main.getGame().beginShape(PConstants.QUADS);

                main.getGame().vertex(startX, startY, 0f);
                main.getGame().vertex(startX, startY, 80f);
                main.getGame().vertex(startX, startY + totalHeight, 80f);
                main.getGame().vertex(startX, startY + totalHeight, 0f);

                main.getGame().vertex(startX + totalWidth, startY, 0f);
                main.getGame().vertex(startX + totalWidth, startY, 80f);
                main.getGame().vertex(startX + totalWidth, startY + totalHeight, 80f);
                main.getGame().vertex(startX + totalWidth, startY + totalHeight, 0f);

                main.getGame().endShape();*/





                /*main.getGame().translate(0, 0, 80f);

                main.getGame().fill(255*0.75f, 255*0.75f, 255*0.75f, 255);
                main.getGame().rect(
                        startX + holeSpacing * i + holeSpacing / 8f,
                        startY + holeSpacing * j + holeSpacing / 2f,
                        holeSpacing / 4f, holeSpacing);
                main.getGame().rect(
                        startX + holeSpacing * i + holeSpacing - holeSpacing / 8f,
                        startY + holeSpacing * j + holeSpacing / 2f,
                        holeSpacing / 4f, holeSpacing);
                main.getGame().rect(
                        startX + holeSpacing * i + holeSpacing / 2f,
                        startY + holeSpacing * j + holeSpacing / 8f,
                        holeSpacing, holeSpacing / 4f);
                main.getGame().rect(
                        startX + holeSpacing * i + holeSpacing / 2f,
                        startY + holeSpacing * j + holeSpacing - holeSpacing / 8f,
                        holeSpacing, holeSpacing / 4f);

                main.getGame().translate(0, 0, -80f);*/
            }
        }

        if(mouseOverColumn != -1 && !(againstComputer && whoseTurn == 2))  {
            main.getGame().fill(255, 255, 255, 16);
            main.getGame().noStroke();

            main.getGame().rect(topLeftX + mouseOverColumn * holeSpacing, topLeftY, holeSpacing, height);
        }
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public boolean isAgainstComputer() {
        return againstComputer;
    }

    public int getWhoWon() {
        return whoWon;
    }
}
