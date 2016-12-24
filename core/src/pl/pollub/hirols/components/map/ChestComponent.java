package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.ResourceType;

/**
 * Created by Eryk on 2016-02-27.
 */
public class ChestComponent implements Component, Pool.Poolable {
    public ResourceType resourceType;
    public int amountResource;
    public int experience;

    public ChestComponent init(ResourceType resourceType, int amountResource, int experience) {
        this.resourceType = resourceType;
        this.amountResource = amountResource;
        this.experience = experience;
        return this;
    }

    @Override
    public void reset() {
        resourceType = null;
        amountResource = 0;
        experience = 0;
    }
}
