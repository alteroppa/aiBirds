import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class creates the domino structure. It also makes sure there are no random blocks at the location of
 * the domino structure.
 */
public class DominoStructure {
    private int startingXValue = 0;
    private int endXValue = 0;

    private double yValue = -1;

    public ArrayList<JSONObject> createDominoStructure (int levelCount, boolean saveCoordinates) {

        // makes sure the xValueToUse for the blocks is far enough away from the sling
        startingXValue = ThreadLocalRandom.current().nextInt(30, 99 + 1);

        // add terrain blocks underneath or not
        boolean firstTerrainBlockBelow = ThreadLocalRandom.current().nextBoolean();
        boolean secondTerrainBlockBelow = ThreadLocalRandom.current().nextBoolean();
        JSONObject firstBlockBelow = new JSONObject();
        JSONObject secondBlockBelow = new JSONObject();
        if (firstTerrainBlockBelow){
            firstBlockBelow.put("angle", 0);
            firstBlockBelow.put("id", TERRAIN.TERRAIN_TEXTURED_HILLS_32X2.toString());
            firstBlockBelow.put("x", startingXValue + 10);
            firstBlockBelow.put("y", yValue);
            yValue = -2.5;
            if (secondTerrainBlockBelow){
                secondBlockBelow.put("angle", 0);
                secondBlockBelow.put("id", TERRAIN.TERRAIN_TEXTURED_HILLS_32X2.toString());
                secondBlockBelow.put("x", startingXValue + 10);
                secondBlockBelow.put("y", yValue);
                yValue = -4.5;
            }
        }


        String randomBlock = VERTICALDOMINOBLOCK.randomBlock().toString();
        // String randomBlock = VERTICALDOMINOBLOCK.STONE_BLOCK_8X1.toString();

        // extracts the height of the block (= value on the last char of the enum)
        int blockHeight = 0;
        if (Character.toString(randomBlock.charAt(randomBlock.length() - 3)).equals("0")) {
            blockHeight = Integer.parseInt(Character.toString(randomBlock.charAt(randomBlock.length() - 4)) + Character.toString(randomBlock.charAt(randomBlock.length() - 3)));
        } else {
            blockHeight = Integer.parseInt(Character.toString(randomBlock.charAt(randomBlock.length() - 3)));
        }

        // sets the distance between the three blocks randomly, but not further away than (blockHeight - 1)
        // nextInt doesn't go until maxvalue but stops one before
        System.out.println("dominoStructure blockheight: " + blockHeight);
        int distance = ThreadLocalRandom.current().nextInt(3, 10); // distance should be at least 3, but no higher than 7
        //distance = 3; // set fixed distance
        endXValue = startingXValue + (distance * 2) + 10; // should actually be * 2 only, but to make some more space behind the structure...

        ArrayList<JSONObject> dominoStructureArrayList = new ArrayList<JSONObject>();

        JSONObject part1DominoStructure = new JSONObject();
        part1DominoStructure.put("angle", 90);
        part1DominoStructure.put("id", randomBlock);
        part1DominoStructure.put("x", startingXValue);
        part1DominoStructure.put("y", yValue);

        // if (distance < 4) { dominoStructureArrayList.add(createConcreteBlock(xValueToUse)); }

        JSONObject part2DominoStructure = new JSONObject();
        part2DominoStructure.put("angle", 90);
        part2DominoStructure.put("id", randomBlock);
        part2DominoStructure.put("x", startingXValue + distance);
        part2DominoStructure.put("y", yValue);

        // add randomized top block
        JSONObject topBlock = new JSONObject();
        boolean material = ThreadLocalRandom.current().nextBoolean();
        String block;
        if (material)
            block = VERTICALDOMINOBLOCK.STONE_BLOCK_10X1.toString();
        else
            block = VERTICALDOMINOBLOCK.WOOD_BLOCK_10X1.toString();
        topBlock.put("angle", 0);
        topBlock.put("id", block);
        topBlock.put("x", startingXValue + distance);
        topBlock.put("y", yValue + 5);

        boolean blockInBetweenBool = ThreadLocalRandom.current().nextInt(100) < 25;
        JSONObject blockInBetween = new JSONObject();
        blockInBetween.put("angle", 90);
        blockInBetween.put("id", TERRAIN.TERRAIN_TEXTURED_HILLS_10X2.toString());
        // put randomly between first or second block
        if (ThreadLocalRandom.current().nextBoolean()){
            blockInBetween.put("x", startingXValue + ThreadLocalRandom.current().nextInt(1,3));
        } else {
            blockInBetween.put("x", startingXValue + distance + ThreadLocalRandom.current().nextInt(1,3));
        }
        blockInBetween.put("y", yValue - 4);

        JSONObject part3DominoStructure = new JSONObject();
        part3DominoStructure.put("angle", 90);
        part3DominoStructure.put("id", randomBlock);
        part3DominoStructure.put("x", startingXValue + (2* distance));
        part3DominoStructure.put("y", yValue);

        ArrayList<String> pigOrTNTString = new ArrayList<String>();
        pigOrTNTString.add("PIG_BASIC_SMALL");
        pigOrTNTString.add("MISC_EXPLOSIVE_TNT");
        JSONObject pigOrTNTAtEnd = new JSONObject();
        pigOrTNTAtEnd.put("angle", 90);
        pigOrTNTAtEnd.put("id", pigOrTNTString.get(new Random().nextInt(pigOrTNTString.size())));
        pigOrTNTAtEnd.put("x", startingXValue + (2*distance) + 7);
        pigOrTNTAtEnd.put("y", yValue);

        dominoStructureArrayList.add(part1DominoStructure);
        dominoStructureArrayList.add(part2DominoStructure);
        //if (ThreadLocalRandom.current().nextBoolean() && distance <= 4 && !blockInBetweenBool) // randomly add top block
        //    dominoStructureArrayList.add(topBlock);
        if (firstTerrainBlockBelow) // randomly add one terrain block below
            dominoStructureArrayList.add(firstBlockBelow);
        if (firstTerrainBlockBelow && secondTerrainBlockBelow) // randomly add another terrain block below
            dominoStructureArrayList.add(secondBlockBelow);
        if (blockInBetweenBool) // randomly add block in between or leave out
            dominoStructureArrayList.add(blockInBetween);
        if (ThreadLocalRandom.current().nextInt(100) < 80) // randomly skip last structure part to see if it still works
            dominoStructureArrayList.add(part3DominoStructure);
        dominoStructureArrayList.add(pigOrTNTAtEnd);

        if (saveCoordinates) {
            saveCoordinates(levelCount, yValue, (startingXValue+distance), distance);
        }

        return dominoStructureArrayList;
    }

