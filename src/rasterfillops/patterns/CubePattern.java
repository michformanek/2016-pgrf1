package rasterfillops.patterns;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public class CubePattern extends AbstractPattern {

    public CubePattern() {
        super(pattern);
    }

    private static boolean[][] pattern = {
            {true, true, true, true},
            {true, false, false, true},
            {true, false, false, true},
            {true, true, true, true},
    };
}
