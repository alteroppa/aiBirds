/**
 * Created by felix on 09.04.18.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;


import org.json.simple.JSONObject;

public class LevelCreator {

	//private ArrayList<JSONObject> dominoLevelList = new ArrayList<JSONObject>();
	//private ArrayList<JSONObject> simpleLevelList = new ArrayList<JSONObject>();
	//private ArrayList<JSONObject> houseLevelList = new ArrayList<JSONObject>();
	//private ArrayList<JSONObject> funnelLevelList = new ArrayList<>();


	public ArrayList<JSONObject> createDominoLevels(int numberOfLevels){
		ArrayList<JSONObject> dominoLevelList = new ArrayList<JSONObject>();
		for (int i = 0; i < numberOfLevels; i++){
			DominoStructure dominoStructure = new DominoStructure();
			ArrayList<JSONObject> dominoStructureList = dominoStructure.createDominoStructure();
			BasicLevel basicLevel = new BasicLevel(dominoStructureList);

			// if domino structure is to be created, only one pig should be added.
			// If more, pigs will be randomly spread, and not set to the beginning of the level
			basicLevel.additionalStructureIsDominoStructure = true;

			// adds the xValues of the created dominoStructure to the list
			basicLevel.addToUsedXValues(dominoStructure.getStartingXValue(), (dominoStructure.getEndXValue() - dominoStructure.getStartingXValue()));
			basicLevel.createSingleLevel(10, 1, 1, false);
			dominoLevelList.add(basicLevel.getLevel());
			System.out.println("domino level "+ i + " created!");
		}
		return dominoLevelList;
	}
	public ArrayList<JSONObject> createFunnelLevels(int numberOfLevels){
		ArrayList<JSONObject> funnelLevelList = new ArrayList<JSONObject>();
		for (int i = 0; i < numberOfLevels; i++){
			FunnelStructure funnelStructure = new FunnelStructure();
			ArrayList<JSONObject> funnelStructureList = funnelStructure.createFunnelStructure(2); // 1 is vertical, 2 is horizontal structure
			BasicLevel basicLevel = new BasicLevel(funnelStructureList);
			basicLevel.addToUsedXValues(funnelStructure.getStartingXValue(), (funnelStructure.getEndXValue() - funnelStructure.getStartingXValue())); // adds the xValues of the created dominoStructure to the list
			basicLevel.createSingleLevel(10, 0, 0, false); //addPig not necessary, since pig is already inside funnel structure
			funnelLevelList.add(basicLevel.getLevel());
			System.out.println("funnel level "+ i + " created!");
		}
		return funnelLevelList;
	}
	public ArrayList<JSONObject> createHouseLevels (int numberOfLevels){
		ArrayList<JSONObject> houseLevelList = new ArrayList<JSONObject>();
		for (int i = 0; i < numberOfLevels; i++){
			HouseStructure houseStructure=new HouseStructure();
			ArrayList<JSONObject> houseStructureList = houseStructure.createHouseStructure();
			BasicLevel basicLevel = new BasicLevel(houseStructureList);
			basicLevel.addToUsedXValues(houseStructure.getStartingXValue(), (houseStructure.getEndXValue() - houseStructure.getStartingXValue()));
			basicLevel.createSingleLevel(4, 2, 1, true);
			houseLevelList.add(basicLevel.getLevel());
		}
		return houseLevelList;
	}
	public ArrayList<JSONObject> createSimpleLevels(int numberOfLevels){
		ArrayList<JSONObject> simpleLevelList = new ArrayList<JSONObject>();
		for (int i = 0; i < numberOfLevels; i++){
			ArrayList<JSONObject> emptyStructure = new ArrayList<JSONObject>();
			BasicLevel basicLevel = new BasicLevel(emptyStructure);
			basicLevel.createSingleLevel(10, 2, 1, true);
			simpleLevelList.add(basicLevel.getLevel());
		}
		return simpleLevelList;
	}

	public void exportLevelsToFiles(ArrayList<JSONObject> levels, String folderName) {

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
				File file=new File("generatedLevels/" + folderName + "/" + levelName);
				FileWriter fileWriterGit = new FileWriter(file.getAbsolutePath());
				fileWriterGit.write(level.toJSONString());
				fileWriterGit.flush();

				/*// also save levels to wolter's cracked game
				FileWriter fileWriterCrackedGame = new FileWriter(
						(new File("game/slingshot/cors/fowl/json/" + levelName)).getAbsolutePath());
				fileWriterCrackedGame.write(level.toJSONString());
				fileWriterCrackedGame.flush();*/

				levelNumber++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(levelName + ":\n" + level.toJSONString() + "\n");
		}
	}

	public void exportLevelsToFolders(ArrayList<JSONObject> levels, String folderName) {
        System.out.println("Exporting " + levels.size() + " levels...");
        int levelNumber = 1;
        int iterationCounter = 1;

        try {
            FileUtils.cleanDirectory(new File("generatedLevels/" + folderName));
            new File("generatedLevels/" + folderName + "/levels" + iterationCounter).mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (JSONObject level : levels) {
            if (levelNumber > 21) {
                iterationCounter += 1;
                levelNumber = 1;
                new File("generatedLevels/" + folderName + "/levels" + iterationCounter).mkdir();
            }
            try {
                String levelName = "Level1-" + levelNumber + ".json";
                File file = new File("generatedLevels/" + folderName + "/levels" + iterationCounter + "/" + levelName);
                FileWriter fileWriterGit = new FileWriter(file.getAbsolutePath());
                fileWriterGit.write(level.toJSONString());
                fileWriterGit.flush();

            /*// also save levels to wolter's cracked game
            FileWriter fileWriterCrackedGame = new FileWriter(
                    (new File("game/slingshot/cors/fowl/json/" + levelName)).getAbsolutePath());
            fileWriterCrackedGame.write(level.toJSONString());
            fileWriterCrackedGame.flush();*/

                levelNumber++;
                System.out.println(levelName + ":\n" + level.toJSONString() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


	// getters
	//public ArrayList<JSONObject> getDominoLevelList () {return dominoLevelList;}
	//public ArrayList<JSONObject> getSimpleLevelList () { return simpleLevelList; }
	//public ArrayList<JSONObject> getHouseLevelList (){return houseLevelList;}
	//public ArrayList<JSONObject> getFunnelLevelList () { return funnelLevelList; }


}
