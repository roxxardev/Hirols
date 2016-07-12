package pl.pollub.hirols.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eryk on 2015-11-28.
 */
public class VelocityComponent implements Component {
    public Vector2 velocity;

    public VelocityComponent(Vector2 velocity) {
        this.velocity = velocity;
    }
    public VelocityComponent() { this.velocity = new Vector2(); }
}
