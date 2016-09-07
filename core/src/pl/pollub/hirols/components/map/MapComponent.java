package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2016-02-20.
 */
public class MapComponent implements Component, Pool.Poolable {
    public boolean walkable;

    public MapComponent init(boolean walkable) {
        this.walkable = walkable;
        return this;
    }

    @Override
    public void reset() {
        walkable = false;
    }
}
