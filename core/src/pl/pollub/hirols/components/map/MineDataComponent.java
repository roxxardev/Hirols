package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.ResourceType;

/**
 * Created by erykp_000 on 2016-10-29.
 */

public class MineDataComponent implements Component, Pool.Poolable {
    public ResourceType type;
    public int amountPerWeek = 0;

    public MineDataComponent init(ResourceType type, int amountPerWeek) {
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
