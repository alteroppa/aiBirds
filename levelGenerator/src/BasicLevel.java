import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents the basic structure of a level. It contains the foundations: birds, pigs, random objects.
 */
public class BasicLevel {
    JSONObject level = new JSONObject();
    JSONObject world = new JSONObject();
    int randomBlocksToCreate = 0;
    ArrayList<Integer> pigXValues = new ArrayList<>();
    ArrayList<Integer> usedXvalues = new ArrayList<>();
    ArrayList<Integer> usedTerrainXvalues = new ArrayList<>();
    int terrainBlockHeight = 0;
    int numberOfPigs = 0;
    int numberOfTerrainBlocks = 0;
    ArrayList<JSONObject> additionalStructureList = new ArrayList<>();
    boolean additionalStructureIsDominoStructure = false;
    public int additionalStructureStartingXValue;

    public BasicLevel(ArrayList<JSONObject> additionalStructureList, ArrayList<Integer> pigXValues){
        this.additionalStructureList = additionalStructureList;
        this.pigXValues = pigXValues;
    }
    public BasicLevel(ArrayList<JSONObject> additionalStructureList) {
        this.additionalStructureList = additionalStructureList;
    }

    public void createSingleLevel(int randomBlocksToCreate, int numberOfPigs, int numberOfTerrainBlocks, boolean ignoreUsedXValues){
        System.out.println("Generating a new level..." + "\n" + usedXvalues);
        this.randomBlocksToCreate = randomBlocksToCreate;

        // create and add camera array
        JSONArray cameraArray = new JSONArray();
        JSONObject camera1 = new JSONObject();
        camera1.put("bottom", 14.338);
        camera1.put("id", "Slingshot");
        camera1.put("left", -39.913);
        camera1.put("right", 33.515);
        camera1.put("top", -40.733);
        camera1.put("x", 49.894);
        camera1.put("y", -13.198);
        cameraArray.add(camera1);

        JSONObject camera2 = new JSONObject();
        camera2.put("bottom", 11.728);
        camera2.put("id", "Castle");
        camera2.put("left", 13.039);
        camera2.put("right", 77.257);
        camera2.put("top", -36.435);
        camera2.put("x", 120.241);
        camera2.put("y", -12.354);
        cameraArray.add(camera2);
        level.put("camera", cameraArray);

        // add birds
        JSONObject bird1 = new JSONObject();
        bird1.put("angle", 0);
        bird1.put("id", "BIRD_RED");
        bird1.put("x", 10.153);
        bird1.put("y", -0.944);
        JSONObject bird2 = new JSONObject();
        bird2.put("angle", 0);
        bird2.put("id", "BIRD_RED");
        bird2.put("x", 6.113);
        bird2.put("y", -1.142);
        JSONObject bird3 = new JSONObject();
        bird3.put("angle", 0);
        bird3.put("id", "BIRD_RED");
        bird3.put("x", 2.878);
        bird3.put("y", -1.143);
        JSONObject bird4 = new JSONObject();
        bird4.put("angle", 0);
        bird4.put("id", "BIRD_RED");
        bird4.put("x", 0);
        bird4.put("y", -1.139);
        world.put("bird_1", bird1);
        world.put("bird_2", bird2);
        world.put("bird_3", bird3);
        world.put("bird_4", bird4);

        // add terrain blocks
        //addTerrainBlocks(numberOfTerrainBlocks);
        // add additional structure (dominostructure, etc)
        addAdditionalStructure();
        // add pigs
        addPigs(numberOfPigs);

        // add randomized blocks
        System.out.println("adding randomized blocks...");
        addRandomJSONBlocks();

        System.out.println("number of blocks all in all: " + (this.randomBlocksToCreate + this.numberOfPigs + this.numberOfTerrainBlocks + additionalStructureList.size()));

        // add basic necessary stuff
        JSONObject counts = new JSONObject();
        counts.put("birds", 4);
        counts.put("blocks", (this.randomBlocksToCreate + this.numberOfPigs + this.numberOfTerrainBlocks + this.additionalStructureList.size()));
        level.put("counts", counts);
        level.put("id", "pack1/Level6.lua");
        level.put("scoreEagle", 65900);
        level.put("scoreGold", 64000);
        level.put("scoreSilver", 51000);
        level.put("theme", "BACKGROUND_BLUE_GRASS");



        level.put("world", world);
    }

