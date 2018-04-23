/**
 * Created by felix on 09.04.18.
 */
public class main {
    public static void main(String[] args){
        LevelCreator levelCreator = new LevelCreator();
        levelCreator.createManyDominoLevels(10);
        levelCreator.exportLevelsToFiles(levelCreator.getDominoLevelList());
    }


}
