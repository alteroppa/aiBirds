import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by felix on 10.04.18.
 */
public enum VERTICALBLOCK {
    STONE_BLOCK_1X2, STONE_BLOCK_1X4, STONE_BLOCK_1X8,
    WOOD_BLOCK_1X2, WOOD_BLOCK_1X4, WOOD_BLOCK_1X8,
    ICE_BLOCK_1X2, ICE_BLOCK_1X4, ICE_BLOCK_1X8;

    private static final List<VERTICALBLOCK> ALLBLOCKS =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = ALLBLOCKS.size();
    private static final Random RANDOM = new Random();

    public static VERTICALBLOCK randomBlock()  {
        return ALLBLOCKS.get(RANDOM.nextInt(SIZE));
    }


}
