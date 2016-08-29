package pl.pollub.hirols.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2015-11-28.
 */
public class VelocityComponent implements Component, Pool.Poolable {
    public Vector2 velocity = new Vector2();

    public VelocityComponent(Vector2 velocity) {
        this.velocity.set(velocity);
    }

    public VelocityComponent() {

    }

    public VelocityComponent init(Vector2 velocity) {
        this.velocity.set(velocity);
        return this;
    }

    @Override
    public void reset() {
        velocity.set(0,0);
    }
}
