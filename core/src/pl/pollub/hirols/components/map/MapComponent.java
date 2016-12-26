package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.GroundType;

/**
 * Created by Eryk on 2016-02-20.
 */
public class MapComponent implements Component, Pool.Poolable {
    public boolean walkable;
    public GroundType groundType;

    public MapComponent init(boolean walkable, GroundType groundType) {
        this.walkable = walkable;
        this.groundType = groundType;
        return this;
    }

    @Override
    public void reset() {
        walkable = false;
    }
}
