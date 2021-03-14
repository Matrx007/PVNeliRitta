package com.pv.neliritta.backend;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SaveManager {

    /*
    * Method saves save data to a file if fails throws Exception
    * save - SaveData object
    * saveName - name of the save file
    * */
    public static void saveToFile(Serializable save, String saveName) throws Exception {
        // Make variable
        ObjectOutputStream objectOutputStream = null;
        // Tries to do its thing
        try {
            // Makes new output stream
            objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get("save/" + saveName + ".sav")));
            // Sends data to stream
            objectOutputStream.writeObject(save);
        // When done close stream
        } finally {
            objectOutputStream.close();
        }
    }

    /*
     * Method load save data from a file if fails throws Exception
     * saveName - name of the save file
     *
     * Returns: SaveData object
     * */
    public static Object loadSaveFile(String saveName) throws Exception {
        // Make variable
        ObjectInputStream objectInputStream = null;
        // Tries to do its thing
        try {
            // Makes new input stream
            objectInputStream = new ObjectInputStream(Files.newInputStream(Paths.get("save/" + saveName + ".sav")));
            // Reads data from stream and returns it
            return objectInputStream.readObject();
        // When done close stream
        } finally {
            objectInputStream.close();
        }
    }

    /*
    * Method creates and calls save of data held in BackEnd (board, whoseTurn)
    * backEnd - BackEnd instance
    * saveName - name of the save
     */
    public static void saveGame(BackEnd backEnd, String saveName) throws Exception {
        // New saveData
        SaveData save = new SaveData();
        // Setting values
        save.board = backEnd.currentBoardState();
        save.whoseTurn = backEnd.getWhoseTurn();
        save.difficulty = backEnd.getDifficulty();
        // Calling saveing method
        saveToFile(save, saveName);
    }

    /*
     * Method load and creates BackEnd with values from save (board, whoseTurn)
     * saveName - name of the save
     *
     * Returns: BackEnd instance
     */
    public static BackEnd loadGame(String saveName) {
        try {
            // Getting saveData from loading a save
            SaveData save = (SaveData) loadSaveFile(saveName);
            // Making new BackEnd with save data
            return new BackEnd(save.board, save.whoseTurn, save.difficulty);
        } catch (Exception e) {
            /* TODO: Do something with error */
            // Logs Error To System logger
            System.out.println("Save load error: " + e.getMessage());
        }
        // For safety reasons
        return null;
    }

    public static ArrayList<String> listSaves() {
        File[] files = new File("save/").listFiles();
        ArrayList<String> fileNames = new ArrayList<String>();
        for (File file: files) {
            if (file.getName().substring(file.getName().length() - 4).equals(".sav")) {
                fileNames.add(file.getName().substring(0, file.getName().length() - 4));
            }
        }
        return fileNames;
    }


}