    private void addPigs(int numberOfPigs) {
        this.numberOfPigs = numberOfPigs;
        for (int i = 0; i < numberOfPigs; i++){
            JSONObject pig = new JSONObject();
            int randomPigXint = additionalStructureIsDominoStructure && numberOfPigs == 1 ? 120 : getRandomXInt(false, 2);
            pig.put("angle", 0);
            pig.put("id", "PIG_BASIC_MEDIUM");
            pig.put("x", randomPigXint);
            pig.put("y", -1);
            world.put("block_"+ (this.randomBlocksToCreate + numberOfTerrainBlocks + additionalStructureList.size() + i+1), pig); // add pig with number of blocks to create plus one as last block
            addToUsedXValues(randomPigXint, 4);
            pigXValues.add(randomPigXint);
        }
    }

    private void addAdditionalStructure() {
        System.out.println("adding additionalStructure...");
        for (int i=0; i < additionalStructureList.size(); i++) {
            System.out.println("block: " + additionalStructureList.get(i).toString());
            world.put("block_" + (randomBlocksToCreate + numberOfTerrainBlocks + i+1), additionalStructureList.get(i));
            System.out.println(additionalStructureList.get(i).toString() + " " + (randomBlocksToCreate - (i)));
        }
    }

    public void addRandomJSONBlocks () {
        for (int i=0; i < this.randomBlocksToCreate; i++) {
            System.out.println("creating randomBlock " + (i) + "...");
            JSONObject randomBlock = new JSONObject();
            int angle = getRandomAngle();
            randomBlock.put("angle", angle);


            String randomBlockString = BLOCK.randomBlock().toString();
            int endingXVal = 0;
            int secondVal = Integer.parseInt(Character.toString(randomBlockString.substring(randomBlockString.lastIndexOf("X") + 1).charAt(0)));
            int firstVal = Integer.parseInt(Character.toString(randomBlockString.substring(randomBlockString.lastIndexOf("X") - 1).charAt(0)));

            if (angle == 0 || angle == 180) {
                endingXVal = firstVal;
            } else {
                endingXVal = secondVal;
            }
            int randomXval = getRandomXInt(false, endingXVal + 2);

            // make sure the first block of the domino structure is reachable
            if (randomXval < additionalStructureStartingXValue) {
                randomBlockString = SMALLBLOCK.randomBlock().toString();
                endingXVal = 0;
                secondVal = Integer.parseInt(Character.toString(randomBlockString.substring(randomBlockString.lastIndexOf("X") + 1).charAt(0)));
                firstVal = Integer.parseInt(Character.toString(randomBlockString.substring(randomBlockString.lastIndexOf("X") - 1).charAt(0)));

                if (angle == 0 || angle == 180) {
                    endingXVal = firstVal;
                } else {
                    endingXVal = secondVal;
                }
                randomXval = getRandomXInt(false, endingXVal + 2);
            }

            randomBlock.put("id", randomBlockString);
            randomBlock.put("x", randomXval);
            randomBlock.put("y", -1); // y should always be -1 or -2 (for terrain), else blocks will be created in mid air
            //System.out.println("randomXVal: " + randomXval + "\n" + "endingXVal: " + (endingXVal + randomXval));
            addToUsedXValues(randomXval, endingXVal);
            world.put("block_"+(i+1), randomBlock);
            System.out.println(randomBlock);
        }
    }

