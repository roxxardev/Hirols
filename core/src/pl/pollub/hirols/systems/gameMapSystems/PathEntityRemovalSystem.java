package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.LifePeriodComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TransparencyComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.SelectedComponent;
import pl.pollub.hirols.components.physics.PositionComponent;

/**
 * Created by Eryk on 2016-05-11.
 */
public class PathEntityRemovalSystem extends GameMapEntitySystem {

    private final Hirols game;

    private ComponentMapper<HeroDataComponent> heroDataMap = ComponentMapper.getFor(HeroDataComponent.class);
    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

    public PathEntityRemovalSystem(int priority, Class<? extends GameMapComponent> gameMapClass, Hirols game) {
        super(priority, gameMapClass);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> playerSelectedHeroes = getEngine().getEntitiesFor(Family.all(HeroDataComponent.class, SelectedComponent.class, game.gameManager.getCurrentPlayerClass(), gameMapClass).get());
        if(playerSelectedHeroes.size() < 1) return;
        Entity hero = playerSelectedHeroes.first();

        HeroDataComponent heroData = heroDataMap.get(hero);
        PositionComponent heroPosition = posMap.get(hero);
        if (heroData.heroPath.hasWalkNodes()) {
            Vector3 node = heroData.heroPath.getWalk().getFirstElement();
            if (heroPosition.x == node.x && heroPosition.y == node.y) {
                Entity removedEntity = heroData.pathEntities.remove(0);
                removedEntity
                        .add(game.engine.createComponent(LifePeriodComponent.class).init(150))
                        .add(game.engine.createComponent(TransparencyComponent.class));
                heroData.movementPoints -= node.z;
                GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapDataArray.first());
                gameMapData.hud.getRightBar().updateSelectedHero(hero);
                heroData.heroPath.getWalk().removeFirstElement();
            }
        }
    }
}
