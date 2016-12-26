package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by erykp_000 on 2016-12-26.
 */

public class RecruitComponent implements Component, Pool.Poolable {
    public Entity enterEntity;

    public RecruitComponent init(Entity enterEntity) {
        this.enterEntity = enterEntity;
        return this;
    }

    @Override
    public void reset() {
        enterEntity = null;
    }
}
