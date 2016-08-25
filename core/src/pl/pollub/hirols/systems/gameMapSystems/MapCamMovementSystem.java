package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;

/**
 * Created by Eryk on 2016-05-01.
 */
public class MapCamMovementSystem extends GameMapEntitySystem {

    private float origDistance;
    private float origZoom;
    private Vector2 panDelta = new Vector2();

    public MapCamMovementSystem(int priority, Class<? extends GameMapComponent> gameMapClass) {
        super(priority, gameMapClass);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        if(gameMapDataArray.size() < 1) return;

        GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapDataArray.first());
        OrthographicCamera cam = gameMapData.gameMapCam;

        panDelta = gameMapData.inputManager.getPanDelta(panDelta);

        if (gameMapData.inputManager.isPan()) {

            Vector3 tempGameCamPosition = cam.position.cpy();
            tempGameCamPosition.x -= panDelta.x * cam.zoom;
            tempGameCamPosition.y += panDelta.y * cam.zoom;

            if (tempGameCamPosition.x > 0 &&
                    tempGameCamPosition.x < gameMapData.map.getMapRect().getWidth()) {
                cam.position.x = tempGameCamPosition.x;
            }
            if (tempGameCamPosition.y > 0 &&
                    tempGameCamPosition.y < gameMapData.map.getMapRect().getHeight()) {
                cam.position.y = tempGameCamPosition.y;
            }
        }

        float zoom = 0.02f * cam.zoom*cam.zoom * gameMapData.inputManager.getScrolledAmount();

        if(zoom != 0) {
            final float minZoomTilesVertically = 7.5f;
            final float maxZoomTilesVertically = 22.5f;
            float maxZoomIn = minZoomTilesVertically /(((float) Gdx.graphics.getHeight())/gameMapData.map.getTileHeight());
            float maxZoomOut = maxZoomTilesVertically /(((float) Gdx.graphics.getHeight())/gameMapData.map.getTileHeight());
            if(maxZoomIn <0) maxZoomIn = 0; else if(maxZoomIn >1) maxZoomIn =1;
            if(maxZoomOut <1) maxZoomOut = 1f;
            float tempZoom = cam.zoom + zoom;
            if (tempZoom < maxZoomIn) {
                cam.zoom = maxZoomIn;
            } else if (tempZoom > maxZoomOut) {
                cam.zoom = maxZoomOut;
            } else cam.zoom = tempZoom;
            Gdx.app.log("MapCamMovementSystem", "GameMapCam zoom: "+ cam.zoom);
        }

        if(gameMapData.inputManager.getZoom()) {
            final float minZoomTilesVertically = 7.5f;
            final float maxZoomTilesVertically = 22.5f;
            float maxZoomIn = minZoomTilesVertically /(((float) Gdx.graphics.getHeight())/gameMapData.map.getTileHeight());
            float maxZoomOut = maxZoomTilesVertically /(((float) Gdx.graphics.getHeight())/gameMapData.map.getTileHeight());
            if(maxZoomIn <0) maxZoomIn = 0; else if(maxZoomIn >1) maxZoomIn =1;
            if(maxZoomOut <1) maxZoomOut = 1f;

            if(gameMapData.inputManager.getOriginalDistance() != origDistance){
                origDistance = gameMapData.inputManager.getOriginalDistance();
                origZoom = cam.zoom;
            }
            zoom = origZoom*gameMapData.inputManager.getOriginalDistance()/gameMapData.inputManager.getCurrentDistance();
            if (zoom < maxZoomIn) {
                cam.zoom = maxZoomIn;
            } else if (zoom > maxZoomOut) {
                cam.zoom = maxZoomOut;
            } else cam.zoom = zoom;
            Gdx.app.log("MapCamMovementSystem", "GameMapCam zoom: "+ cam.zoom);
        }
    }
}
