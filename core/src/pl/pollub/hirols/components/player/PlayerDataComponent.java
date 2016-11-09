package pl.pollub.hirols.components.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.managers.enums.Resource;

/**
 * Created by erykp_000 on 2016-03-09.
 */
public class PlayerDataComponent implements Component, Pool.Poolable {
    public final Map<Resource, Integer> resources = new HashMap<Resource, Integer>();

    public Color color;
    public String name;

    public PlayerDataComponent init(Color color, String name) {
        this.color = color;
        this.name = name;
        return this;
    }

    @Override
    public void reset() {
        resources.clear();
        color = null;
        name = null;
    }
}