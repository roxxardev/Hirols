package pl.pollub.hirols.managers.enums;

/**
 * Created by erykp_000 on 2016-07-29.
 */
public enum Direction {
    N,
    S,
    W,
    E,
    NE,
    NW,
    SE,
    SW;

    public static Direction getRandomDirection() {
        return values()[(int) (Math.random() * values().length)];
    }
}
