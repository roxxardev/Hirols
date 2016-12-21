package pl.pollub.hirols.managers;

/**
 * Created by erykp_000 on 2016-12-21.
 */

public enum RenderPriority {
    FIRST(0),
    LOW(100),
    MEDIUM(200),
    HIGH(300),
    LAST(400);

    public final int priority;

    RenderPriority(int priority) {
        this.priority = priority;
    }
}
