import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by felix on 10.04.18.
 */
public class DominoStructure {
    private int startingXValue = 0;
    private int endXValue = 0;

    public ArrayList<JSONObject> createDominoStructure (int xValue) {

        // makes sure the xValueToUse for the blocks is far enough away from the sling
        int xValueToUse = 14;
        if (xValue > 14) {
            xValueToUse = xValue;
        }
        startingXValue = xValueToUse;

        String randomBlock = VERTICALDOMINOBLOCK.randomBlock().toString();
        // String randomBlock = VERTICALDOMINOBLOCK.STONE_BLOCK_8X1.toString();

        // extracts the height of the block (= value on the last char of the enum)
        int blockHeight = Integer.parseInt(Character.toString(randomBlock.charAt(randomBlock.length() - 3)));

        // sets the distance between the three blocks randomly, but not further away than (blockHeight - 1)
        // nextInt doesn't go until maxvalue but stops one before
        System.out.println("dominoStructure blockheight: " + blockHeight);
        int distance = ThreadLocalRandom.current().nextInt(1, blockHeight);
        endXValue = startingXValue + (distance * 2);

        ArrayList<JSONObject> dominoStructureArrayList = new ArrayList<JSONObject>();

        JSONObject part1DominoStructure = new JSONObject();
        part1DominoStructure.put("angle", 90);
        part1DominoStructure.put("id", randomBlock);
        part1DominoStructure.put("x", xValueToUse);
        part1DominoStructure.put("y", -2);

        JSONObject part2DominoStructure = new JSONObject();
        part2DominoStructure.put("angle", 90);
        part2DominoStructure.put("id", randomBlock);
        part2DominoStructure.put("x", xValueToUse + distance);
        part2DominoStructure.put("y", -2);

        JSONObject part3DominoStructure = new JSONObject();
        part3DominoStructure.put("angle", 90);
        part3DominoStructure.put("id", randomBlock);
        part3DominoStructure.put("x", xValueToUse + (2 * distance));
        part3DominoStructure.put("y", -2);

        dominoStructureArrayList.add(part1DominoStructure);
        dominoStructureArrayList.add(part2DominoStructure);
        dominoStructureArrayList.add(part3DominoStructure);

        return dominoStructureArrayList;
    }

    public int getStartingXValue() {
        return startingXValue;
    }
    public int getEndXValue() {
        return endXValue;
    }
}
