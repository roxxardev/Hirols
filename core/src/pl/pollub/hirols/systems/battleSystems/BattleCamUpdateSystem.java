package pl.pollub.hirols.systems.battleSystems;

import pl.pollub.hirols.components.battle.BattleComponent;
import pl.pollub.hirols.components.battle.BattleDataComponent;

/**
 * Created by Eryk on 2016-06-17.
 */
public class BattleCamUpdateSystem extends BattleEntitySystem {

    public BattleCamUpdateSystem(int priority, Class<? extends BattleComponent> battleClass) {
        super(priority, battleClass);
    }

    @Override
    public void update(float deltaTime) {
        if(battleData.size() < 1) return;
        BattleDataComponent battleDataComponent  = battleDataMapper.get(battleData.first());
        battleDataComponent.battleCam.update();
    }
}
