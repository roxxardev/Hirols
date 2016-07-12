package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;

/**
 * Created by Eryk on 2015-12-02.
 */
public class EnemyComponent implements Component {
    public float enemyTargetPositionX;
    public float enemyTargetPositionY;
    public int id;
    public boolean trueEntity;
    public String enemyName;

    public EnemyComponent(float enemyTargetPositionX, float enemyTargetPositionY, int id, boolean trueEntity) {
        this.enemyTargetPositionX = enemyTargetPositionX;
        this.enemyTargetPositionY = enemyTargetPositionY;
        this.id = id;
        this.trueEntity = trueEntity;
    }
}
