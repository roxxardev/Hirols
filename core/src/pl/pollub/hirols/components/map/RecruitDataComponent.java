package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.UnitsManager;

/**
 * Created by erykp_000 on 2016-12-26.
 */

public class RecruitDataComponent implements Component, Pool.Poolable {
    public UnitsManager.Unit unit;
    public int amountPerWeek;
    public int currentNumber;

    public RecruitDataComponent init(UnitsManager.Unit unit, int amountPerWeek) {
        this.amountPerWeek = amountPerWeek;
        this.unit = unit;
        this.currentNumber = amountPerWeek;
        return this;
    }

    @Override
    public void reset() {
        unit = null;
        amountPerWeek = 0;
        currentNumber = 0;
    }
}
