package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.ArrayList;

/**
 * Created by erykp_000 on 2016-08-25.
 */
public class EngineTools {

    public static ArrayList<Entity> getArraySnapshot(Engine engine, Family family) {
        ImmutableArray<Entity> entities = engine.getEntitiesFor(family);
        return getArraySnapshot(entities);
    }

    public static ArrayList<Entity> getArraySnapshot(ImmutableArray<Entity> entities) {
        ArrayList<Entity> snapshot = new ArrayList<Entity>();
        for(int i = 0; i < entities.size(); i++) {
            snapshot.add(entities.get(i));
        }
        return snapshot;
    }
}
