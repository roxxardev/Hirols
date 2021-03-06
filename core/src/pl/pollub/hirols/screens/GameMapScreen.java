package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.console.GraphicalConsole;
import pl.pollub.hirols.gui.gameMap.GameMapHud;
import pl.pollub.hirols.managers.EngineTools;
import pl.pollub.hirols.managers.SpawnGenerator;
import pl.pollub.hirols.managers.input.InputManager;
import pl.pollub.hirols.gameMap.Map;
import pl.pollub.hirols.managers.input.MyGestureListener;
import pl.pollub.hirols.managers.input.MyInputProcessor;
import pl.pollub.hirols.systems.gameMapSystems.BannerRenderSystem;
import pl.pollub.hirols.systems.gameMapSystems.EndNodeInteractionSystem;
import pl.pollub.hirols.systems.gameMapSystems.HeroAnimationChangerSystem;
import pl.pollub.hirols.systems.gameMapSystems.MapCamMovementSystem;
import pl.pollub.hirols.systems.gameMapSystems.MapCamUpdateSystem;
import pl.pollub.hirols.systems.gameMapSystems.HeroMovementSystem;
import pl.pollub.hirols.systems.gameMapSystems.MapInteractionSystem;
import pl.pollub.hirols.systems.gameMapSystems.PathEntityRemovalSystem;
import pl.pollub.hirols.systems.generalSystems.FontsDeathSystem;
import pl.pollub.hirols.systems.generalSystems.InputManagerUpdateSystem;
import pl.pollub.hirols.systems.generalSystems.TextureRenderableComponentRemovalSystem;
import pl.pollub.hirols.systems.generalSystems.TextureTransparentDeathSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.AnimationSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.BitmapFontRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.ConsoleRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.HudRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.RenderSystem;
import pl.pollub.hirols.systems.gameMapSystems.TiledMapRenderSystem;
import pl.pollub.hirols.systems.generalSystems.physics.MovementSystem;

/**
 * Created by Eryk on 2016-04-25.
 */
public class GameMapScreen extends GameScreen {

    private final OrthographicCamera gameMapCam;
    private final Viewport gameMapViewport;

    private final Map map;
    private final Entity gameMapEntity;
    private final GraphicalConsole console;
    private final GameMapHud hud;

    private final InputManager inputManager;
    private final GestureDetector gestureDetector;
    private final MyInputProcessor myInputProcessor;

    public GameMapScreen(final Hirols game, final Map map, OrthographicCamera gameMapCam, Viewport gameMapViewport) {
        super(game);
        this.gameMapCam = gameMapCam;
        this.gameMapViewport = gameMapViewport;
        this.map = map;

        inputManager = new InputManager();
        myInputProcessor = new MyInputProcessor(inputManager);
        gestureDetector = new GestureDetector(new MyGestureListener(inputManager));

        SpawnGenerator.loadEntities(game,map);

        hud = new GameMapHud(game, this);

        console = new GraphicalConsole(new pl.pollub.hirols.gameMap.GameMapCommands(game,this),
                game.assetManager.get("ui/default_skin/uiskin.json", Skin.class),game, new ScreenViewport());

        createSystems();

        gameMapEntity = game.engine.createEntity()
                        .add(game.engine.createComponent(map.getGameMapComponentClazz()))
                        .add(new GameMapDataComponent(map,gameMapCam,game.batch,inputManager, hud));
        game.engine.addEntity(gameMapEntity);
    }

    public Class<? extends GameMapComponent> getGameMapComponentClass() {return map.getGameMapComponentClazz();}

    @Override
    protected void createSystems() {
        Class<? extends GameMapComponent> gameMapClass = map.getGameMapComponentClazz();

        systems.add(new MapCamMovementSystem(3,gameMapClass, game));
        systems.add(new EndNodeInteractionSystem(4,gameMapClass,game));
        systems.add(new PathEntityRemovalSystem(5,gameMapClass,game));
        systems.add(new MapInteractionSystem(6,game,gameMapClass));
        systems.add(new HeroMovementSystem(7,gameMapClass,game));
        systems.add(new MovementSystem(8,gameMapClass));
        systems.add(new FontsDeathSystem(9,gameMapClass));
        systems.add(new TextureRenderableComponentRemovalSystem(9,gameMapClass));
        systems.add(new TextureTransparentDeathSystem(9,gameMapClass));
        systems.add(new HeroAnimationChangerSystem(9,gameMapClass,game));
        systems.add(new AnimationSystem(10,gameMapClass));
        systems.add(new MapCamUpdateSystem(13,gameMapClass));

        systems.add(new TiledMapRenderSystem(14, gameMapClass));

        systems.add(new BannerRenderSystem(17, gameMapClass, game.batch));
        systems.add(new RenderSystem(17, game.batch, gameMapClass));
        systems.add(new BitmapFontRenderSystem(18,game.batch,gameMapClass));

        systems.add(new HudRenderSystem(21,gameMapClass,hud));
        systems.add(new ConsoleRenderSystem(22,gameMapClass,console));
        systems.add(new InputManagerUpdateSystem(50,gameMapClass,inputManager));
    }

    @Override
    public void render(float delta) {
        game.gameManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        hud.resize(width,height);
        gameMapViewport.update(width, height);
        console.resize(width,height);
    }

    @Override
    public void show() {
        super.show();
        game.multiplexer.addProcessor(hud.getStage());
        game.multiplexer.addProcessor(gestureDetector);
        game.multiplexer.addProcessor(myInputProcessor);
        console.addInputProcessorToMultiplexer();
    }

    @Override
    public void hide() {
        super.hide();
        game.multiplexer.removeProcessor(hud.getStage());
        game.multiplexer.removeProcessor(gestureDetector);
        game.multiplexer.removeProcessor(myInputProcessor);
        console.setVisible(false);
        console.removeInputProcessorFromMultiplexer();
    }

    @Override
    public void dispose() {
        map.dispose();
        ImmutableArray<Entity> battleEntities = game.engine.getEntitiesFor(Family.all(getGameMapComponentClass()).get());
        ArrayList<Entity> battleEntitiesSnapshot = EngineTools.getArraySnapshot(battleEntities);
        for(Entity gameMapEntity : battleEntitiesSnapshot) {
            game.engine.removeEntity(gameMapEntity);
        }
        console.dispose();
        hud.dispose();
    }

    public GraphicalConsole getConsole() {
        return console;
    }

    public Map getMap() {
        return map;
    }

    public GameMapHud getHud() {
        return hud;
    }
}
