package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;

/**
 * Created by Eryk on 2016-02-10.
 */
public class PathComponent implements Component{
    public int heroId;

    public PathComponent(int heroId) {
        this.heroId = heroId;
    }
}
