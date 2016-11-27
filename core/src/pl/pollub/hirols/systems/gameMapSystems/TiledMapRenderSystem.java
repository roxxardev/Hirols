package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.gdx.graphics.OrthographicCamera;

import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;

/**
 * Created by Eryk on 2016-05-01.
 */
public class TiledMapRenderSystem extends GameMapEntitySystem {

    public TiledMapRenderSystem(int priority, Class<? extends GameMapComponent> gameMapClass) {
        super(priority, gameMapClass);
    }

    @Override
    public void update(float deltaTime) {
        if(gameMapDataArray.size() < 1) return;
        GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapDataArray.first());
        //gameMapData.tiledMapRenderer.setView(gameMapData.gameMapCam);
        OrthographicCamera camera = gameMapData.gameMapCam;

        float width = (camera.viewportWidth + gameMapData.map.getTileWidth()) * camera.zoom;
        float height = (camera.viewportHeight + gameMapData.map.getTileHeight()) * camera.zoom ;
        float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
        float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);

        gameMapData.tiledMapRenderer.setView(gameMapData.gameMapCam.combined, camera.position.x - w / 2 , camera.position.y - h / 2 , w, h);
        gameMapData.tiledMapRenderer.render();
    }
}
