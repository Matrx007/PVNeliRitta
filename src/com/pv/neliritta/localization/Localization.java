package com.pv.neliritta.localization;

import com.pv.neliritta.gui.Action;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Localization {


    public static class Term {
        public final String group, key;
        public String value;

        public Term(String group, String key, String value) {
            this.group = group;
            this.key = key;
            this.value = value;
        }

        public Term(String value) {
            this.group = null;
            this.key = null;

            this.value = value;
        }
    }

    // #######################
    // ### ACCESSING TERMS ###
    // #######################

    /* This is intended to display the language choice to the user
     *
     * Key is meant to be the language's name in human readable format
     * Key's value is meant to locate the localization file
     */
    private static final LinkedHashMap<String, String> localizationFileNames = new LinkedHashMap<>();
    static {
        localizationFileNames.put("English", "en-US.lang");
        localizationFileNames.put("Eesti", "et-EE.lang");
    }

    /* Returns a term translated into the selected language
    *
    * 'group' represents a group inside the localization file, ex:  [MAIN MENU]
    * 'key' represents a key inside the localization file, ex:      pause: "Pause"
    * */
    public static String term(String group, String key) {
        HashMap<String, String> keyGroup = termMap.get(group);

        if(keyGroup == null) {
            System.err.println("Error while reading a term: no such group: '"+ group.toLowerCase() +"'");
            return null;
        }

        String term = keyGroup.get(key);

        if(term == null) {
            System.err.println("Error while reading a term: no such key: '"+ key.toLowerCase() +"' in group'"+ group.toLowerCase() +"'");
            return null;
        }


        return term;
    }

    /* Returns reference to the term which will always be up to date after changing the language
    * */
    private static HashMap<String, HashMap<String, Term>> termReferences = new HashMap<>();
    public static Term reference(String group, String key) {
        group = group.toUpperCase();
        key = key.toLowerCase();
        // If a reference to this term is already cached, give out a reference to that

        reference: {
            HashMap<String, Term> foundGroup = termReferences.get(group);
            if(foundGroup == null) {
                break reference;
            }

            Term foundTerm = foundGroup.get(key);
            if(foundTerm == null) {
                break reference;
            }

            return foundTerm;
        }

        // If a reference to this term is not cached yet, find it
        {
            HashMap<String, String> foundGroup = termMap.get(group);
            if(foundGroup == null) {
                System.err.println("No such group '"+group+"'");
                return null;
            }

            String foundTerm = foundGroup.get(key);
            if(foundTerm == null) {
                System.err.println("No such key '"+key+"' in group '"+group+"'");
                return null;
            }

            // Cache the found term and give out a reference to that
            {
                HashMap<String, Term> targetGroup = termReferences.get(group);
                Term newTerm;
                if(targetGroup == null) {
                    targetGroup = new HashMap<>();

                    newTerm = new Term(group, key, foundTerm);
                    targetGroup.put(key, newTerm);

                    termReferences.put(group, targetGroup);
                } else {
                    newTerm = new Term(group, key, foundTerm);
                    targetGroup.put(key, newTerm);
                }

                return newTerm;
            }
        }
    }


    // #####################################
    // ### LOADING THE LOCALIZATION FILE ###
    // #####################################

    private static HashMap<String, HashMap<String, String>> termMap = null;

    // Returns all possible language options to display to the user
    public static String[] languageChoices() {
        return localizationFileNames.keySet().toArray(new String[0]);
    }

    /*
    * languageChoice - one of the options given by languageChoices()
    *
    * Loads the correct localization file, parses it and creates a so called 'term map'
    * which can be accessed through term(..).
    * */
    public static void localize(String languageChoice) {
        String[] fileLines = null;

        // Load the localization file into String array (line by line)
        {
            String localizationFileName = localizationFileNames.get(languageChoice);

            if(localizationFileName == null) {
                System.err.println("Error while opening localization file: no such localization option: '"+ languageChoice +"'");
                return;
            }

            File localizationFile = new File("assets/localization/" + localizationFileName);

            if(!localizationFile.exists()) {
                System.err.println("Error while opening localization file: no such localization file: '"+ localizationFile.getAbsolutePath() +"'");
                return;
            }


            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(localizationFile));

                ArrayList<String> fileLinesList = new ArrayList<>();

                String line;
                while((line = bufferedReader.readLine()) != null) {
                    fileLinesList.add(line);
                }

                fileLines = fileLinesList.toArray(new String[0]);
            } catch (IOException e) {
                System.err.println("Error while opening localization file: ");
                e.printStackTrace();
            }
        }

        // Parse the localization file
        if(fileLines == null) {
            System.err.println("Error while opening localization file: the localization file couldn't be opened");
        } else {
            HashMap<String, HashMap<String, String>> parsedFile = parseFile(fileLines);

            // If localization file couldn't be loaded, use the one that is already in memory to avoid
            // being unable to render text in the game at all
            if(parsedFile == null) {
                System.err.println("Failed to parse localization file, reverting back to previous");
                return;
            }

            termMap = parsedFile;

            // Update term references
            for(HashMap<String, Term> group : termReferences.values()) {
                for(Term term : group.values()) {
                    term.value = term(term.group, term.key);
                }
            }
        }
    }









    // #####################################
    // ### PARSING THE LOCALIZATION FILE ###
    // #####################################

    /*
    * Creates a data map of the given file. The file must be in the following format:
    *
    *   [GROUP 1]
    *       key1:    "Value1"
    *       key2:   "Value2"
    *
    * All keys and titles will be stored in lowercase format to avoid possible errors.
    * Values on the other hand will be left untouched.
    */
    private static HashMap<String, HashMap<String, String>> parseFile(String[] fileLines) {
        HashMap<String, HashMap<String, String>> dataMap = new HashMap<>();

        // Current group
        // Null at first because values cannot be outside of a group
        HashMap<String, String> currentGroup = null;

        for(String line : fileLines) {
            // Line with leading and trailing spaces and tabs removed
            String formattedLine = line.replaceAll("(^[\\t ]+)|([\\t ]+$)", "");

            if(line.length() == 0) {
                continue;
            }

            // If line is a title
            if(formattedLine.startsWith("[")) {
                if(!formattedLine.endsWith("]")) {
                    System.err.println("Error while parsing localization file: expected ']' after a title");
                    return null;
                }

                formattedLine = formattedLine.substring(1, formattedLine.length()-1);

                if(dataMap.containsKey(formattedLine.toLowerCase())) {
                    System.err.println("Error while parsing localization file: title is already defined");
                    return null;
                }

                currentGroup = new HashMap<>();

                dataMap.put(formattedLine, currentGroup);
            } else {
                String key, value;

                // Extracts the key from the line and transforms it into lowercase
                // Extracts the value from the line and removes surrounding double quotes
                {
                    String[] linePieces = formattedLine.split(":", 2);

                    key = linePieces[0].replaceAll("(^[\\t ]+)|([\\t ]+$)", "").toLowerCase();

                    value = linePieces[1].replaceAll("(^[\\t ]+)|([\\t ]+$)", "");

                    if(!value.startsWith("\"") || !value.endsWith("\"")) {
                        System.err.println("Error while parsing localization file: value isn't surrounded with double quotes");
                        return null;
                    }

                    value = value.substring(1, value.length()-1);
                }

                if(currentGroup == null) {
                    System.err.println("Error while parsing localization file: cannot define a value outside of a group");
                    return null;
                }

                if(currentGroup.containsKey(key)) {
                    System.err.println("Error while parsing localization file: this key already exists in the current group");
                    return null;
                }

                currentGroup.put(key, value);
            }
        }

        return dataMap;
    }

}
