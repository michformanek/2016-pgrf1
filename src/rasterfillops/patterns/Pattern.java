package rasterfillops.patterns;

/**
 * Created by Michal Formanek on 25.12.16.
 */
public interface Pattern {

    /**
     * Returns true if pixel should change color, false otherwise
     *
     * @param c pixels x coordinate, measured from top left corner
     * @param r pixels y coordinate, measured from top left corner
     * @return true if pixel should be filled, false otherwise
     */
    boolean filled(int c, int r);
}
