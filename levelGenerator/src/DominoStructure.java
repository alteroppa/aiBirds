import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by felix on 10.04.18.
 */
public class DominoStructure {
    public static ArrayList<JSONObject> createDominoStructure () {

        String randomBlock = BLOCK.randomBlock().toString();

        ArrayList<JSONObject> dominoStructure = new ArrayList<JSONObject>();

        JSONObject part1DominoStructure = new JSONObject();
        part1DominoStructure.put("angle", 0);
        part1DominoStructure.put("id", randomBlock);
        part1DominoStructure.put("x", 10);
        part1DominoStructure.put("y", -1);

        JSONObject part2DominoStructure = new JSONObject();
        part2DominoStructure.put("angle", 0);
        part2DominoStructure.put("id", randomBlock);
        part2DominoStructure.put("x", 12);
        part2DominoStructure.put("y", -1);

        JSONObject part3DominoStructure = new JSONObject();
        part3DominoStructure.put("angle", 0);
        part3DominoStructure.put("id", randomBlock);
        part3DominoStructure.put("x", 14);
        part3DominoStructure.put("y", -1);

        dominoStructure.add(part1DominoStructure);
        dominoStructure.add(part2DominoStructure);
        dominoStructure.add(part3DominoStructure);

        return dominoStructure;
    }
}
