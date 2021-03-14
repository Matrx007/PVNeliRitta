package com.pv.neliritta.backend;
// Written by Gregor Suurvarik


import java.util.Random;

/*
* Data about circles will be stored as an integer, 0 meaning 'empty', '1' meaning player 1 placed it
* and '2' meaning player 2 or the computer placed it.
*
* Players will be differentiated by their ID-s. Player 1 will have an ID of 1 and player 2 (or the computer)
* will have an ID of 2.
* */
public class BackEnd {

    /*
    * When a new game starts, this constructor will be called
    *
    * 'boardWidth' and 'boardHeight' should be used to initialize the game
    * board (aka 'private int[][] board').
    * */
    public BackEnd(int boardWidth, int boardHeight, Difficulty difficulty) {
        // Initializes board
        this.board = new int[boardWidth][boardHeight];
        this.difficulty = difficulty;
    }

    /*
    * When a saved game is loaded, this constructor will be called.
    *
    * 'board' will contain the current state of the game board.
    *
    * 'whoseTurn' is either a '0' when no-one is winning (game is still going on) or
    * an ID of the player whose turn is at the moment (meaning (s)he hasn't done anything yet but is about to'.
    * */
    public BackEnd(int[][] board, int whoseTurn, Difficulty difficulty, Boolean againstComputer) {
        /* TODO */
        this.board = board;
        this.whoseTurn = whoseTurn;
        this.difficulty = difficulty;
        this.againstComputer = againstComputer;
    }

    /*
    * Should be modified after every turn by either the function 'executePlayersTurn(int playerID)' or
    * 'executeComputersTurn()'.
    * */
    private final int[][] board;
    public int whoseTurn;
    public boolean againstComputer;
    // Difficulty just in case, default normal
    private final Difficulty difficulty;
    // Player 1 last move
    private int p1Column = 0;

    /*
    * Returns the current state of the game board (aka 'private int[][] board').
    * */
    public int[][] currentBoardState() {
        return board;
    }

    /*
     * Returns the whoseTurn.
     * */
    public int getWhoseTurn() {
        return whoseTurn;
    }

