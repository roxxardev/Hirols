package pl.pollub.hirols.components.battle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.pollub.hirols.battle.HexagonMapPolygon;
import pl.pollub.hirols.managers.input.InputManager;

/**
 * Created by Eryk on 2016-05-02.
 */
public class BattleDataComponent implements Component, Pool.Poolable{

    public OrthographicCamera battleCam;
    public Viewport battleViewport;
    public InputManager inputManager;
    public HexagonMapPolygon hexagonMapPolygon;

    public BattleDataComponent init(OrthographicCamera battleCam, InputManager inputManager, HexagonMapPolygon hexagonMapPolygon, Viewport battleViewport) {
        this.battleCam = battleCam;
        this.inputManager = inputManager;
        this.hexagonMapPolygon = hexagonMapPolygon;
        this.battleViewport = battleViewport;
        return this;
    }

    @Override
    public void reset() {
        battleCam = null;
        inputManager = null;
        hexagonMapPolygon = null;
        battleViewport = null;
    }
}
