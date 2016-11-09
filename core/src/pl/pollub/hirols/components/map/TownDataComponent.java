package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by erykp_000 on 2016-11-06.
 */

public class TownDataComponent implements Component, Pool.Poolable{

    public String name;

    public TownDataComponent init(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void reset() {
        name = null;
    }
}
