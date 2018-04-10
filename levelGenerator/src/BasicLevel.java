/**
 * Created by felix on 09.04.18.
 */
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BasicLevel {
	public void createManyLevels (int numberOfLevels){
		for (int i = 0; i < numberOfLevels; i++){
			createLevel("Level1" + "-" + (i+1), 10);
		}
	}

    public void createLevel(String levelName, int blocks){

		JSONObject wholeLevel = new JSONObject();

		//create camera array
    	JSONArray cameraArray = new JSONArray();

			JSONObject camera1 = new JSONObject();

			camera1.put("bottom", 14.338);
			camera1.put("id", "Slingshot");
			camera1.put("left", -39.913);
			camera1.put("right", 33.515);
			camera1.put("top", -40.733);
			camera1.put("x", 29.894);
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

		JSONObject counts = new JSONObject();
			counts.put("birds", 4);
			counts.put("blocks", blocks);

		wholeLevel.put("counts", counts);
		wholeLevel.put("id", "pack1/Level6.lua");
		wholeLevel.put("scoreEagle", 65900);
		wholeLevel.put("scoreGold", 64000);
		wholeLevel.put("scoreSilver", 51000);
		wholeLevel.put("theme", "BACKGROUND_BLUE_GRASS");

		JSONObject world = new JSONObject();

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


		// add randomized blocks
		for (int i = 0; i < blocks; i++){
			world.put("block_"+(i+1), createRandomJSONBlock());
		}

		wholeLevel.put("world", world);

		// writing the JSONObject into a file(info.json)
		try {
        FileWriter fileWriter = new FileWriter("generatedLevels/" + levelName + ".json");
        fileWriter.write(wholeLevel.toJSONString());
        fileWriter.flush();
    } catch (Exception e) {
        e.printStackTrace();
    }
		System.out.println(wholeLevel);
	}

	public JSONObject createRandomJSONBlock () {
		JSONObject jsonBlock = new JSONObject();

		jsonBlock.put("angle", getRandomAngle());
		jsonBlock.put("id", BLOCK.randomBlock().toString());
		jsonBlock.put("x", getRandomXInt());
		jsonBlock.put("y", getRandomYInt());

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
		int min = 5;
		int max = 100;

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return (ThreadLocalRandom.current().nextInt(min, max + 1));
	}

	public int getRandomYInt () {
		int min = 5; // -25
		int max = 25;

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		return (ThreadLocalRandom.current().nextInt(min, max + 1));
	}
}
