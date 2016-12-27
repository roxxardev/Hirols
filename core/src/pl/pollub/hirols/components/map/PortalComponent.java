package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by erykp_000 on 2016-12-23.
 */

public class PortalComponent implements Component, Pool.Poolable {

    public Entity destinationMapEntity;

    public PortalComponent init(Entity destinationMapEntity) {
        this.destinationMapEntity = destinationMapEntity;
        return this;
    }

    @Override
    public void reset() {
        destinationMapEntity = null;
    }
}
