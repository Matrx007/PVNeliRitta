package com.pv.neliritta.gui.ingame;

import com.pv.neliritta.Utilities;
import com.pv.neliritta.GraphicsManager;
import com.pv.neliritta.Main;
import com.pv.neliritta.backend.BackEnd;
import com.pv.neliritta.gui.Action;
import com.pv.neliritta.gui.Color;
import com.pv.neliritta.gui.Component;
import processing.core.PConstants;
import processing.core.PImage;

import java.util.Stack;

public class Board implements Component {
    private Main main;

    private static float BALL_SIZE = 1.03f;

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
    private BackEnd.Difficulty difficulty;

    /* Appearance */
    public Color background = new Color(88, 88, 88);
    public Color foreground = new Color(143, 143, 143);

    private PImage slotImage;

    public Color player1Color = new Color(92, 124, 138);
    public Color player2Color = new Color(135, 98, 91);

    private int mouseOverColumn = -1;

    private Stack<BallAnimation> ballAnimations = new Stack<>();

    /* Layout */
    float width, height, padding, topLeftX, topLeftY, bottomRightX, bottomRightY, holeSpacing;
    float boardStartX, boardStartY, totalWidth, totalHeight;

    public Board(Main main, int boardWidth, int boardHeight, BackEnd.Difficulty difficulty) {
        this.main = main;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.difficulty = difficulty;

        slotImage = GraphicsManager.loadedGraphics.get("slot");

        init();

        resize();

        System.out.println("Using difficulty: "+difficulty.toString());
    }

    public Board(Main main, int[][] boardState, int whoseTurn, boolean againstComputer, BackEnd.Difficulty difficulty) {
        this.main = main;
        this.boardState = boardState;
        this.whoseTurn = whoseTurn;
        this.againstComputer = againstComputer;
        this.difficulty = difficulty;

        this.backEnd = new BackEnd(boardState, whoseTurn, difficulty);

        resize();
    }

    public void init() {
        boardState = new int[boardWidth][boardHeight];

        this.backEnd = new BackEnd(boardWidth, boardHeight, difficulty);

        whoseTurn = 1;
        whoWon = 0;
        computerThinkingTime = 0;
    }

     @Override
    public void resize() {

        /* Calculate needed coordinates */
         float padding = 0f;

         holeSpacing = (main.guiSize - padding * 2f) / (float)Math.max(boardWidth, boardHeight);

         totalWidth = (boardWidth * holeSpacing);
         totalHeight = (boardHeight * holeSpacing);

         boardStartX = -totalWidth  / 2f;
         boardStartY = -totalHeight / 2f;
    }

