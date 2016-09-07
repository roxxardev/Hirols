package pl.pollub.hirols.components.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.managers.enums.Resource;

/**
 * Created by erykp_000 on 2016-03-09.
 */
public class PlayerDataComponent implements Component, Pool.Poolable {
    public final Map<Resource, Integer> resources = new HashMap<Resource, Integer>();

    public Class<? extends PlayerComponent> playerClass;

    public PlayerDataComponent init(Class<? extends PlayerComponent> playerClass) {
        this.playerClass = playerClass;
        return this;
    }

    @Override
    public void reset() {
        resources.clear();
    }
}