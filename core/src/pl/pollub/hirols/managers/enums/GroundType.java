package pl.pollub.hirols.managers.enums;

/**
 * Created by erykp_000 on 2016-12-26.
 */

public enum GroundType {
    GRASS(0), SNOW(10), SAND(20), MOOD(35);

    public final int penalty;

    GroundType(int penalty) {
        this.penalty = penalty;
    }
}
