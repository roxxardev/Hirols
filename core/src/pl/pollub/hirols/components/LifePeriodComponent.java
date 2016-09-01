package pl.pollub.hirols.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by erykp_000 on 2016-03-08.
 */
public class LifePeriodComponent implements Component, Pool.Poolable {
    public long createTime;
    public long deathTime;
    public long duration;

    public LifePeriodComponent init(long milliseconds) {
        this.createTime = System.currentTimeMillis();
        this.duration = milliseconds;
        this.deathTime = createTime + milliseconds;
        return this;
    }

    @Override
    public void reset() {
        createTime = 0L;
        deathTime = 0L;
        duration = 0L;
    }
}
