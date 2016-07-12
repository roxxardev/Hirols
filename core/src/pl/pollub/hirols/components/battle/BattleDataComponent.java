package pl.pollub.hirols.components.battle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

import pl.pollub.hirols.battle.HexagonMapPolygon;
import pl.pollub.hirols.managers.input.InputManager;

/**
 * Created by Eryk on 2016-05-02.
 */
public class BattleDataComponent implements Component{

    public OrthographicCamera battleCam;
    public InputManager inputManager;
    public HexagonMapPolygon hexagonMapPolygon;

    public BattleDataComponent(OrthographicCamera battleCam, InputManager inputManager, HexagonMapPolygon hexagonMapPolygon) {
        this.battleCam = battleCam;
        this.inputManager = inputManager;
        this.hexagonMapPolygon = hexagonMapPolygon;
    }
}
