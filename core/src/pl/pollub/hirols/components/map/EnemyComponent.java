package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2015-12-02.
 */
public class EnemyComponent implements Component, Pool.Poolable {

    public final Vector2 enemyPosition = new Vector2();
    public boolean trueEntity;

    public EnemyComponent init(Vector2 enemyTargetPosition, boolean trueEntity) {
        this.enemyPosition.set(enemyTargetPosition);
        this.trueEntity = trueEntity;
        return this;
    }

    @Override
    public void reset() {
        enemyPosition.set(0,0);
        trueEntity = false;
    }
}
