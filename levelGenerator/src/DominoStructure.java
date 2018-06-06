import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by felix on 10.04.18.
 */
public class DominoStructure {
    private int startingXValue = 0;
    private int endXValue = 0;

    public ArrayList<JSONObject> createDominoStructure () {

        // makes sure the xValueToUse for the blocks is far enough away from the sling
        startingXValue = ThreadLocalRandom.current().nextInt(30, 99 + 1);

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
        int distance = ThreadLocalRandom.current().nextInt(3, 7); // distance should be at least 3, but no higher than 7
        endXValue = startingXValue + (distance * 2) + 10; // should actually be * 2 only, but to make some more space behind the structure...

        ArrayList<JSONObject> dominoStructureArrayList = new ArrayList<JSONObject>();

        JSONObject part1DominoStructure = new JSONObject();
        part1DominoStructure.put("angle", 90);
        part1DominoStructure.put("id", randomBlock);
        part1DominoStructure.put("x", startingXValue);
        part1DominoStructure.put("y", -1);

        // if (distance < 4) { dominoStructureArrayList.add(createConcreteBlock(xValueToUse)); }

        JSONObject part2DominoStructure = new JSONObject();
        part2DominoStructure.put("angle", 90);
        part2DominoStructure.put("id", randomBlock);
        part2DominoStructure.put("x", startingXValue + distance);
        part2DominoStructure.put("y", -1);

        JSONObject part3DominoStructure = new JSONObject();
        part3DominoStructure.put("angle", 90);
        part3DominoStructure.put("id", randomBlock);
        part3DominoStructure.put("x", startingXValue + (2 * distance));
        part3DominoStructure.put("y", -1);

        dominoStructureArrayList.add(part1DominoStructure);
        dominoStructureArrayList.add(part2DominoStructure);
        dominoStructureArrayList.add(part3DominoStructure);

        return dominoStructureArrayList;
    }

    public JSONObject createConcreteBlock(int startingXValue){
        JSONObject concreteBlock = new JSONObject();
        concreteBlock.put("angle", 0);
        concreteBlock.put("id", BLOCK.STONE_BLOCK_4X4_HOLLOW);
        concreteBlock.put("x", startingXValue + 1);
        return concreteBlock;
    }

    public int getStartingXValue() {
        return startingXValue;
    }
    public int getEndXValue() {
        return endXValue;
    }
}
