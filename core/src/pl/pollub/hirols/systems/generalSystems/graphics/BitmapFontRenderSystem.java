package pl.pollub.hirols.systems.generalSystems.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pl.pollub.hirols.components.graphics.BitmapFontComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.systems.generalSystems.GeneralIteratingSystem;

/**
 * Created by Eryk on 2015-12-02.
 */
public abstract class BitmapFontRenderSystem extends GeneralIteratingSystem {

    private SpriteBatch batch;

    private ComponentMapper<BitmapFontComponent> bitmapMap = ComponentMapper.getFor(BitmapFontComponent.class);
    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

    public BitmapFontRenderSystem(int priority, SpriteBatch batch, Class<? extends Component> affiliationClass) {
        super(Family.all(BitmapFontComponent.class, PositionComponent.class, RenderableComponent.class, affiliationClass).get(), priority, affiliationClass);
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BitmapFontComponent bitmapFontData = bitmapMap.get(entity);
        PositionComponent position = posMap.get(entity);
        bitmapFontData.bitmapFont.getData().setScale(bitmapFontData.scale);
        bitmapFontData.bitmapFont.setColor(bitmapFontData.color);
        bitmapFontData.bitmapFont.draw(batch, bitmapFontData.sequence, position.x, position.y);
        //Gdx.app.log("BitmapFontRenderSystem", priority + "");
    }
}
