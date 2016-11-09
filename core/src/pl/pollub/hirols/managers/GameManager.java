package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.SelectedComponent;
import pl.pollub.hirols.components.map.maps.GameMap1;
import pl.pollub.hirols.components.map.maps.GameMap2;
import pl.pollub.hirols.components.map.maps.GameMap3;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.player.Player1;
import pl.pollub.hirols.components.player.Player2;
import pl.pollub.hirols.components.player.Player3;
import pl.pollub.hirols.components.player.Player4;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.components.player.PlayerDataComponent;
import pl.pollub.hirols.screens.GameMapScreen;
import pl.pollub.hirols.systems.genericSystems.BeginSystem;
import pl.pollub.hirols.systems.genericSystems.ClearSystem;
import pl.pollub.hirols.systems.genericSystems.EndSystem;

/**
 * Created by Eryk on 2016-04-25.
 */
public class GameManager {

    private final Hirols game;

    private final HashMap<String, GameMapScreen> mapScreens = new HashMap<String, GameMapScreen>();
    private GameMapScreen currentGameMapScreen;

    private final ArrayList<EntitySystem> sharedSystems = new ArrayList<EntitySystem>();

    private final ArrayList<Class<? extends GameMapComponent>> availableGameMapComponents = new ArrayList<Class<? extends GameMapComponent>>();
    private final ArrayList<Class<? extends PlayerComponent>> availablePlayerComponents = new ArrayList<Class<? extends PlayerComponent>>();

    private final ArrayList<Class<? extends PlayerComponent>> players = new ArrayList<Class<? extends PlayerComponent>>();

    private final TmxMapLoader tmxMapLoader = new TmxMapLoader();
    private final TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();

    private final OrthographicCamera gameMapCam;
    private final Viewport gameMapPort;

    public GameManager(Hirols game, int playersNumber) {
        this.game = game;
        parameters.generateMipMaps = false;
        parameters.textureMagFilter = Texture.TextureFilter.Nearest;
        parameters.textureMinFilter = Texture.TextureFilter.Nearest;

        gameMapCam = new OrthographicCamera();
        gameMapCam.setToOrtho(false);
        gameMapPort = new ScreenViewport(gameMapCam);

        createSharedSystems();

        availableGameMapComponents.add(GameMap1.class);
        availableGameMapComponents.add(GameMap2.class);
        availableGameMapComponents.add(GameMap3.class);

        availablePlayerComponents.add(Player1.class);
        availablePlayerComponents.add(Player2.class);
        availablePlayerComponents.add(Player3.class);
        availablePlayerComponents.add(Player4.class);

        if(playersNumber > availablePlayerComponents.size()) throw new IndexOutOfBoundsException();
        for(int i = 0; i < playersNumber; i++) {
            players.add(availablePlayerComponents.get(i));
        }
    }

    public void createMap(String filePath) {
        //TODO exception handling
        pl.pollub.hirols.gameMap.Map map = new pl.pollub.hirols.gameMap.Map(game,tmxMapLoader.load(filePath, parameters),getNewGameMapComponentClass());
        GameMapScreen gameMapScreen = new GameMapScreen(game,map,gameMapCam,gameMapPort);
        mapScreens.put(filePath,gameMapScreen);
        if(currentGameMapScreen == null) currentGameMapScreen = gameMapScreen; else gameMapScreen.setSystemsProcessing(false);
    }

    private Class<? extends GameMapComponent> getNewGameMapComponentClass() {
        return (availableGameMapComponents.size() > 0) ? availableGameMapComponents.remove(0) : null;
    }

    public void update(float delta) {
        game.engine.update(delta);
    }

    public GameMapScreen getCurrentMapScreen() {
        return currentGameMapScreen;
    }

    public void setCurrentMapScreen(GameMapScreen gameMapScreen) {
        currentGameMapScreen = gameMapScreen;
        game.setScreen(currentGameMapScreen);
    }

    public void setCurrentMapScreen(String mapName) {
        setCurrentMapScreen(mapScreens.get(mapName));
    }

    public ArrayList<Class<? extends PlayerComponent>> getPlayerClasses() {
        return players;
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

    public Entity getCurrentPlayer() {
        ImmutableArray<Entity> currentPlayers = game.engine.getEntitiesFor(Family.all(SelectedComponent.class, PlayerDataComponent.class).get());
        if(currentPlayers.size() > 1) throw new ArrayIndexOutOfBoundsException();

        return currentPlayers.first();
    }

    public Class<? extends PlayerComponent> getCurrentPlayerClass() {
        return attachedToPlayer(getCurrentPlayer());
    }

    public Class<? extends PlayerComponent> attachedToPlayer(Entity entity) {
        for(Class<? extends PlayerComponent> playerClass : players) {
            if(ComponentMapper.getFor(playerClass).has(entity)) return playerClass;
        }
        return null;
    }

    public void dispose() {
        removeSharedSystemsFromEngine();
        for(GameMapScreen mapScreen : mapScreens.values()) {
            mapScreen.dispose();
        }
    }
}
