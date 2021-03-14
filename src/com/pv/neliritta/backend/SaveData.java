package com.pv.neliritta.backend;
// Written by Gregor Suurvarik

public class SaveData implements java.io.Serializable {

    // Constant
    private  static final long serialVersionUID = 1L;

    // Data held in save
    public int[][] board;
    public int whoseTurn;
    public BackEnd.Difficulty difficulty;
}
