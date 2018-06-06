import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by felix on 10.04.18.
 */
public enum VERTICALDOMINOBLOCK {
    //STONE_BLOCK_4X1,
    STONE_BLOCK_8X1,
    //STONE_BLOCK_10X1,
    //WOOD_BLOCK_4X1,
    WOOD_BLOCK_8X1,
    //WOOD_BLOCK_8X2,
  //WOOD_BLOCK_10X2,
    //ICE_BLOCK_4X1,
    ICE_BLOCK_8X1;
    //ICE_BLOCK_10X1;


    private static final List<VERTICALDOMINOBLOCK> ALLBLOCKS =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = ALLBLOCKS.size();
    private static final Random RANDOM = new Random();

    public static VERTICALDOMINOBLOCK randomBlock()  {
        return ALLBLOCKS.get(RANDOM.nextInt(SIZE));
    }


}
