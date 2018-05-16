import org.json.simple.JSONObject;
import sun.jvm.hotspot.runtime.Thread;

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
        startingXValue = ThreadLocalRandom.current().nextInt(35, 100 + 1); // 100 is max for the birds still to be able to reach the structure

        String randomBlock = VERTICALDOMINOBLOCK.randomBlock().toString();
        // String randomBlock = VERTICALDOMINOBLOCK.STONE_BLOCK_8X1.toString();

        // extracts the height of the block (= value on the last char of the enum)
        int blockHeight = 0;
        if (Character.toString(randomBlock.charAt(randomBlock.length() - 5)).equals("_")) {
            String blockHeightSubstring = randomBlock.substring(randomBlock.length() - 4, randomBlock.length() - 2);
            System.out.println(blockHeightSubstring);
            blockHeight = Integer.parseInt(blockHeightSubstring);
        } else {
            blockHeight = Integer.parseInt(Character.toString(randomBlock.charAt(randomBlock.length() - 3)));
        }

        // sets the distance between the three blocks randomly, but not further away than (blockHeight - 1)
        // nextInt doesn't go until maxvalue but stops one before
        System.out.println("dominoStructure blockheight: " + blockHeight);
        int distance = ThreadLocalRandom.current().nextInt(4, blockHeight); // distance should be at least 4
        endXValue = startingXValue + (distance * 2);

        ArrayList<JSONObject> dominoStructureArrayList = new ArrayList<JSONObject>();

        JSONObject part1DominoStructure = new JSONObject();
        part1DominoStructure.put("angle", 90);
        part1DominoStructure.put("id", randomBlock);
        part1DominoStructure.put("x", startingXValue);
        part1DominoStructure.put("y", -4);

        // if (distance < 4) { dominoStructureArrayList.add(createConcreteBlock(xValueToUse)); }



        JSONObject part2DominoStructure = new JSONObject();
        part2DominoStructure.put("angle", 90);
        part2DominoStructure.put("id", randomBlock);
        part2DominoStructure.put("x", startingXValue + distance);
        part2DominoStructure.put("y", -4);

        JSONObject part3DominoStructure = new JSONObject();
        part3DominoStructure.put("angle", 90);
        part3DominoStructure.put("id", randomBlock);
        part3DominoStructure.put("x", startingXValue + (2 * distance));
        part3DominoStructure.put("y", -4);

        boolean addTopBar = ThreadLocalRandom.current().nextBoolean();

        if (addTopBar && (distance < 30)) { // if true, add top bar above structure
            JSONObject topBar = new JSONObject();
            topBar.put("angle", 0);
            topBar.put("id", randomBlock);
            topBar.put("x", startingXValue + distance);
            topBar.put("y", -12);
            dominoStructureArrayList.add(topBar);
        }

        dominoStructureArrayList.add(part1DominoStructure);
        dominoStructureArrayList.add(part2DominoStructure);
        dominoStructureArrayList.add(part3DominoStructure);

        if (addTopBar){
            System.out.println("topbar added. Size of dominostructure: " + dominoStructureArrayList.size());
        }

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
