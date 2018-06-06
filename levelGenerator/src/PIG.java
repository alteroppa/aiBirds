import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/**
 * Created by lara on 30.04.18.
 */

public enum PIG {
	PIG_BASIC_BIG,
	PIG_BASIC_MEDIUM,
	PIG_BASIC_SMALL,
	PIG_HELMET,
	PIG_KING,
	PIG_MUSTACHE;
	private static final List<PIG> ALLPIGS =
			Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = ALLPIGS.size();
	private static final Random RANDOM = new Random();

	public static PIG randomPig()  {
		return ALLPIGS.get(RANDOM.nextInt(SIZE));
	}

}
