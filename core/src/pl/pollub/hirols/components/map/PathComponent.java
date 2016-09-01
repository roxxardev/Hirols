package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2016-02-10.
 */
public class PathComponent implements Component, Pool.Poolable{
    //TODO to think about storing entity rather than id
    public int heroId = -1;

    public PathComponent init(int heroId) {
        this.heroId = heroId;
        return this;
    }

    @Override
    public void reset() {
        heroId = -1;
    }
}
