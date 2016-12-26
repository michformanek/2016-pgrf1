package rasterfillops.patterns;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public class VerticalStripePattern extends AbstractPattern {

    public VerticalStripePattern() {
        super(pattern);
    }

    private static boolean[][] pattern = {
            {true, true, false, false}
    };
}
