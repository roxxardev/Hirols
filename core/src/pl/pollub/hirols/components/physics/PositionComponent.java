package pl.pollub.hirols.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2015-11-28.
 */
public class PositionComponent implements Component, Pool.Poolable {
    public float x = 0.0f;
    public float y = 0.0f;

    public PositionComponent() {
    }

    public PositionComponent(float x, float y) {
        init(x,y);
    }

    public PositionComponent(Vector2 position) {
        this(position.x,position.y);
    }

    public PositionComponent(Vector3 position) {
        this(position.x,position.y);
    }

    public PositionComponent init(float x, float y) {
        this.y = y;
        this.x = x;
        return this;
    }

    @Override
    public void reset() {
        x = 0f;
        y = 0f;
    }
}
