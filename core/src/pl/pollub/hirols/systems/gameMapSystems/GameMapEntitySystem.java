package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;

/**
 * Created by Eryk on 2016-05-02.
 */
public abstract class GameMapEntitySystem extends EntitySystem {

    protected ImmutableArray<Entity> gameMapData;
    protected ComponentMapper<GameMapDataComponent> gameMapDataMapper = ComponentMapper.getFor(GameMapDataComponent.class);

    protected final Class<? extends GameMapComponent> gameMapClass;

    public GameMapEntitySystem(int priority, Class<? extends GameMapComponent> gameMapClass) {
        super(priority);
        this.gameMapClass = gameMapClass;
    }

    @Override
    public void addedToEngine(Engine engine) {
        gameMapData = engine.getEntitiesFor(Family.all(GameMapDataComponent.class, gameMapClass).get());
    }

    @Override
    public abstract void update(float deltaTime);
}
