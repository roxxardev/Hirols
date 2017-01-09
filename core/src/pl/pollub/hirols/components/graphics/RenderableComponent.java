package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.RenderPriority;

/**
 * Created by Eryk on 2015-11-28.
 */
public class RenderableComponent implements Component, Pool.Poolable{

    public RenderPriority renderPriority = RenderPriority.MEDIUM;

    public RenderableComponent init(RenderPriority renderPriority) {
        this.renderPriority = renderPriority;
        return this;
    }

    @Override
    public void reset() {
        renderPriority = RenderPriority.MEDIUM;
    }
}
