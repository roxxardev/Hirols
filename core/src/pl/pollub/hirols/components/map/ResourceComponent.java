package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.ResourceType;

/**
 * Created by Marcin on 2016-02-15.
 */
public class ResourceComponent implements Component, Pool.Poolable{

    public ResourceType resourceType;
    public int amount;

    public ResourceComponent init(ResourceType resourceType, int amount) {
        this.resourceType = resourceType;
        this.amount = amount;
        return this;
    }

    @Override
    public void reset() {
        resourceType = null;
        amount = 0;
    }
}
