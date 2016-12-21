package pl.pollub.hirols.systems.generalSystems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import pl.pollub.hirols.components.LifePeriodComponent;
import pl.pollub.hirols.components.TextureRenderableRemovalComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.components.graphics.TransparencyComponent;

/**
 * Created by erykp_000 on 2016-12-20.
 */

public class TextureRenderableComponentRemovalSystem extends GeneralIteratingSystem {

    private ComponentMapper<LifePeriodComponent> lifePeriodMap = ComponentMapper.getFor(LifePeriodComponent.class);
    private ComponentMapper<TransparencyComponent> transparencyMap = ComponentMapper.getFor(TransparencyComponent.class);
    private ComponentMapper<TextureComponent> textureMap = ComponentMapper.getFor(TextureComponent.class);

    public TextureRenderableComponentRemovalSystem(int priority, Class<? extends Component> affiliationClass) {
        super(Family.all(LifePeriodComponent.class, TransparencyComponent.class, TextureComponent.class, RenderableComponent.class, TextureRenderableRemovalComponent.class, affiliationClass).get(), priority, affiliationClass);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifePeriodComponent lifePeriodComponent = lifePeriodMap.get(entity);

        float transparency = transparencyMap.get(entity).transparency =
                1.0f - (float)Math.pow((System.currentTimeMillis() - lifePeriodComponent.createTime)/(double)lifePeriodComponent.duration,2.0d);
        Color color = textureMap.get(entity).sprite.getColor();
        color.a = transparency;
        textureMap.get(entity).sprite.setColor(color);

        if(lifePeriodComponent.deathTime<=System.currentTimeMillis()) {
            entity.remove(TextureComponent.class);
            entity.remove(RenderableComponent.class);
            entity.remove(LifePeriodComponent.class);
            entity.remove(TransparencyComponent.class);
        }
    }
}
