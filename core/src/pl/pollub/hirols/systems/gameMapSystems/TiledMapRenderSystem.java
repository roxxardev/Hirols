package pl.pollub.hirols.systems.gameMapSystems;

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
        gameMapData.tiledMapRenderer.setView(gameMapData.gameMapCam);
        gameMapData.tiledMapRenderer.render();
    }
}
