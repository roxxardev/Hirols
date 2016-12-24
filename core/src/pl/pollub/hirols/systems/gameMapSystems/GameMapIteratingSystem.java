package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.maps.GameMapComponent;

/**
 * Created by erykp_000 on 2016-12-24.
 */

public abstract class GameMapIteratingSystem extends IteratingSystem {
    protected ImmutableArray<Entity> gameMapDataArray;
    protected ComponentMapper<GameMapDataComponent> gameMapDataMapper = ComponentMapper.getFor(GameMapDataComponent.class);

    protected final Class<? extends GameMapComponent> gameMapClass;

    public GameMapIteratingSystem(Family family, int priority, Class<? extends GameMapComponent> gameMapClass) {
        super(family, priority);
        this.gameMapClass = gameMapClass;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        gameMapDataArray = engine.getEntitiesFor(Family.all(GameMapDataComponent.class, gameMapClass).get());
    }
}
