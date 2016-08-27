package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by erykp_000 on 2016-03-08.
 */
public class TransparencyComponent implements Component, Pool.Poolable {
   public float transparency = 1;

    public TransparencyComponent(float transparency) {
        this.transparency = transparency;
    }

    @Override
    public void reset() {
        transparency = 1;
    }
}
