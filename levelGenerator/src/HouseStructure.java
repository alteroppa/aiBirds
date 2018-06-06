import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by lara on 30.04.18.
 */
public class HouseStructure {
	private int startingXValue = 0;
	private int endXValue = 0;

	public ArrayList<JSONObject> createHouseStructure () {
		startingXValue = ThreadLocalRandom.current().nextInt(50, 100);

		String randomBlock = VERTICALDOMINOBLOCK.randomBlock().toString();
		String randomPig = PIG.PIG_BASIC_SMALL.toString();

		// extracts the height of the block (= value on the last char of the enum)
		int blockHeight = Integer.parseInt(Character.toString(randomBlock.charAt(randomBlock.length() - 3)));
		int blockWidth = Integer.parseInt(Character.toString(randomBlock.charAt(randomBlock.length() - 1)));

		// sets the distance between the three blocks randomly, but not further away than (blockHeight - 1)
		// nextInt doesn't go until maxvalue but stops one before
		System.out.println("block blockheight: " + blockHeight);
		//TODO change as this is for domino
		int distance = ThreadLocalRandom.current().nextInt(3, blockHeight);
		System.out.println(distance);
		endXValue = startingXValue + (distance);

		ArrayList<JSONObject> blockStructureArrayList = new ArrayList<JSONObject>();

		JSONObject part1BlockStructure = new JSONObject();
		part1BlockStructure.put("angle", 91);
		part1BlockStructure.put("id", randomBlock);
		part1BlockStructure.put("x", startingXValue);
		part1BlockStructure.put("y", -2);

		JSONObject part2BlockStructure = new JSONObject();
		part2BlockStructure.put("angle", 91);
		part2BlockStructure.put("id", randomBlock);
		part2BlockStructure.put("x", endXValue);
		part2BlockStructure.put("y", -2);

		JSONObject part3BlockStructure = new JSONObject();
		part3BlockStructure.put("angle", 180);
		part3BlockStructure.put("id", randomBlock);
		part3BlockStructure.put("x", startingXValue+(int)(((double)distance)/2));
		part3BlockStructure.put("y", -1.5-distance);

		JSONObject pigInBlock = new JSONObject();
		pigInBlock.put("angle",0);
		pigInBlock.put("id",randomPig);
		pigInBlock.put("x", startingXValue+(int)(((double)distance)/2));
		pigInBlock.put("y",-1);


		blockStructureArrayList.add(part1BlockStructure);
		blockStructureArrayList.add(part2BlockStructure);
		blockStructureArrayList.add(part3BlockStructure);

		blockStructureArrayList.add(pigInBlock);

		return blockStructureArrayList;
	}

	public int getStartingXValue() {
		return startingXValue;
	}
	public int getEndXValue() {
		return endXValue;
	}
}


