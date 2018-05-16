import com.sun.org.apache.regexp.internal.CharacterArrayCharacterIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by felix on 23.04.18.
 */
public class BasicLevel {
    JSONObject level = new JSONObject();
    List<Integer> usedXvalues = new ArrayList<>();

    public void createSingleLevel(int blocksToCreate, ArrayList<JSONObject> dominoStructureList){
        System.out.println("Generating a new level..." + "\n" + usedXvalues);

        //int blocks = blocksToCreate;

        // create foundation of level
        JSONObject wholeLevel = new JSONObject();
        JSONObject world = new JSONObject();

        // add one pig at the beginning of the level
        int numberOfPigs = 1;
        JSONObject pig = new JSONObject();
        pig.put("angle", 0);
        pig.put("id", "PIG_BASIC_MEDIUM");
        pig.put("x", 25);
        pig.put("y", -1);
        world.put("block_"+ (blocksToCreate + numberOfPigs), pig); // add pig with number of blocks to create plus one as last block
        addToUsedXValues(20, 30);

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

        // add randomized terrain
        int numberOfTerrainBlocks = 0;
        if (ThreadLocalRandom.current().nextBoolean()) {
            System.out.println("adding terrain block...");
            numberOfTerrainBlocks = 1;
            JSONObject terrainBlock = new JSONObject();
            String terrainBlockString = TERRAIN.randomBlock().toString();
            int xVal = Integer.parseInt(Character.toString(terrainBlockString.substring(terrainBlockString.lastIndexOf("X") - 1).charAt(0)));
            int randomXInt = getRandomXInt(false, xVal);
            terrainBlock.put("angle", 0);
            terrainBlock.put("id", terrainBlockString);
            terrainBlock.put("x", randomXInt);
            terrainBlock.put("y", -1); // y should always be -1, else blocks will be created in mid air
            System.out.println(terrainBlock.toString());
            world.put("block_" + (blocksToCreate + numberOfPigs + numberOfTerrainBlocks), terrainBlock); // add block with last block number
            addToUsedXValues(randomXInt, randomXInt+xVal);
        }

        // add domino structure
        System.out.println("adding dominoStructure...");
        for (int i = 0; i < dominoStructureList.size(); i++) {
            System.out.println("domino: " + dominoStructureList.get(i).toString());
            world.put("block_" + (blocksToCreate + numberOfTerrainBlocks + numberOfPigs + i+1), dominoStructureList.get(i));
            System.out.println(dominoStructureList.get(i).toString() + " " + (blocksToCreate - (i)));
        }

        // add randomized blocks
        System.out.println("adding randomized blocks...");
        for (int i = 0; i < blocksToCreate; i++){
            System.out.println("creating block " + (i + 1) + "...");
            JSONObject randomBlock = createRandomJSONBlock();
            world.put("block_"+(i+1), randomBlock); // since the first block is the pig
            System.out.println(randomBlock);
        }

        // add basic necessary stuff
        JSONObject counts = new JSONObject();
        counts.put("birds", 4);
        counts.put("blocks", (blocksToCreate + numberOfTerrainBlocks + numberOfPigs + dominoStructureList.size()));
        wholeLevel.put("counts", counts);
        wholeLevel.put("id", "pack1/Level6.lua");
        wholeLevel.put("scoreEagle", 65900);
        wholeLevel.put("scoreGold", 64000);
        wholeLevel.put("scoreSilver", 51000);
        wholeLevel.put("theme", "BACKGROUND_BLUE_GRASS");
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
        wholeLevel.put("camera", cameraArray);

        wholeLevel.put("world", world);
        level = wholeLevel;
    }

    public JSONObject createRandomJSONBlock () {
        JSONObject jsonBlock = new JSONObject();
        int angle = getRandomAngle();
        jsonBlock.put("angle", angle);
        String randomBlockString = BLOCK.randomBlock().toString();
        int endingXVal = 0;
        int secondVal = Integer.parseInt(Character.toString(randomBlockString.substring(randomBlockString.lastIndexOf("X") + 1).charAt(0)));
        int firstVal = Integer.parseInt(Character.toString(randomBlockString.substring(randomBlockString.lastIndexOf("X") - 1).charAt(0)));

        if (angle == 0 || angle == 180) {
            endingXVal = firstVal + 2;
        } else {
            endingXVal = secondVal + 2;
        }
        jsonBlock.put("id", randomBlockString);
        int randomXval = getRandomXInt(false, endingXVal);
        jsonBlock.put("x", randomXval);
        jsonBlock.put("y", -2); // y should always be -1 or -2 (for terrain), else blocks will be created in mid air
        System.out.println("randomXVal: " + randomXval + "\n" + "endingXVal: " + (endingXVal + randomXval));
        addToUsedXValues(randomXval, randomXval+endingXVal);
        return jsonBlock;
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
        int min = 45;
        int max = 130;
        int numberOfTries = 0;
        System.out.println("blockEndXVal: " + blockEndXVal);

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        // then check if xValue is already used so no blocks are at the same x-position
        int randomXvalue = ThreadLocalRandom.current().nextInt(min, max + 1);
        int randomXValueBuffer = randomXvalue;
        System.out.println("randomxVal: " + randomXvalue);
        for (int i = randomXValueBuffer; i <= randomXValueBuffer + blockEndXVal; i++) {
            while (usedXvalues.contains(randomXvalue) && !ignoreUsedXVals) {
                numberOfTries++;
                if (numberOfTries > 5000) {
                    randomXvalue = ThreadLocalRandom.current().nextInt(max, 180 + 1);;
                    System.out.println("Warning!Number of tries for random int > 500! New randomXValue is " + randomXvalue);
                } else {
                    randomXvalue = ThreadLocalRandom.current().nextInt(min, max + 1);
                }
            }
        }
        System.out.println("usedValues: " + usedXvalues);
        return randomXvalue;
    }

    public void addToUsedXValues (int valueToAdd, int endingValOfBlock) {
        System.out.println("adding values to usedvaluesList: " + valueToAdd + " " + (endingValOfBlock+valueToAdd));
        for (int i = valueToAdd - 1; i <= endingValOfBlock + 1; i++) {
            usedXvalues.add(i);
        }
    }



    public JSONObject getLevel () {
        return level;
    }
}
