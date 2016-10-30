package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.Resource;

/**
 * Created by erykp_000 on 2016-10-29.
 */

public class MineDataComponent implements Component, Pool.Poolable {
    public Resource type;
    public int amountPerWeek = 0;

    public MineDataComponent init(Resource type, int amountPerWeek) {
        this.amountPerWeek = amountPerWeek;
        this.type = type;
        return this;
    }

    @Override
    public void reset() {
        type = null;
        amountPerWeek = 0;
    }
}