    public void saveCoordinates(int levelCount, double yValueMiddleBlock, int xValueMiddleBlock, int distance){
        Writer output = null;  //clears file every time
        try {
            File coordinatesFile = new File("generatedLevels/dominoCoordinates.txt");
            if (coordinatesFile.createNewFile()) {
                coordinatesFile.delete();
            }
            output = new BufferedWriter(new FileWriter("generatedLevels/dominoCoordinates.txt", true));
            String line = levelCount+";"+xValueMiddleBlock+";"+yValueMiddleBlock+";"+distance+";\n";
            output.append(line);
            output.close();
        } catch (IOException e) {
            System.out.println("something went wrong writing dominoCoordinates.txt");
            e.printStackTrace();
        }

    }

    public JSONObject createConcreteBlock(int startingXValue){
        JSONObject concreteBlock = new JSONObject();
        concreteBlock.put("angle", 0);
        concreteBlock.put("id", BLOCK.STONE_BLOCK_4X4_HOLLOW);
        concreteBlock.put("x", startingXValue + 1);
        return concreteBlock;
    }


    public void setStartingXValue(int startingXValue) {this.startingXValue = startingXValue;}
    public double getyValue() { return yValue; }
    public void setyValue(int yValue) { this.yValue = yValue; }
    public int getStartingXValue() { return startingXValue; }
    public int getEndXValue() { return endXValue; }
}
