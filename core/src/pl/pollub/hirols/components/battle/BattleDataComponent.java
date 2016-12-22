package pl.pollub.hirols.components.battle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.pollub.hirols.battle.HexagonMapPolygon;
import pl.pollub.hirols.components.map.EnemyDataComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.managers.input.InputManager;

/**
 * Created by Eryk on 2016-05-02.
 */
public class BattleDataComponent implements Component, Pool.Poolable{

    public Viewport battleViewport;
    public InputManager inputManager;
    public HexagonMapPolygon hexagonMapPolygon;
    public HeroDataComponent heroData;
    public EnemyDataComponent enemyData;

    public BattleDataComponent init(InputManager inputManager, HexagonMapPolygon hexagonMapPolygon, Viewport battleViewport, HeroDataComponent heroData, EnemyDataComponent enemyData) {
        this.inputManager = inputManager;
        this.hexagonMapPolygon = hexagonMapPolygon;
        this.battleViewport = battleViewport;
        this.heroData = heroData;
        this.enemyData = enemyData;
        return this;
    }

    @Override
    public void reset() {
        inputManager = null;
        hexagonMapPolygon = null;
        battleViewport = null;
        heroData = null;
        enemyData = null;
    }
}
