package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2016-02-27.
 */
public class MineComponent implements Component, Pool.Poolable {

    public Entity enterEntity;

    public MineComponent init(Entity enterEntity) {
        this.enterEntity = enterEntity;
        return this;
    }

    @Override
    public void reset() {
        enterEntity = null;
    }
}
