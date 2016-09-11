package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by Eryk on 2016-02-10.
 */
public class PathComponent implements Component, Pool.Poolable{
    //TODO to think about storing entity rather than id
    public int heroId = -1;
    public Direction direction;

    public PathComponent init(int heroId, Direction direction) {
        this.heroId = heroId;
        this.direction = direction;
        return this;
    }

    @Override
    public void reset() {
        heroId = -1;
        direction = null;
    }
}
