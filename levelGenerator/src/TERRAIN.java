import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by felix on 10.04.18.
 */
public enum TERRAIN {
    TERRAIN_TEXTURED_HILLS_10X10,
    TERRAIN_TEXTURED_HILLS_10X2,
    TERRAIN_TEXTURED_HILLS_1X1,
    TERRAIN_TEXTURED_HILLS_32X2,
    TERRAIN_TEXTURED_HILLS_5X2,
    TERRAIN_TEXTURED_HILLS_5X5;

    private static final List<TERRAIN> ALLBLOCKS =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = ALLBLOCKS.size();
    private static final Random RANDOM = new Random();

    public static TERRAIN randomBlock()  {
        return ALLBLOCKS.get(RANDOM.nextInt(SIZE));
    }


}
