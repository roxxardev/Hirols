package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;

/**
 * Created by Asmacker on 2016-02-20.
 */
public class MapComponent implements Component
{
    public boolean walkable;

    public MapComponent(boolean isWalkable)
    {
        this.walkable = isWalkable;
    }
}