    /*
     * Returns the Difficulty.
     * */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /*
    * This will usually get called after a call to 'executePlayerTurn(..)' or 'executeComputerTurn()'.
    *
    * Returns either a '0' when no-one is winning (game is still going on) or an ID of the player who
    * got 4 circles in a row, column or in a diagonal.
     * */
    public int whoWon() {
        boolean[][] boardPlayer = new boolean[board.length][board[1].length];
        boolean[][] boardPlayer2 = new boolean[board.length][board[1].length];
        try {
            // Converts board to player based boolean array for easier checking
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 1) {
                        // Setting values to correct ones
                        boardPlayer[i][j] = true;
                        boardPlayer2[i][j] = false;
                    } else if (board[i][j] == 2) {
                        boardPlayer[i][j] = false;
                        boardPlayer2[i][j] = true;
                    }
                }
            }
            // checking from bottom to top and left to right to get horizontal rows of 4
            for (int j = 0; j < board[1].length; j++) {
                for (int i = 0; i < board.length - 3; i++) {
                    // Player board check
                    if (boardPlayer[i][j] && boardPlayer[i + 1][j]
                            && boardPlayer[i + 2][j] && boardPlayer[i + 3][j]) {
                        return 1;
                        // Player 2 board check
                    } else if (boardPlayer2[i][j] && boardPlayer2[i + 1][j]
                            && boardPlayer2[i + 2][j] && boardPlayer2[i + 3][j]) {
                        return 2;
                    }
                }
            }
            // Checking from left to right and bottom to top if there are vertical rows of 4
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length - 3; j++) {
                    // Player board
                    if (boardPlayer[i][j] && boardPlayer[i][j + 1]
                            && boardPlayer[i][j + 2] && boardPlayer[i][j + 3]) {
                        return 1;
                        // Player 2 board
                    } else if (boardPlayer2[i][j] && boardPlayer2[i][j + 1]
                            && boardPlayer2[i][j + 2] && boardPlayer2[i][j + 3]) {
                        return 2;
                    }
                }
            }
            // Checks board for diagonal rows of 4 (down left to up right)
            for (int i = 0; i < board.length - 3; i++) {
                for (int j = 0; j < board[i].length - 3; j++) {
                    if (boardPlayer[i][j] && boardPlayer[i + 1][j + 1]
                            && boardPlayer[i + 2][j + 2] && boardPlayer[i + 3][j + 3]) {
                        return 1;
                    } else if (boardPlayer2[i][j] && boardPlayer2[i + 1][j + 1]
                            && boardPlayer2[i + 2][j + 2] && boardPlayer2[i + 3][j + 3]) {
                        return 2;
                    }
                }
            }
            // Checks board for diagonal rows of 4 (up left to down right)
            for (int i = 3; i < board.length; i++) {
                for (int j = 0; j < board[i].length - 3; j++) {
                    // Player board
                    if (boardPlayer[i][j] && boardPlayer[i - 1][j + 1]
                            && boardPlayer[i - 2][j + 2] && boardPlayer[i - 3][j + 3]) {
                        return 1;
                        // Player 2 board
                    } else if (boardPlayer2[i][j] && boardPlayer2[i - 1][j + 1]
                            && boardPlayer2[i - 2][j + 2] && boardPlayer2[i - 3][j + 3]) {
                        return 2;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        // To get to here there can not be any rows of 4 at the moment.
        int k = 0;
        for (int i = 0; i < board.length - 1; i++) {
            k += possibleHighest(i);
        }
        if (k == board.length * -1) return -1;
        return 0;
    }

    /*
    * This function will be executed when player 1 or player 2 places a circle.
    * This function also could be called by the executeComputerTurn() in case Gregor finds it easier that way.
    * 
    * playerID - either 1 or 2, representing either player 1 or player 2
    * x - the column where to place the circle (left to right)
    * */
    public boolean executePlayerTurn(int playerID, int x) {
        // Gets boards highest point where dod can be placed
        int y = possibleHighest(x);
        // If doesn't exist returns;
        if (y == -1) {
            return false;
        }
        // Set on board player made change
        board[x][y] = playerID;
        if (playerID == 1) {
            p1Column = x;
        }
        try {
            SaveManager.saveGame(this, "Test");
            System.out.println(SaveManager.listSaves());
            SaveManager.loadGame("Test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /*
    * Returns the current state of the game board, should be modified after every turn by either the function
    * 'executePlayerTurn(..)' or 'executeComputerTurn()'
    * Computer's ID is always 2, so it is playing as it was player 2
    * 
    * Returns the column where a circle was placed by the computer (left to right), 
    *  -1 should only be used when an error occurred.
    * */
    public int executeComputerTurn() {
        /* TODO Debug */
        switch (difficulty) {
            case NORMAL:
                int pLastX, pLastY;
                pLastX = p1Column;
                pLastY = possibleHighest(pLastX) == board[0].length - 1 ? possibleHighest(pLastX) : board.length - 1;
                int pNextX = -1, pNextY = -1;
                try {
                    // Checks if player has 3 in row, next 71 lines of code
                    for (int i = 0; i < board[0].length; i++) {
                        for (int j = 0; j < board.length - 2; j++) {
                            if (board[i][j] == 1 && board[i + 1][j] == 1 && board[i + 2][j] == 1) {
                                if (!(j + 3 == board.length) && i - 1 >= 0) {
                                    if ((i == 0 || board[j + 3][i - 1] != 0) && onBoard(j+3, i)) {
                                        pNextX = j + 3;
                                        pNextY = i;
                                    }
                                }
                                if (j - 1 >= 0 && i - 1 >= 0) {
                                    if ((i == 0 || board[j - 1][i - 1] != 0) && onBoard(j-1, i)) {
                                        pNextX = j - 1;
                                        pNextY = i;
                                    }
                                }
                            }
                        }
                    }
                    for (int i = 0; i < board.length; i++) {
                        for (int j = 0; j < board[0].length - 2; j++) {
                            if (board[i][j] == 1 && board[i][j + 1] == 1 && board[i][j + 2] == 1) {
                                if (!(j + 3 >= board[i].length)) {
                                    if (board[i][j + 2] != 0 && onBoard(i, j+3)) {
                                        pNextX = i;
                                        pNextY = j + 3;
                                    }
                                }
                                if (j - 1 >= 0) {
                                    if (board[i][j - 2] != 0 && onBoard(i, j-1)) {
                                        pNextX = i;
                                        pNextY = j - 1;
                                    }
                                }
                            }
                        }
                    }
                    for (int i = 0; i < board.length - 2; i++) {
                        for (int j = 0; j < board[1].length - 2; j++) {
                            if (board[i][j] == 1 && board[i + 1][j + 1] == 1 && board[i + 2][j + 2] == 1) {
                                if (i + 3 < board.length && j + 3 < board[i].length) {
                                    if (board[i + 3][j + 2] != 0  && onBoard(i+3, j+3)) {
                                        pNextX = i + 3;
                                        pNextY = j + 3;
                                    }
                                }
                                if (i - 1 >= 0 && j - 1 >= 0) {
                                    if (board[i - 1][j - 2] != 0  && onBoard(i-1, j-1)) {
                                        pNextX = i - 1;
                                        pNextY = j - 1;
                                    }
                                }
                            }
                        }
                    }
                    for (int i = 2; i < board.length - 2; i++) {
                        for (int j = 0; j < board[1].length - 2; j++) {
                            if (board[i][j] == 1 && board[i - 1][j + 1] == 1 && board[i - 2][j + 2] == 1) {
                                if (i - 1 >= 0 && j + 3 < board[i].length) {
                                    if (board[i - 3][j + 2] != 0  && onBoard(i-3, j+3)) {
                                        pNextX = i - 3;
                                        pNextY = j + 3;
                                    }
                                }
                                if (i + 1 < board.length && j - 1 >= 0) {
                                    if (board[i + 1][j - 2] != 0  && onBoard(i+1, j-1)) {
                                        pNextX = i + 1;
                                        pNextY = j - 1;
                                    }
                                }
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
                if (pNextX != -1 && pNextY != -1) { // If player had that then computer puts its thing there
                    boolean result = executePlayerTurn(2, pNextX);
                    // Returns result of the placement
                    return result ? pNextX : -1;
                } else {
                    int cNextX = -1, cNextY = -1;
                    try {
                        // Checks if Computer has 3 in row and could win
                        for (int i = 0; i < board[0].length; i++) {
                            for (int j = 0; j < board.length - 2; j++) {
                                if (board[i][j] == 1 && board[i + 1][j] == 1 && board[i + 2][j] == 1) {
                                    if (!(j + 3 == board.length) && i - 1 >= 0) {
                                        if ((i == 0 || board[j + 3][i - 1] != 0) && onBoard(j+3, i)) {
                                            cNextX = j + 3;
                                            cNextY = i;
                                        }
                                    }
                                    if (j - 1 >= 0 && i - 1 >= 0) {
                                        if ((i == 0 || board[j - 1][i - 1] != 0) && onBoard(j-1, i)) {
                                            cNextX = j - 1;
                                            cNextY = i;
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < board.length; i++) {
                            for (int j = 0; j < board[0].length - 2; j++) {
                                if (board[i][j] == 2 && board[i][j + 1] == 2 && board[i][j + 2] == 2) {
                                    if (!(j + 3 >= board[i].length)) {
                                        if (board[i][j + 2] != 0 && onBoard(i, j+3)) {
                                            cNextX = i;
                                            cNextY = j + 3;
                                        }
                                    }
                                    if (j - 1 >= 0) {
                                        if (board[i][j - 1] != 0 && onBoard(i, j-1)) {
                                            cNextX = i;
                                            cNextY = j - 1;
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < board.length - 2; i++) {
                            for (int j = 0; j < board[1].length - 2; j++) {
                                if (board[i][j] == 2 && board[i + 1][j + 1] == 2 && board[i + 2][j + 2] == 2) {
                                    if (i + 3 < board.length && j + 3 < board[i].length) {
                                        if (board[i + 3][j + 2] != 0 && onBoard(i+3, j+3)) {
                                            cNextX = i + 3;
                                            cNextY = j + 3;
                                        }
                                    }
                                    if (i - 1 >= 0 && j - 1 >= 0) {
                                        if (board[i - 1][j - 1] != 0 && onBoard(i-1, j-1)) {
                                            cNextX = i - 1;
                                            cNextY = j - 1;
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 2; i < board.length - 2; i++) {
                            for (int j = 0; j < board[1].length - 2; j++) {
                                if (board[i][j] == 2 && board[i - 1][j + 1] == 2 && board[i - 2][j + 2] == 2) {
                                    if (i - 1 >= 0 && j + 3 < board[i].length) {
                                        if (board[i - 3][j + 2] != 0 && onBoard(i-3, j+3)) {
                                            cNextX = i - 3;
                                            cNextY = j + 3;
                                        }
                                    }
                                    if (i + 1 < board.length && j - 1 >= 0) {
                                        if (board[i + 1][j - 2] != 0 && onBoard(i+1, j-1)) {
                                            cNextX = i + 1;
                                            cNextY = j - 1;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                    if (cNextX != -1 && cNextY != -1) {
                        boolean result = executePlayerTurn(2, cNextX);
                        return result ? cNextX : -1;
                    } else {
                        int nextX = -1;
                        for (int i = 0; i < board.length; i++) {
                            for (int j = 0; j < board[0].length; j++) {
                                if (board[i][j] == 2) {
                                    if (onBoard(i, j + 1) && onBoard(i, j)) {
                                        if (board[i][j+1] == 0) {
                                            nextX = i;
                                        }
                                    }
                                    if (onBoard(i+1, j) && onBoard(i+1, j-1)) {
                                        if (board[i+1][j-1] != 0 && board[i+1][j] == 0) {
                                            nextX = i+1;
                                        }
                                    }
                                    if (onBoard(i-1, j) && onBoard(i-1, j-1)) {
                                        if (board[i-1][j-1] != 0 && board[i-1][j] == 0) {
                                            nextX = i-1;
                                        }
                                    }
                                    if (onBoard(i+1, j+1) && onBoard(i+1, j)) {
                                        if (board[i+1][j] != 0 && board[i+1][j+1] == 0) {
                                            nextX = i+1;
                                        }
                                    }
                                    if (onBoard(i+1, j-1) && onBoard(i+1, j-2)) {
                                        if (board[i+1][j-2] != 0 && board[i+1][j-1] == 0) {
                                            nextX = i+1;
                                        }
                                    }
                                    if (onBoard(i-1, j+1) && onBoard(i+1, j)) {
                                        if (board[i-1][j] != 0 && board[i-1][j+1] == 0) {
                                            nextX = i-1;
                                        }
                                    }
                                    if (onBoard(i-1, j-1) && onBoard(i-1, j-2)) {
                                        if (board[i-1][j-2] != 0 && board[i-1][j-1] == 0) {
                                            nextX = i-1;
                                        }
                                    }
                                }
                            }
                        }
                        if (nextX != -1 && onBoard(nextX, 1)) {
                            return executePlayerTurn(2, nextX) ? nextX : -1;
                        } else {
                            int column = new Random().nextInt(board.length);
                            boolean result = executePlayerTurn(2, column);
                            return result ? column : -1;
                        }
                    }
                }
            case EASY:
                int column = new Random().nextInt(board.length);
                boolean result = executePlayerTurn(2, column);
                return result ? column : -1;
        }
        return -1; //Temp
    }

    /*
    * Function will return possible highest point where dot can be placed
    * returns -1 if column is full
    * otherwise returns highest point
    * */
    private int possibleHighest(int x) {
        for (int i = 0; i < board[x].length; i++) {
            if (board[x][i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean onBoard(int x, int y) {
        return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
    }

    public enum Difficulty {
        EASY, NORMAL, HARD
    }
}
