package pl.pollub.hirols.systems.generalSystems.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.systems.generalSystems.GeneralSortedIteratingSystem;

/**
 * Created by Eryk on 2015-11-28.
 */
public class RenderSystem extends GeneralSortedIteratingSystem {

    private SpriteBatch batch;

    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TextureComponent> textureMap = ComponentMapper.getFor(TextureComponent.class);

    public RenderSystem(int priority, SpriteBatch batch, Class<? extends Component> affiliationClass) {
        super(Family.all(PositionComponent.class, RenderableComponent.class, TextureComponent.class, affiliationClass).get(), new Comparator<Entity>() {
            private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
            @Override
            public int compare(Entity o1, Entity o2) {
                return (int)Math.signum(posMap.get(o2).y - posMap.get(o1).y);
            }
        }, priority, affiliationClass);
        this.batch = batch;
    }

    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent posCom = posMap.get(entity);
        Sprite sprite = textureMap.get(entity).sprite;
        Vector2 offset = textureMap.get(entity).additionalOffset;
        sprite.setPosition(posCom.x + offset.x,posCom.y + offset.y);

        sprite.draw(batch);
    }
}
