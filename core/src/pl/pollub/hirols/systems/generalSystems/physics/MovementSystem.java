package pl.pollub.hirols.systems.generalSystems.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import pl.pollub.hirols.components.physics.PositionComponent;

import pl.pollub.hirols.components.physics.VelocityComponent;
import pl.pollub.hirols.systems.generalSystems.GeneralIteratingSystem;


/**
 * Created by Eryk on 2015-11-28.
 */
public abstract class MovementSystem extends GeneralIteratingSystem {

    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> velMap = ComponentMapper.getFor(VelocityComponent.class);

    public MovementSystem(int priority, Class<? extends Component> affiliationClass) {
        super(Family.all(PositionComponent.class,VelocityComponent.class, affiliationClass).get(), priority, affiliationClass);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        float force = 1f;

        PositionComponent position = posMap.get(entity);
        VelocityComponent velocity = velMap.get(entity);

        position.x += velocity.velocity.x * force * deltaTime;
        position.y += velocity.velocity.y * force * deltaTime;
    }
}
