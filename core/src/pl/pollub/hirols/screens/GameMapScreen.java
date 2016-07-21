package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.console.CommandsContainer;
import pl.pollub.hirols.console.GraphicalConsole;
import pl.pollub.hirols.managers.SpawnGenerator;
import pl.pollub.hirols.managers.input.InputManager;
import pl.pollub.hirols.gameMap.Map;
import pl.pollub.hirols.managers.input.MyGestureListener;
import pl.pollub.hirols.managers.input.MyInputProcessor;
import pl.pollub.hirols.systems.gameMapSystems.EndNodeInteractionSystem;
import pl.pollub.hirols.systems.gameMapSystems.MapCamMovementSystem;
import pl.pollub.hirols.systems.gameMapSystems.MapCamUpdateSystem;
import pl.pollub.hirols.systems.gameMapSystems.HeroMovementSystem;
import pl.pollub.hirols.systems.gameMapSystems.MapInteractionSystem;
import pl.pollub.hirols.systems.gameMapSystems.PathEntityRemovalSystem;
import pl.pollub.hirols.systems.generalSystems.FontsDeathSystem;
import pl.pollub.hirols.systems.generalSystems.InputManagerUpdateSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.AnimationSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.BitmapFontRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.RenderSystem;
import pl.pollub.hirols.systems.gameMapSystems.TiledMapRenderSystem;
import pl.pollub.hirols.systems.generalSystems.physics.MovementSystem;

/**
 * Created by Eryk on 2016-04-25.
 */
public class GameMapScreen extends GameScreen {

    private final OrthographicCamera gameMapCam;
    private final Viewport gameMapPort;

    private final Map map;
    private final Entity gameMapEntity;

    private final InputManager inputManager;
    private final GestureDetector gestureDetector;
    private final MyInputProcessor myInputProcessor;

    private GraphicalConsole console;

    public GameMapScreen(Hirols game, Map map, OrthographicCamera gameMapCam, Viewport gameMapPort) {
        super(game);
        this.gameMapCam = gameMapCam;
        this.gameMapPort = gameMapPort;
        this.map = map;

        inputManager = new InputManager();
        myInputProcessor = new MyInputProcessor(inputManager);
        gestureDetector = new GestureDetector(new MyGestureListener(inputManager));

        createSystems();

        SpawnGenerator.loadEntities(game,map);

        gameMapEntity = new Entity()
                        .add(map.getGameMapComponent())
                        .add(new GameMapDataComponent(map,gameMapCam,game.batch,inputManager));
        game.engine.addEntity(gameMapEntity);

        CommandsContainer commandsContainer = new CommandsContainer() {
            @Override
            public void exit() {
                Gdx.app.exit();
            }

            @Override
            public void showCommands() {
                console.showCommands();
            }

            @Override
            public void clear() {
                console.clear();
            }

            public void dupa(String s) {
                console.log("NO ELO");
            }

            public void quit() { Gdx.app.exit();}
        };
        console = new GraphicalConsole(commandsContainer,
                game.assetManager.get("default_skin/uiskin.json", Skin.class),game);
    }

    @Override
    protected void createSystems() {
        //TODO systems
        Class<? extends GameMapComponent> gameMapClass = map.getGameMapComponent().getClass();

        systems.add(new MapCamMovementSystem(3,gameMapClass));
        systems.add(new EndNodeInteractionSystem(4,gameMapClass,game));
        systems.add(new PathEntityRemovalSystem(5,gameMapClass));
        systems.add(new MapInteractionSystem(6,game,gameMapClass));
        systems.add(new HeroMovementSystem(7,gameMapClass));
        systems.add(new MovementSystem(8,gameMapClass){});
        systems.add(new FontsDeathSystem(9,gameMapClass){});
        systems.add(new AnimationSystem(10,gameMapClass){});
        systems.add(new MapCamUpdateSystem(13,gameMapClass));
        systems.add(new TiledMapRenderSystem(14, gameMapClass));

        systems.add(new RenderSystem(16, game.batch, gameMapClass){});
        systems.add(new BitmapFontRenderSystem(17,game.batch,gameMapClass){});
        systems.add(new InputManagerUpdateSystem(50,gameMapClass,inputManager){});
    }

    @Override
    public void render(float delta) {
        game.gameMapManager.update(delta);
        console.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        gameMapPort.update(width, height);
        gameMapCam.zoom = 1;
        console.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        super.show();
        game.multiplexer.addProcessor(gestureDetector);
        game.multiplexer.addProcessor(myInputProcessor);
        console.addInputProcessorToMultiplexer();
    }

    @Override
    public void hide() {
        super.hide();
        game.multiplexer.removeProcessor(gestureDetector);
        game.multiplexer.removeProcessor(myInputProcessor);
        console.setVisible(false);
        console.removeInputProcessorFromMultiplexer();
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        game.engine.removeEntity(gameMapEntity);
        console.dispose();
    }
}
