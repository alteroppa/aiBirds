/**
 * Created by felix on 09.04.18.
 */
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LevelCreator {

	private ArrayList<JSONObject> dominoLevelList = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> simpleLevelList = new ArrayList<JSONObject>();

	public void createManyDominoLevels (int numberOfLevels){
		for (int i = 0; i < numberOfLevels; i++){
			DominoStructure dominoStructure = new DominoStructure();
			BasicLevel basicLevel = new BasicLevel();
			ArrayList<JSONObject> dominoStructureList = dominoStructure.createDominoStructure();
			basicLevel.addToUsedXValues(dominoStructure.getStartingXValue(), (dominoStructure.getEndXValue() - dominoStructure.getStartingXValue())); // adds the xValues of the created dominoStructure to the list
			basicLevel.createSingleLevel(10, dominoStructureList);
			dominoLevelList.add(basicLevel.getLevel());
		}
	}

	public void createManySimpleLevels (int numberOfLevels){
		for (int i = 0; i < numberOfLevels; i++){
			ArrayList<JSONObject> emptyStructure = new ArrayList<JSONObject>();
			BasicLevel basicLevel = new BasicLevel();
			basicLevel.createSingleLevel(10, emptyStructure);
			simpleLevelList.add(basicLevel.getLevel());
		}
	}

	public void exportLevelsToFiles(ArrayList<JSONObject> levels) {
		System.out.println("Exporting " + levels.size() + " levels...");
		int levelNumber = 1;
		int iterationCounter = 1;
		for (JSONObject level : levels) {
			if (levelNumber > 21) {
				iterationCounter += 1;
				levelNumber = 1;
			}

			String levelName = "Level" + iterationCounter + "-" + levelNumber + ".json";
			try {
				FileWriter fileWriterGit = new FileWriter("/Users/felix/Documents/git/aiBirds/levelGenerator/generatedLevels/" + levelName);
				fileWriterGit.write(level.toJSONString());
				fileWriterGit.flush();

				// also save levels to wolter's cracked game
				FileWriter fileWriterCrackedGame = new FileWriter(
						"/Users/felix/Downloads/BamBird_2017-master-a3781e3a9491cf5e6cffc84c8ea48bb96ac13455/game/slingshot/cors/fowl/json/" + levelName);
				fileWriterCrackedGame.write(level.toJSONString());
				fileWriterCrackedGame.flush();

				levelNumber++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(levelName + ":\n" + level.toJSONString() + "\n");
		}
	}

	// getters
	public ArrayList<JSONObject> getDominoLevelList () {
		return dominoLevelList;
	}
	public ArrayList<JSONObject> getSimpleLevelList () {
		return simpleLevelList;
	}
}
