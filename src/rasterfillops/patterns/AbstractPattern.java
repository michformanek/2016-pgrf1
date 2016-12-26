package rasterfillops.patterns;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public abstract class AbstractPattern implements Pattern {

    private final boolean[][] PATTERN;

    AbstractPattern(boolean[][] pattern) {
        this.PATTERN = pattern;
    }

    AbstractPattern() {
        this.PATTERN = new boolean[][]{{true}};
    }

    @Override
    public boolean filled(final int c, final int r) {
        final int x = c % PATTERN.length;
        final int y = r % PATTERN[x].length;
        return PATTERN[x][y];
    }

}
