package pl.pollub.hirols.systems.battleSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import pl.pollub.hirols.components.battle.BattleComponent;
import pl.pollub.hirols.components.battle.BattleDataComponent;

/**
 * Created by Eryk on 2016-05-02.
 */
public abstract class BattleEntitySystem extends EntitySystem {

    protected ImmutableArray<Entity> battleData;
    protected ComponentMapper<BattleDataComponent> battleDataMapper = ComponentMapper.getFor(BattleDataComponent.class);

    protected Class<? extends BattleComponent> battleClass;

    public BattleEntitySystem(int priority, Class<? extends BattleComponent> battleClass) {
        super(priority);
        this.battleClass = battleClass;
    }

    @Override
    public void addedToEngine(Engine engine) {
        battleData = engine.getEntitiesFor(Family.all(BattleDataComponent.class,battleClass).get());
    }

    @Override
    public abstract void update(float deltaTime);
}
