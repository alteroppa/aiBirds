import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by felix on 10.04.18.
 */
public enum BLOCK {
    STONE_BLOCK_2X1, STONE_BLOCK_2X2, STONE_BLOCK_4X1, STONE_BLOCK_4X2, STONE_BLOCK_8X1, STONE_BLOCK_8X2,
    WOOD_BLOCK_2X1, WOOD_BLOCK_2X2, WOOD_BLOCK_4X1, WOOD_BLOCK_4X2, WOOD_BLOCK_8X1, WOOD_BLOCK_8X2,
    ICE_BLOCK_2X1, ICE_BLOCK_2X2, ICE_BLOCK_4X1, ICE_BLOCK_4X2, ICE_BLOCK_8X1, ICE_BLOCK_8X2,
    STONE_CIRCLE_1X1, STONE_CIRCLE_2X2, STONE_CIRCLE_4X4, STONE_CIRCLE_8X8;

    private static final List<BLOCK> ALLBLOCKS =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = ALLBLOCKS.size();
    private static final Random RANDOM = new Random();

    public static BLOCK randomBlock()  {
        //return ALLBLOCKS.get(RANDOM.nextInt(SIZE));
        return BLOCK.ICE_BLOCK_2X1;
    }


}
