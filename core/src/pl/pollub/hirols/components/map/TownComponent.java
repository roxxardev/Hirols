package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2016-02-27.
 */
public class TownComponent implements Component, Pool.Poolable {

    public Vector2 enterPosition = new Vector2();
    public String name;

    public TownComponent(Vector2 enterPosition) {
        this.enterPosition.set(enterPosition);
    }

    @Override
    public void reset() {
        enterPosition.set(0,0);
        name = null;
    }
}
