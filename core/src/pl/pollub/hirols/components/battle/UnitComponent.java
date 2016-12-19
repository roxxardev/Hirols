package pl.pollub.hirols.components.battle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.UnitsManager;

/**
 * Created by Eryk on 2016-12-16.
 */

public class UnitComponent implements Component, Pool.Poolable {

    public UnitsManager.Unit unit;
    public int quantity;

    public UnitComponent init(UnitsManager.Unit unit, int quantity) {
        this.unit = unit;
        this.quantity = quantity;
        return this;
    }

    @Override
    public void reset() {
        unit = null;
        quantity = 0;
    }
}
