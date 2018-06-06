import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class FunnelStructure {
	private int startingXValue = 0;
	private int endXValue = 0;

	public ArrayList<JSONObject> createFunnelStructure (int structureVariant) {

		// makes sure the xValueToUse for the blocks is far enough away from the sling
		startingXValue = ThreadLocalRandom.current().nextInt(35, 99 + 1);

		//String randomBlock = "TERRAIN_TEXTURED_HILLS_10X2";

		ArrayList<JSONObject> concreteStructureArrayList = new ArrayList<JSONObject>();


		if (structureVariant == 1) {
			concreteStructureArrayList = createFirstStructureVariant();
		} else if (structureVariant == 2){
			concreteStructureArrayList = createSecondStructureVariant();
		} else {
			concreteStructureArrayList = createThirdStructureVariant();
		}

		return concreteStructureArrayList;
	}

	public ArrayList<JSONObject> createFirstStructureVariant(){
		ArrayList<JSONObject> structureList = new ArrayList<JSONObject>();
		int xDistance = 7;
		String longConcreteBlock = "TERRAIN_TEXTURED_HILLS_10X2";

		JSONObject baseBlockLeft = new JSONObject();
		baseBlockLeft.put("angle", 90);
		baseBlockLeft.put("id", longConcreteBlock);
		baseBlockLeft.put("x", startingXValue + 1);
		baseBlockLeft.put("y", -1);

		JSONObject middleBlockLeft = new JSONObject();
		middleBlockLeft.put("angle", 90);
		middleBlockLeft.put("id", longConcreteBlock);
		middleBlockLeft.put("x", startingXValue + 1);
		middleBlockLeft.put("y", -10);

		JSONObject baseBlockRight = new JSONObject();
		baseBlockRight.put("angle", 90);
		baseBlockRight.put("id", longConcreteBlock);
		baseBlockRight.put("x", startingXValue + xDistance);
		baseBlockRight.put("y", -1);

		JSONObject middleBlockRight = new JSONObject();
		middleBlockRight.put("angle", 90);
		middleBlockRight.put("id", longConcreteBlock);
		middleBlockRight.put("x", startingXValue + xDistance);
		middleBlockRight.put("y", -10);

		JSONObject topBlockDiagonalLeft = new JSONObject();
		topBlockDiagonalLeft.put("angle", 45);
		topBlockDiagonalLeft.put("id", longConcreteBlock);
		topBlockDiagonalLeft.put("x", startingXValue - 2);
		topBlockDiagonalLeft.put("y", -18);

		JSONObject topBlockDiagonalRight = new JSONObject();
		topBlockDiagonalRight.put("angle", 45);
		topBlockDiagonalRight.put("id", longConcreteBlock);
		topBlockDiagonalRight.put("x", startingXValue + xDistance - 2);
		topBlockDiagonalRight.put("y", -18);

		JSONObject pig = new JSONObject();
		pig.put("angle", 90);
		pig.put("id", "PIG_BASIC_SMALL");
		pig.put("x", startingXValue + 4);
		pig.put("y", -1);

		structureList.add(baseBlockLeft);
		structureList.add(middleBlockLeft);
		structureList.add(baseBlockRight);
		structureList.add(middleBlockRight);
		structureList.add(topBlockDiagonalLeft);
		structureList.add(topBlockDiagonalRight);
		structureList.add(pig);

		this.endXValue = startingXValue + xDistance;

		return structureList;
	}

	public ArrayList<JSONObject> createSecondStructureVariant(){
		ArrayList<JSONObject> structureList = new ArrayList<JSONObject>();
		//int xDistance = 7;
		String longConcreteBlock = "TERRAIN_TEXTURED_HILLS_10X2";

		/*JSONObject topDiagonalBlock = new JSONObject();
		topDiagonalBlock.put("angle", 45);
		topDiagonalBlock.put("id", longConcreteBlock);
		topDiagonalBlock.put("x", startingXValue + 1);
		topDiagonalBlock.put("y", -8);

		JSONObject bottomDiagonalBlock = new JSONObject();
		bottomDiagonalBlock.put("angle", 45);
		bottomDiagonalBlock.put("id", longConcreteBlock);
		bottomDiagonalBlock.put("x", startingXValue + 1);
		bottomDiagonalBlock.put("y", -1); */

		JSONObject topFirstBlock = new JSONObject();
		topFirstBlock.put("angle", 0);
		topFirstBlock.put("id", longConcreteBlock);
		topFirstBlock.put("x", startingXValue + 15);
		topFirstBlock.put("y", -5);

		JSONObject topSecondBlock = new JSONObject();
		topSecondBlock.put("angle", 0);
		topSecondBlock.put("id", longConcreteBlock);
		topSecondBlock.put("x", startingXValue + 24);
		topSecondBlock.put("y", -5);


		JSONObject pig = new JSONObject();
		pig.put("angle", 90);
		pig.put("id", "PIG_BASIC_MEDIUM");
		pig.put("x", startingXValue + 26);
		pig.put("y", -1);

		//structureList.add(topDiagonalBlock);
		//structureList.add(bottomDiagonalBlock);
		structureList.add(topFirstBlock);
		structureList.add(topSecondBlock);
		structureList.add(pig);

		this.endXValue = startingXValue + 28;

		return structureList;
	}
	public ArrayList<JSONObject> createThirdStructureVariant(){
		return new ArrayList<JSONObject>();
	}


	public int getStartingXValue() {
		return startingXValue;
	}
	public int getEndXValue() {
		return endXValue;
	}
}
