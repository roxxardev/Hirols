package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.PathComponent;
import pl.pollub.hirols.components.map.SelectedHeroComponent;
import pl.pollub.hirols.components.physics.PositionComponent;

/**
 * Created by Eryk on 2016-05-11.
 */
public class PathEntityRemovalSystem extends GameMapEntitySystem {

    private ImmutableArray<Entity> selectedHeroes;
    private ImmutableArray<Entity> pathEntities;

    private ComponentMapper<PathComponent> pathMap = ComponentMapper.getFor(PathComponent.class);
    private ComponentMapper<HeroDataComponent> heroDataMap = ComponentMapper.getFor(HeroDataComponent.class);
    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

    public PathEntityRemovalSystem(int priority, Class<? extends GameMapComponent> gameMapClass) {
        super(priority, gameMapClass);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        pathEntities = engine.getEntitiesFor(Family.all(PathComponent.class, gameMapClass).get());
        selectedHeroes = engine.getEntitiesFor(Family.all(HeroDataComponent.class, SelectedHeroComponent.class, gameMapClass).get());
    }

    @Override
    public void update(float deltaTime) {
        if (gameMapData.size() < 1) return;
        for (Entity hero : selectedHeroes) {
            HeroDataComponent heroData = heroDataMap.get(hero);
            PositionComponent heroPosition = posMap.get(hero);
            if (!heroData.pathNodesPosition.isEmpty()) {
                if (heroPosition.x == heroData.pathNodesPosition.get(0).x && heroPosition.y == heroData.pathNodesPosition.get(0).y) {
                    for (Entity pathEntity : pathEntities) {
                        PathComponent pathData = pathMap.get(pathEntity);
                        if (pathData.playerID == heroData.id) {
                            PositionComponent positionPathEntity = posMap.get(pathEntity);
                            if (positionPathEntity.x == heroData.pathNodesPosition.get(0).x && positionPathEntity.y == heroData.pathNodesPosition.get(0).y) {
                                getEngine().removeEntity(pathEntity);
                                break;
                            }
                        }
                    }
                    heroData.movementPoints -= heroData.pathNodesPosition.get(0).z;
                    heroData.pathNodesPosition.remove(0);
                }
            }
        }
    }
}
