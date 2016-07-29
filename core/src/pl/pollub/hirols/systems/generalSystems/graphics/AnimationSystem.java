package pl.pollub.hirols.systems.generalSystems.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import pl.pollub.hirols.components.graphics.AnimationComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.systems.generalSystems.GeneralIteratingSystem;

/**
 * Created by Marcin on 2015-12-07.
 */

public abstract class AnimationSystem extends GeneralIteratingSystem {

    private ComponentMapper<AnimationComponent> animDataMap = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<TextureComponent> textureMap = ComponentMapper.getFor(TextureComponent.class);

    public AnimationSystem(int priority, Class<? extends Component> affiliationClass) {
        super(Family.all(AnimationComponent.class, TextureComponent.class, affiliationClass).get(), priority, affiliationClass);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animData = animDataMap.get(entity);

        animData.stateTime += deltaTime;
        textureMap.get(entity).sprite.setRegion(animData.animationSet.getCurrentAnimation().getKeyFrame(animData.stateTime,animData.looping));
    }
}
