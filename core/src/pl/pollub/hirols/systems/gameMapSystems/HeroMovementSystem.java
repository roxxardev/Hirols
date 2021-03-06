package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.SelectedComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.components.physics.VelocityComponent;

/**
 * Created by Eryk on 2016-03-04.
 */
public class HeroMovementSystem extends GameMapEntitySystem {

    private final Hirols game;

    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> velMap = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<HeroDataComponent> selectedHeroDataMap = ComponentMapper.getFor(HeroDataComponent.class);

    public HeroMovementSystem(int priority, Class<? extends GameMapComponent> gameMapClass, Hirols game) {
        super(priority,gameMapClass);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    public void update(float deltaTime) {
        GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapDataArray.first());

        ImmutableArray<Entity> selectedHeroes = getEngine().getEntitiesFor(Family.all(HeroDataComponent.class, SelectedComponent.class, gameMapClass, game.gameManager.getCurrentPlayerClass()).get());

        for (Entity hero : selectedHeroes) {
            HeroDataComponent heroData = selectedHeroDataMap.get(hero);
            PositionComponent heroPosition = posMap.get(hero);
            VelocityComponent velocity = velMap.get(hero);

            if (heroData.heroPath.hasWalkNodes()) {
                if(heroData.movementPoints - heroData.heroPath.getWalk().getFirstElement().z >= 0)
                handleMovementForPosition(heroData.heroPath.getWalk().getFirstElement(), heroPosition, velocity, deltaTime, gameMapData.gameMapCam);
                else {
                    heroData.heroPath.stopFollowing(true);
                }
            }
        }
    }

    private void handleMovementForPosition(Vector3 destinationPosition, PositionComponent heroPosition, VelocityComponent velocity, float deltaTime, OrthographicCamera cam) {
        float sub1 = destinationPosition.x - heroPosition.x;
        float sub2 = destinationPosition.y - heroPosition.y;
        float tmp1, tmp2;
        float cameraOffset = 48;
        float speed = 200.0f; //TODO selectedHeroes speed setting in options
        if (sub1 > 0) {
            tmp1 = heroPosition.x + speed * deltaTime;
            cam.position.x = Math.round(tmp1 + cameraOffset);
            if (tmp1 >= destinationPosition.x) {
                heroPosition.x = destinationPosition.x;
                velocity.velocity.x = 0;
            } else {
                velocity.velocity.x = speed;
            }
        } else if (sub1 < 0) {
            tmp1 = heroPosition.x - speed * deltaTime;
            cam.position.x = Math.round(tmp1 + cameraOffset);
            if (tmp1 <= destinationPosition.x) {
                heroPosition.x = destinationPosition.x;
                velocity.velocity.x = 0;
            } else {
                velocity.velocity.x = -speed;
            }
        } else cam.position.x = Math.round(heroPosition.x + cameraOffset);

        if (sub2 > 0) {
            tmp2 = heroPosition.y + speed * deltaTime;
            cam.position.y = Math.round(tmp2 + cameraOffset);
            if (tmp2 >= destinationPosition.y) {
                heroPosition.y = destinationPosition.y;
                velocity.velocity.y = 0;
            } else {
                velocity.velocity.y = speed;
            }
        } else if (sub2 < 0) {
            tmp2 = heroPosition.y - speed * deltaTime;
            cam.position.y = Math.round(tmp2 + cameraOffset);
            if (tmp2 <= destinationPosition.y) {
                heroPosition.y = destinationPosition.y;
                velocity.velocity.y = 0;
            } else {
                velocity.velocity.y = -speed;
            }
        } else cam.position.y = Math.round(heroPosition.y + cameraOffset);
    }
}