    public void addTerrainBlocks(int numberOfTerrainBlocks) {
        this.numberOfTerrainBlocks = numberOfTerrainBlocks;
        for (int i = 0; i < numberOfTerrainBlocks; i++){
            System.out.println("adding terrain block...");
            JSONObject terrainBlock = new JSONObject();
            String terrainBlockString = TERRAIN.randomBlock().toString();
            int xVal = 10;
            int xStringIndex = terrainBlockString.indexOf("X");
            terrainBlockHeight = 10;

            // extracts the xVal of the block
            /*if (Character.toString(terrainBlockString.charAt(xStringIndex - 2)).equals("_")) {
                String blockXValSubstring = terrainBlockString.substring(xStringIndex - 1, xStringIndex);
                xVal = Integer.parseInt(blockXValSubstring);
            } else if (Character.toString(terrainBlockString.charAt(xStringIndex - 3)).equals("X")) {
                String blockXValSubstring = terrainBlockString.substring(terrainBlockString.length() - 2, xStringIndex);
                xVal = Integer.parseInt(blockXValSubstring);
            }*/

                // extracts the height of the block (= value on the last char of the enum)
            /*if (xStringIndex + 2 == terrainBlockString.length()) {
                String blockHeightSubstring = terrainBlockString.substring(xStringIndex + 1, xStringIndex +1);
                System.out.println(blockHeightSubstring);
                terrainBlockHeight = Integer.parseInt(blockHeightSubstring);
            } else if (xStringIndex + 2 > terrainBlockString.length() && ) {
                String blockHeightSubstring = terrainBlockString.substring(xStringIndex + 1, terrainBlockString.length() - 3);
                System.out.println(blockHeightSubstring);
                terrainBlockHeight = Integer.parseInt(blockHeightSubstring);
            } else {
                terrainBlockHeight = Integer.parseInt(Character.toString(terrainBlockString.charAt(terrainBlockString.length() - 3)));
            }*/
            System.out.println("terrainBlockHeight: " + terrainBlockHeight);
            System.out.println("terrainBlockxVal: " + xVal);
            int randomXInt = getRandomXInt(false, xVal);
            terrainBlock.put("angle", 0);
            terrainBlock.put("id", terrainBlockString);
            terrainBlock.put("x", randomXInt);
            terrainBlock.put("y", -1); // y should always be -1, else blocks will be created in mid air
            System.out.println(terrainBlock.toString());
            addToUsedTerrainXValues(randomXInt, randomXInt+xVal);
            world.put("block_" + (randomBlocksToCreate + i+1), terrainBlock); // add block with last block number
        }

    }

    public int getRandomAngle () {
        ArrayList<Integer> angleList = new ArrayList<Integer>();
        angleList.add(0);
        angleList.add(90);
        angleList.add(180);
        angleList.add(270);
        Collections.shuffle(angleList);
        return (angleList.get(0));
    }

    public int getRandomXInt (boolean ignoreUsedXVals, int blockEndXVal) {
        int min = 35;
        int max = 130;
        int numberOfTries = 0;
        System.out.println("blockEndXVal: " + blockEndXVal);

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        // then check if xValue is already used so no blocks are at the same x-position
        int randomXvalue = 0;
        boolean valueAlreadyInUse = true;
        while (valueAlreadyInUse && !ignoreUsedXVals){
            numberOfTries++;
            if (numberOfTries > 5000) {
                max = 150;
            }
            valueAlreadyInUse = false;
            randomXvalue = ThreadLocalRandom.current().nextInt(min, max + 1);
            //System.out.println("randomxVal: " + randomXvalue);
            for (int i = randomXvalue; i <= randomXvalue + blockEndXVal; i++) {
                if (usedXvalues.contains(i)) {
                    valueAlreadyInUse = true;
                }
            }
        }

        while (valueAlreadyInUse && ignoreUsedXVals){
            numberOfTries++;
            if (numberOfTries > 5000) {
                max = 150;
            }
            valueAlreadyInUse = false;
            randomXvalue = ThreadLocalRandom.current().nextInt(min, max + 1);
            //System.out.println("randomxVal: " + randomXvalue);
            for (int i = randomXvalue; i <= randomXvalue + blockEndXVal; i++) {
                if (pigXValues.contains(i)) {
                    valueAlreadyInUse = true;
                }
            }
        }

        return randomXvalue;
    }

    public void addToUsedXValues (int valueToAdd, int endingValOfBlock) {
        //System.out.println("adding values to usedvaluesList: " + valueToAdd + " " + (endingValOfBlock+valueToAdd));
        for (int i = valueToAdd; i <= valueToAdd + endingValOfBlock; i++) {
            usedXvalues.add(i);
        }
    }

    public boolean isOnTerrainXValues (int xValue) {
        if (usedTerrainXvalues.contains(xValue)) {
            return true;
        } else {
            return false;
        }
    }

    public void addToUsedTerrainXValues (int valueToAdd, int endingValOfBlock) {
        System.out.println("adding values to usedvaluesList: " + valueToAdd + " " + (endingValOfBlock));
        for (int i = valueToAdd; i <= endingValOfBlock; i++) {
            usedTerrainXvalues.add(i);
        }
    }
    public void addPigXValue(int pigXValue) {
        this.pigXValues.add(pigXValue);
    }



    public JSONObject getLevel () {
        return level;
    }
}
