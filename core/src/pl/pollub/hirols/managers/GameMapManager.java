package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.battle.BattleComponent;
import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.screens.GameMapScreen;
import pl.pollub.hirols.systems.genericSystems.BeginSystem;
import pl.pollub.hirols.systems.genericSystems.ClearSystem;
import pl.pollub.hirols.systems.genericSystems.EndSystem;

/**
 * Created by Eryk on 2016-04-25.
 */
public class GameMapManager {

    private Hirols game;

    private HashMap<String, GameMapScreen> maps = new HashMap<String, GameMapScreen>();
    private String currentMap;

    private TmxMapLoader tmxMapLoader = new TmxMapLoader();
    private TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();

    private OrthographicCamera gameMapCam;
    private Viewport gameMapPort;

    private ArrayList<EntitySystem> sharedSystems = new ArrayList<EntitySystem>();
    private ArrayList<Entity> sharedEntities = new ArrayList<Entity>();

    private ArrayList<GameMapComponent> gameMapComponents = new ArrayList<GameMapComponent>();

    public GameMapManager(Hirols game) {
        this.game = game;
        parameters.generateMipMaps = true;
        parameters.textureMagFilter = Texture.TextureFilter.Linear;
        parameters.textureMinFilter = Texture.TextureFilter.MipMap;

        gameMapCam = new OrthographicCamera();
        gameMapCam.setToOrtho(false);
        gameMapPort = new ScreenViewport(gameMapCam);

        createSharedSystems();

        gameMapComponents.add(new GameMapComponent(){});
        gameMapComponents.add(new GameMapComponent(){});
        gameMapComponents.add(new GameMapComponent(){});
        gameMapComponents.add(new GameMapComponent(){});
    }

    private GameMapComponent getNewGameMapComponentClass() {
        return (gameMapComponents.size() > 0) ? gameMapComponents.remove(0) : null;
    }

    public void createMap(String filePath) {
        //TODO exception handling
        pl.pollub.hirols.gameMap.Map map = new pl.pollub.hirols.gameMap.Map(game,tmxMapLoader.load(filePath),getNewGameMapComponentClass());
        GameMapScreen gameMapScreen = new GameMapScreen(game,map,gameMapCam,gameMapPort);
        maps.put(filePath,gameMapScreen);
        if(currentMap == null) currentMap = filePath; else gameMapScreen.setSystemsProcessing(false);
    }

    public void update(float delta) {
        game.engine.update(delta);
    }

    public GameMapScreen getCurrentMapScreen() {
        return maps.get(currentMap);
    }

    private void createSharedSystems() {
        sharedSystems.add(new ClearSystem(1));
        sharedSystems.add(new BeginSystem(15,game.batch));
        sharedSystems.add(new EndSystem(20,game.batch));
        addSharedSystemsToEngine();
    }

    private void addSharedSystemsToEngine() {
        for(EntitySystem entitySystem : sharedSystems) {
            game.engine.addSystem(entitySystem);
        }
    }

    private void removeSharedSystemsFromEngine() {
        for(EntitySystem entitySystem : sharedSystems) {
            game.engine.removeSystem(entitySystem);
        }
    }

    private void removeSharedEntitiesFromEngine() {
        for(Entity entity : sharedEntities) {
            game.engine.removeEntity(entity);
        }
    }

    public void dispose() {
        removeSharedEntitiesFromEngine();
        removeSharedSystemsFromEngine();
        for(GameMapScreen mapScreen : maps.values()) {
            mapScreen.dispose();
        }
    }
}
