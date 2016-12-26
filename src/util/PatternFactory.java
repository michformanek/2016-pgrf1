package util;

import rasterfillops.patterns.*;
import rasterization.*;

/**
 * Created by mformanek on 25.12.16.
 */
public class PatternFactory {

    /**
     * Returns new instance of Pattern specified by given type.
     *
     * @param type Type of returned Pattern
     * @return Pattern of given type, Solid if pattern does not exist
     */
    public Pattern getPattern(PatternType type) {
        if (type == PatternType.CHECKED)
            return new CheckedPattern();
        else if (type == PatternType.CUBE)
            return new CubePattern();
        else if (type == PatternType.HORIZONTAL)
            return new HorizontalStripePattern();
        else if (type == PatternType.VERTICAL)
            return new VerticalStripePattern();
        else
            return new SolidPattern();
    }
}
