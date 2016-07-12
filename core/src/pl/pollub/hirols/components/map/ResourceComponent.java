package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;

import pl.pollub.hirols.managers.enums.Resource;

/**
 * Created by Marcin on 2016-02-15.
 */
public class ResourceComponent implements Component{

    public Resource resourceType;
    public int amount;

    public ResourceComponent(Resource resourceType, int amount) {
        this.resourceType = resourceType;
        this.amount = amount;
    }
}
