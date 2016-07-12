package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;

/**
 * Created by erykp_000 on 2016-03-08.
 */
public class TransparencyComponent implements Component {
   public float transparency = 1;

    public TransparencyComponent(float transparency) {
        this.transparency = transparency;
    }
}
