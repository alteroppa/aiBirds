import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * This is the entry method. Uncomment the function calls to create n new levels.
 * SimpleLevels and DominoLevels are certain to work. FunnelLevels hasn't been
 * made sure to work, though. HouseLevel will not work.
 */
public class main {
    public static void main(String[] args){
        //createFunnelLevels(10);
        createDominoLevels(2001);
        //createSimpleLevels(10);
        //createHouseLevels(10);
    }


    public static void createFunnelLevels(int numberOfLevels){
        LevelCreator levelCreator = new LevelCreator();
        ArrayList<JSONObject> levelList = levelCreator.createFunnelLevels(numberOfLevels);
        levelCreator.exportLevelsToFiles(levelList, "funnelLevels"); // the second parameter is the folder in which to save the levels
    }
    public static void createSimpleLevels(int numberOfLevels){
        LevelCreator levelCreator = new LevelCreator();
        ArrayList<JSONObject> levelList = levelCreator.createSimpleLevels(numberOfLevels);
        levelCreator.exportLevelsToFiles(levelList, "simpleLevels");
    }
    public static void createHouseLevels(int numberOfLevels){
        LevelCreator levelCreator = new LevelCreator();
        ArrayList<JSONObject> levelList = levelCreator.createHouseLevels(numberOfLevels);
        levelCreator.exportLevelsToFiles(levelList, "houseLevels");
    }
    public static void createDominoLevels(int numberOfLevels){
        LevelCreator levelCreator = new LevelCreator();
        ArrayList<JSONObject> levelList = levelCreator.createDominoLevels(numberOfLevels);
        levelCreator.exportLevelsToFolders(levelList, "dominoLevels");
    }


}
