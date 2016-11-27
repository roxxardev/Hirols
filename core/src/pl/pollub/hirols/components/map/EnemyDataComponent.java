package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.UnitsManager;

/**
 * Created by Eryk on 2016-11-25.
 */

public class EnemyDataComponent implements Component, Pool.Poolable {
    public UnitsManager.Unit unit;

    public EnemyDataComponent init(UnitsManager.Unit unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public void reset() {
        unit = null;
    }
}
