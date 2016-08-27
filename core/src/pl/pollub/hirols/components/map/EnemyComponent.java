package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2015-12-02.
 */
public class EnemyComponent implements Component, Pool.Poolable {

    public final Vector2 enemyPosition = new Vector2();
    public int id = -1;
    public boolean trueEntity;
    public String enemyName;

    public EnemyComponent(float enemyTargetPositionX, float enemyTargetPositionY, int id, boolean trueEntity) {
        this.enemyPosition.set(enemyTargetPositionX,enemyTargetPositionY);
        this.id = id;
        this.trueEntity = trueEntity;
    }

    @Override
    public void reset() {
        enemyPosition.set(0,0);
        id = -1;
        trueEntity = false;
        enemyName = null;
    }
}
