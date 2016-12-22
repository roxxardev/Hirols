package pl.pollub.hirols.systems.battleSystems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;

import pl.pollub.hirols.battle.HexagonMapPolygon;
import pl.pollub.hirols.battle.HexagonTilePolygon;
import pl.pollub.hirols.components.battle.BattleComponent;
import pl.pollub.hirols.components.battle.BattleDataComponent;

/**
 * Created by Eryk on 2016-11-26.
 */

public class BattleInteractionSystem extends BattleEntitySystem{

    public BattleInteractionSystem(int priority, Class<? extends BattleComponent> battleClass) {
        super(priority, battleClass);
    }

    @Override
    public void update(float deltaTime) {
        BattleDataComponent battleData = battleDataMapper.get(this.battleData.first());

        if(battleData.inputManager.getUnreadTap()) {
            Vector3 mouseTemp = Pools.obtain(Vector3.class).set(battleData.inputManager.getMouseCoords().x,battleData.inputManager.getMouseCoords().y,0);
            battleData.battleViewport.unproject(mouseTemp);
            Vector2 mouseWorldPosition = Pools.obtain(Vector2.class);
            mouseWorldPosition.set(mouseTemp.x, mouseTemp.y);
            Pools.free(mouseTemp);





            HexagonMapPolygon hexagonMap = battleData.hexagonMapPolygon;
            HexagonTilePolygon tile = hexagonMap.getHexagonTileFromPoint(mouseWorldPosition);
            if(tile != null) {
                Color color = tile.sprite.getColor();
                color.a = 0.25f;
                tile.sprite.setColor(color);

            }

            Pools.free(mouseWorldPosition);
        }
    }
}
