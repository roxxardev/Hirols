package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.map.BannerComponent;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.physics.PositionComponent;

/**
 * Created by erykp_000 on 2016-12-24.
 */

public class BannerRenderSystem extends GameMapIteratingSystem {
    private SpriteBatch batch;

    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<BannerComponent> bannerMap = ComponentMapper.getFor(BannerComponent.class);

    public BannerRenderSystem(int priority, Class<? extends GameMapComponent> gameMapClass, SpriteBatch batch) {
        super(Family.all(gameMapClass, RenderableComponent.class, BannerComponent.class).get(),  priority, gameMapClass);
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent posCom = posMap.get(entity);
        BannerComponent bannerComponent = bannerMap.get(entity);
        Sprite sprite = bannerComponent.bannerSprite;
        Vector2 offset = bannerComponent.offset;
        sprite.setColor(bannerComponent.color);
        sprite.setPosition(posCom.x + offset.x,posCom.y + offset.y);

        sprite.draw(batch);
    }
}