    @Override
    public void update(double deltaTime) {
        if(whoWon != 0) return;

        /* If no animations are playing at the moment, check for winner */
        if(ballAnimations.empty()) {
            whoWon = backEnd.whoWon();
        }

        /* If mouse is on top of the board */
        if(mouseOverColumn > -1) {
            if(main.getGame().input.isButtonUp(PConstants.LEFT) && ballAnimations.empty()) {
                if(againstComputer) {
                    if(whoseTurn == 1) {
                        boolean result = backEnd.executePlayerTurn(1, mouseOverColumn);

                        // Find the first free slot in that column, used to display ball dropping animation
                        int firstFreeSlot = boardHeight;
                        for (int i = 0; i < backEnd.currentBoardState()[mouseOverColumn].length; i++) {
                            if (backEnd.currentBoardState()[mouseOverColumn][i] == 0) {
                                firstFreeSlot = i;
                                break;
                            }
                        }

                        // If the turn was successful, computer will take a turn now
                        if(result) {
                            computerThinkingTime = (float) (Math.random() * 2 + 2);

                            whoseTurn = 2;

                            // Spawn the ball dropping animation
                            ballAnimations.push(new BallAnimation(mouseOverColumn, (boardHeight) - firstFreeSlot, player1Color,
                                    () -> {}));
                        }
                    }
                } else {
                    boolean result = backEnd.executePlayerTurn(whoseTurn, mouseOverColumn);

                    // Find the first free slot in that column, used to display ball dropping animation
                    int firstFreeSlot = boardHeight;
                    for (int i = 0; i < backEnd.currentBoardState()[mouseOverColumn].length; i++) {
                        if (backEnd.currentBoardState()[mouseOverColumn][i] == 0) {
                            firstFreeSlot = i;
                            break;
                        }
                    }

                    // If the turn was successful, computer will take a turn now
                    if(result) {
                        whoseTurn = 1 + (whoseTurn) % 2;

                        // Spawn the ball animation
                        ballAnimations.push(new BallAnimation(mouseOverColumn, (boardHeight) - firstFreeSlot, whoseTurn == 1 ? player2Color : player1Color,
                                () -> {}));
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

                    // Find the first free slot in that column, used to display ball dropping animation
                    int firstFreeSlot = boardHeight;
                    for (int i = 0; i < backEnd.currentBoardState()[column].length; i++) {
                        if (backEnd.currentBoardState()[column][i] == 0) {
                            firstFreeSlot = i;
                            break;
                        }
                    }

                    computerThinkingTime = 0;
                    whoseTurn = 1;

                    // Spawn the ball animation
                    ballAnimations.push(new BallAnimation(column, (boardHeight) - firstFreeSlot, player2Color,
                            () -> {}));
                }
            } else {
                computerThinkingTime -= deltaTime;
            }
        }

        /* Removing finished animation */
        while (!ballAnimations.isEmpty() && !ballAnimations.peek().alive) {
            ballAnimations.pop();
        }

        /* Animating ball animations */
        for(BallAnimation animation : ballAnimations) {
            animation.update(deltaTime);
        }
    }

    @Override
    public void render() {
        /* Draw holes with background color */
        mouseOverColumn = -1;

        for(int i = 0; i < boardWidth; i++) {
slot:      for(int j = 0; j < boardHeight; j++) {


                if(!(againstComputer && whoseTurn == 2) &&
                        Utilities.isPointInsidePerspectiveRectangle(main,
                                boardStartX + holeSpacing * i,
                                boardStartY,
                                boardStartX + holeSpacing * i + holeSpacing,
                                boardStartY + boardHeight * holeSpacing)) {
                    mouseOverColumn = i;
                    main.getGame().tint(255, 256f);
                } else {
                    main.getGame().tint(255, 192f);
                }
                main.getGame().imageMode(PConstants.CORNER);
                main.getGame().image(slotImage, boardStartX + holeSpacing * i, boardStartY + holeSpacing * j, holeSpacing, holeSpacing);

                if(j == 0)
                    main.getGame().image(GraphicsManager.loadedGraphics.get("slot-top"),
                            boardStartX + holeSpacing * i, boardStartY + holeSpacing * -1,
                            holeSpacing, holeSpacing);
                else if(j == boardHeight-1)
                    main.getGame().image(GraphicsManager.loadedGraphics.get("slot-bottom"),
                            boardStartX + holeSpacing * i, boardStartY + holeSpacing * boardHeight,
                            holeSpacing, holeSpacing);



                // Skip drawing the circle if this slot is in middle of an animation
                for(BallAnimation animation : ballAnimations) {
                    if(animation.slotX == i && animation.slotY == j) continue slot;
                }

                main.getGame().imageMode(PConstants.CENTER);

                if(backEnd.currentBoardState()[i][(boardHeight-1)-j] == 1) {
                    main.getGame().fill(player1Color.r, player1Color.g, player1Color.b, player1Color.a);

                    main.getGame().noStroke();
                    main.getGame().image(
                            GraphicsManager.loadedGraphics.get("ball_red"),
                            boardStartX + holeSpacing * i + holeSpacing / 2f,
                            boardStartY + holeSpacing * j + holeSpacing / 2f,
                            holeSpacing * BALL_SIZE,
                            holeSpacing * BALL_SIZE
                    );
//                    main.getGame().circle(
//                            boardStartX + holeSpacing * i + holeSpacing / 2f,
//                            boardStartY + holeSpacing * j + holeSpacing / 2f,
//                            holeSpacing / 4f * 3f);
                } else if(backEnd.currentBoardState()[i][(boardHeight-1)-j] == 2) {
                    main.getGame().fill(player2Color.r, player2Color.g, player2Color.b, player2Color.a);

                    main.getGame().noStroke();
                    main.getGame().image(
                            GraphicsManager.loadedGraphics.get("ball_blue"),
                            boardStartX + holeSpacing * i + holeSpacing / 2f,
                            boardStartY + holeSpacing * j + holeSpacing / 2f,
                            holeSpacing * BALL_SIZE,
                            holeSpacing * BALL_SIZE
                    );
//                    main.getGame().circle(
//                            boardStartX + holeSpacing * i + holeSpacing / 2f,
//                            boardStartY + holeSpacing * j + holeSpacing / 2f,
//                            holeSpacing / 4f * 3f);
                }




                main.getGame().fill(255*0.5f, 255*0.5f, 255*0.5f, 255);
            }
        }

        for(BallAnimation animation : ballAnimations) {
            animation.render();
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














    class BallAnimation {
        private static final float ANIMATION_FALL_TIME = 0.5f;
        private static final float ANIMATION_FADE_IN_TIME = 0.25f;

        private final float startX, startY;
        private final float endX, endY;
        private float currentX, currentY;

        public int slotX, slotY;

        public boolean alive = true;
        private float timer = 0;
        private final Action onFinish;

        private final Color ballColor;
        private float alpha = 0f;

        public BallAnimation(int slotX, int slotY, Color ballColor, Action onFinish) {
            this.slotX = slotX;
            this.slotY = slotY;

            this.startX = boardStartX + holeSpacing * slotX + holeSpacing / 2f;
            this.startY = boardStartY + holeSpacing * -1 + holeSpacing / 2f;
            this.endX = boardStartX + holeSpacing * slotX + holeSpacing / 2f;
            this.endY = boardStartY + holeSpacing * slotY + holeSpacing / 2f;

            this.ballColor = ballColor;
            this.onFinish = onFinish;
        }

        public void update(double deltaTime) {
            timer += deltaTime;
            alpha = (float)Math.min(1f, alpha + deltaTime / ANIMATION_FADE_IN_TIME);

            // Easing result, ranges from 0-1
            float location = 0;
            // Input variable, ranges from 0-1
            float time = timer / ANIMATION_FALL_TIME;

            // Finish the animation when time ran out
            if(timer > ANIMATION_FALL_TIME) {
                onFinish.run();
                alive = false;
                return;
            }


            // Bouncy ease animation
            // source: easings.net
            float n1 = 7.5625f;
            float d1 = 2.75f;
            if (time < 1 / d1) {
                location = n1 * time * time;
            } else if (time < 2 / d1) {
                location = n1 * (time -= 1.5 / d1) * time + 0.75f;
            } else if (time < 2.5 / d1) {
                location = n1 * (time -= 2.25 / d1) * time + 0.9375f;
            } else {
                location = n1 * (time -= 2.625 / d1) * time + 0.984375f;
            }

            // Map calculated coordinate to move along given path
            currentX = startX + (endX - startX) * location;
            currentY = startY + (endY - startY) * location;
        }

        public void render() {
            main.getGame().fill(ballColor.r, ballColor.g, ballColor.b, ballColor.a * alpha);

            main.getGame().noStroke();

            main.getGame().imageMode(PConstants.CENTER);

            main.getGame().image(
                    (ballColor == player1Color) ?
                        GraphicsManager.loadedGraphics.get("ball_red") :
                        GraphicsManager.loadedGraphics.get("ball_blue"),
                    currentX,
                    currentY,
                    holeSpacing * BALL_SIZE,
                    holeSpacing * BALL_SIZE
            );
//            main.getGame().circle(
//                    currentX,
//                    currentY,
//                    holeSpacing / 4f * 3f);
        }
    }
}
