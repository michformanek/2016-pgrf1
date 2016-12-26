package rasterfillops.patterns;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public class CheckedPattern extends AbstractPattern {

    public CheckedPattern() {
        super(pattern);
    }

    private static boolean[][] pattern = {
            {true, true, false, false},
            {false, false, true, true}
    };

}


