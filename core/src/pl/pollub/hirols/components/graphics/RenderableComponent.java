package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2015-11-28.
 */
public class RenderableComponent implements Component, Pool.Poolable {
    @Override
    public void reset() {

    }
    //do nothing for now, tells only if draw entity
}
