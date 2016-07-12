package pl.pollub.hirols.systems.generalSystems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import pl.pollub.hirols.components.LifePeriodComponent;
import pl.pollub.hirols.components.graphics.BitmapFontComponent;
import pl.pollub.hirols.components.graphics.TransparencyComponent;
import pl.pollub.hirols.components.physics.VelocityComponent;

/**
 * Created by erykp_000 on 2016-03-08.
 */
public abstract class FontsDeathSystem extends GeneralIteratingSystem {

    private ComponentMapper<LifePeriodComponent> lifePeriodMap = ComponentMapper.getFor(LifePeriodComponent.class);
    private ComponentMapper<TransparencyComponent> transparencyMap = ComponentMapper.getFor(TransparencyComponent.class);
    private ComponentMapper<BitmapFontComponent> fontMap = ComponentMapper.getFor(BitmapFontComponent.class);
    private ComponentMapper<VelocityComponent> velMap = ComponentMapper.getFor(VelocityComponent.class);

    public FontsDeathSystem(int priority, Class<? extends Component> affiliationClass) {
        super(Family.all(LifePeriodComponent.class, BitmapFontComponent.class, TransparencyComponent.class, affiliationClass).get(), priority, affiliationClass);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifePeriodComponent lifePeriodComponent = lifePeriodMap.get(entity);

        float transparency = transparencyMap.get(entity).transparency =  1.0f - (float)Math.pow((System.currentTimeMillis() - lifePeriodComponent.createTime)/(double)lifePeriodComponent.duration,2.0d);
        fontMap.get(entity).bitmapFont.getColor().a = transparency;

        if(velMap.has(entity)) {
            VelocityComponent velocityComponent = velMap.get(entity);
            velocityComponent.velocity.y = 60.0f*transparency;
        }

        if(lifePeriodComponent.deathTime<=System.currentTimeMillis()) {
            getEngine().removeEntity(entity);
        }
    }

}
