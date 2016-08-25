package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
            if (heroData.heroPath.hasWalkNodes()) {
                Vector3 node = heroData.heroPath.getWalkNodesPosition().get(0);
                if (heroPosition.x == node.x && heroPosition.y == node.y) {
                    for (Entity pathEntity : pathEntities) {
                        PathComponent pathData = pathMap.get(pathEntity);
                        if (pathData.playerID == heroData.id) {
                            PositionComponent positionPathEntity = posMap.get(pathEntity);
                            if (positionPathEntity.x == node.x && positionPathEntity.y == node.y) {
                                getEngine().removeEntity(pathEntity);
                                break;
                            }
                        }
                    }
                    heroData.movementPoints -= node.z;
                    heroData.heroPath.getWalkNodesPosition().remove(0);
                }
            }
        }
    }
}
