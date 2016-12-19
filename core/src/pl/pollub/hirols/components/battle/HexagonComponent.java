package pl.pollub.hirols.components.battle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.battle.HexagonTilePolygon;

/**
 * Created by Eryk on 2016-12-16.
 */

public class HexagonComponent implements Component, Pool.Poolable {
    public HexagonTilePolygon hexagonTilePolygon;

    public HexagonComponent init(HexagonTilePolygon hexagonTilePolygon) {
        this.hexagonTilePolygon = hexagonTilePolygon;
        return this;
    }

    @Override
    public void reset() {
        hexagonTilePolygon = null;
    }
}
