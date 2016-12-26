package rasterfillops.patterns;

/**
 * Created by mformanek on 25.12.16.
 */
public class HorizontalStripePattern extends AbstractPattern {

    public HorizontalStripePattern() {
        super(pattern);
    }

    private static boolean[][] pattern = {
            {true}, {true},
            {false}, {false}
    };
}