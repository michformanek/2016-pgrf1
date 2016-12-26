package util;

import rasterfillops.FillerType;
import rasterfillops.seedfill.SeedFill;
import rasterfillops.seedfill.SeedFill4;
import rasterfillops.seedfill.SeedFill8;
import rasterization.*;

/**
 * Created by Michal Formanek on 26.12.16.
 */
public class FillerFactory<P> {

    /**
     * Returns new instance of SeedFill specified by given type.
     *
     * @param type Type of returned SeedFill
     * @return SeedFill of given type, SeedFill4 if implementation does not exist
     */
    public SeedFill<P> getFiller(FillerType type) {
        if (type == FillerType.SEEDFILL4)
            return new SeedFill4<>();
        else if (type == FillerType.SEEDFILL8)
            return new SeedFill8<>();
        else
            return new SeedFill4<>();
    }
}
