import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum CONCRETEBLOCK {
	//STONE_BLOCK_4X1,
	STONE_BLOCK_8X1,
	//WOOD_BLOCK_4X1,
	WOOD_BLOCK_8X1,
	//ICE_BLOCK_4X1,
	ICE_BLOCK_8X1;

	private static final List<CONCRETEBLOCK> ALLBLOCKS =
			Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = ALLBLOCKS.size();
	private static final Random RANDOM = new Random();

	public static CONCRETEBLOCK randomBlock()  {
		return ALLBLOCKS.get(RANDOM.nextInt(SIZE));
	}


}
