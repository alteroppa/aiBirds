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
        System.out.println("Generating a new level...");
        int blocks = blocksToCreate;
        if (dominoStructureList.size() > 0){
            // check for xValues of dominoStructureList
            int dominoStructureStartingXValue = 200;
            int dominoStructureEndingXValue = 0;
            for (JSONObject block : dominoStructureList){
                int xVal = Integer.parseInt(block.get("x").toString());
                if (xVal < dominoStructureStartingXValue){
                    dominoStructureStartingXValue = xVal;
                }
                if (xVal > dominoStructureEndingXValue){
                    dominoStructureEndingXValue = xVal;
                }
            }
            for (int i = dominoStructureStartingXValue; i == dominoStructureEndingXValue; i++){
                usedXvalues.add(i);
            }
        }

        // create foundation of level
        JSONObject wholeLevel = new JSONObject();
        JSONObject world = new JSONObject();

        // add randomized terrain
        int coinFlip = ThreadLocalRandom.current().nextInt(0, 10 + 1);
        int numberOfTerrainBlocks = 0;
        if (coinFlip <= 5) {
            System.out.println("adding terrain block...");
            numberOfTerrainBlocks = 1;
            blocks += 1;
            JSONObject terrainBlock = new JSONObject();
            terrainBlock.put("angle", 0);
            terrainBlock.put("id", TERRAIN.randomBlock().toString());
            terrainBlock.put("x", getRandomXInt());
            terrainBlock.put("y", -1); // y should always be -1, else blocks will be created in mid air
            System.out.println(terrainBlock.toString());
            world.put("block_" + blocks, terrainBlock); // add block with last block number
        }

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
        camera2.put("x", 78.241);
        camera2.put("y", -12.354);
        cameraArray.add(camera2);
        wholeLevel.put("camera", cameraArray);

        // add basic necessary stuff
        JSONObject counts = new JSONObject();
        counts.put("birds", 4);
        counts.put("blocks", blocks);
        wholeLevel.put("counts", counts);
        wholeLevel.put("id", "pack1/Level6.lua");
        wholeLevel.put("scoreEagle", 65900);
        wholeLevel.put("scoreGold", 64000);
        wholeLevel.put("scoreSilver", 51000);
        wholeLevel.put("theme", "BACKGROUND_BLUE_GRASS");

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



        // add domino structure
        System.out.println("adding dominoStructure...");
        for (int i = 0; i < dominoStructureList.size(); i++) {
            System.out.println("domino: " + dominoStructureList.get(i).toString());
            world.put("block_" + (blocks - i - numberOfTerrainBlocks), dominoStructureList.get(i));
            System.out.println(dominoStructureList.get(i).toString() + " " + (blocks - (i)));
        }

        // add randomized blocks
        System.out.println("adding randomized blocks...");
        for (int i = 0; i < (blocks - dominoStructureList.size() - numberOfTerrainBlocks); i++){
            System.out.println("creating block " + i + "...");
            world.put("block_"+(i+1), createRandomJSONBlock());
        }

        wholeLevel.put("world", world);
        level = wholeLevel;
    }

    public JSONObject createRandomJSONBlock () {
        JSONObject jsonBlock = new JSONObject();

        jsonBlock.put("angle", getRandomAngle());
        jsonBlock.put("id", BLOCK.randomBlock().toString());
        jsonBlock.put("x", getRandomXInt());
        jsonBlock.put("y", -1); // y should always be -1, else blocks will be created in mid air

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

    public int getRandomXInt () {
        int min = 15;
        int max = 100;

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        // then check if xValue is already used so no blocks are at the same x-position
        int randomXvalue = ThreadLocalRandom.current().nextInt(min, max + 1);
        while (usedXvalues.contains(randomXvalue)) {
            randomXvalue = ThreadLocalRandom.current().nextInt(min, max + 1);
        }
        usedXvalues.add(randomXvalue);
        System.out.println("random xValue: "+randomXvalue);
        return randomXvalue;
    }


    public JSONObject getLevel () {
        return level;
    }
}
