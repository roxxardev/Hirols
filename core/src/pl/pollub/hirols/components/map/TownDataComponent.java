package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.managers.enums.Race;

/**
 * Created by erykp_000 on 2016-11-06.
 */

public class TownDataComponent implements Component, Pool.Poolable{

    public String name;
    public Race race;
    public Entity heroInTown;

    public TownDataComponent init(String name, Race race) {
        this.name = name;
        this.race = race;
        return this;
    }

    @Override
    public void reset() {
        name = null;
        race = null;
        heroInTown = null;
    }
}
