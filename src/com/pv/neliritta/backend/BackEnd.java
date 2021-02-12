package com.pv.neliritta.backend;
// Written by Rainis Randmaa (function names, parameters and return types only)
// Written by Gregor Suurvarik (everything else)


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
    public BackEnd(int boardWidth, int boardHeight) {
        /* TODO */
    }

    /*
    * When a saved game is loaded, this constructor will be called.
    *
    * 'board' will contain the current state of the game board.
    *
    * 'whoseTurn' is either a '0' when no-one is winning (game is still going on) or
    * an ID of the player whose turn is at the moment (meaning (s)he hasn't done anything yet but is about to'.
    * */
    public BackEnd(int[][] board, int whoseTurn) {
        /* TODO */
    }

    /*
    * Should be modified after every turn by either the function 'executePlayersTurn(int playerID)' or
    * 'executeComputersTurn()'.
    * */
    private int[][] board;

    /*
    * Returns the current state of the game board (aka 'private int[][] board').
    * */
    public int[][] currentBoardState() {
        /* TODO */
        return null;
    }

    /*
    * This will usually get called after a call to 'executePlayerTurn(..)' or 'executeComputerTurn()'.
    *
    * Returns either a '0' when no-one is winning (game is still going on) or an ID of the player who
    * got 4 circles in a row, column or in a diagonal.
     * */
    public int whoWon() {
        /* TODO */
        return 0;
    }

    /*
    * This function will be executed when player 1 or player 2 places a circle.
    * This function also could be called by the executeComputerTurn() in case Gregor finds it easier that way.
    * 
    * playerID - either 1 or 2, representing either player 1 or player 2
    * x - the column where to place the circle (left to right)
    *
    * Returns false if a circle could not be placed at the given location.
    * */
    public boolean executePlayerTurn(int playerID, int x) {
        /* TODO */
        return false;
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
        /* TODO */
        return -1;
    }
}
