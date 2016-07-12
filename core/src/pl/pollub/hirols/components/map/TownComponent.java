package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eryk on 2016-02-27.
 */
public class TownComponent implements Component {

    public Vector2 enterPosition;
    public String name;

    public TownComponent(Vector2 enterPosition) {
        this.enterPosition = enterPosition;
    }
}
