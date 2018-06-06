/**
 * Created by felix on 09.04.18.
 */
public class main {
    public static void main(String[] args){
        // create max. 63 levels for level-changer only recognizes 21 levels at once, then you have to manually switch
        //createSimpleLevels(63);
        createDominoLevels(21);
    }

    public static void createDominoLevels(int numberOfLevels){
        LevelCreator levelCreator = new LevelCreator();
        levelCreator.createManyDominoLevels(numberOfLevels);
        levelCreator.exportLevelsToFiles(levelCreator.getDominoLevelList(), "dominoLevels");
    }

    public static void createSimpleLevels(int numberOfLevels) {
        LevelCreator levelCreator = new LevelCreator();
        levelCreator.createManySimpleLevels(numberOfLevels);
        levelCreator.exportLevelsToFiles(levelCreator.getSimpleLevelList(), "simpleLevels");
    }


}
