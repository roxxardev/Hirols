package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import pl.pollub.hirols.gameMap.Map;
import pl.pollub.hirols.managers.input.InputManager;

/**
 * Created by erykp_000 on 2016-04-24.
 * Component used for storing GameMap data.
 */
public class GameMapDataComponent implements Component {
    public Map map;
    public OrthogonalTiledMapRenderer tiledMapRenderer;
    public OrthographicCamera gameMapCam;
    public InputManager inputManager;

    public GameMapDataComponent(Map map, OrthographicCamera gameMapCam, SpriteBatch batch, InputManager inputManager) {
        this.map = map;
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap(), batch);
        this.gameMapCam = gameMapCam;
        this.inputManager = inputManager;
    }
}
