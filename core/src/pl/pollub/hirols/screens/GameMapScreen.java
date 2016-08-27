package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.console.CommandsContainer;
import pl.pollub.hirols.console.GraphicalConsole;
import pl.pollub.hirols.gui.gameMap.GameMapHud;
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
    private final Viewport gameMapPort;

    private final Map map;
    private final Entity gameMapEntity;
    private final GraphicalConsole console;
    private final GameMapHud hud;

    private final InputManager inputManager;
    private final GestureDetector gestureDetector;
    private final MyInputProcessor myInputProcessor;



    public GameMapScreen(final Hirols game, final Map map, OrthographicCamera gameMapCam, Viewport gameMapPort) {
        super(game);
        this.gameMapCam = gameMapCam;
        this.gameMapPort = gameMapPort;
        this.map = map;

        inputManager = new InputManager();
        myInputProcessor = new MyInputProcessor(inputManager);
        gestureDetector = new GestureDetector(new MyGestureListener(inputManager));

        SpawnGenerator.loadEntities(game,map);

        hud = new GameMapHud(game, this);

        CommandsContainer commandsContainer = new CommandsContainer() {
            private Hirols hirols;
            private Map gameMap;
            {
                this.hirols = game;
                this.gameMap = map;
            }

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

            public void quit() { Gdx.app.exit();}

            public void setMovementPoints(float value) {
                Entity selectedHero = game.engine.getSystem(MapInteractionSystem.class).getSelectedHeroes().first();
                HeroDataComponent selectedHeroData = ComponentMapper.getFor(HeroDataComponent.class).get(selectedHero);
                selectedHeroData.movementPoints = value;
                game.engine.getSystem(MapInteractionSystem.class).resetHeroPath(selectedHeroData, true);

                console.log("Selected Hero id: "+ selectedHeroData.id +" movement points set to " + value + ".");
            }

            public void recalculatePath() {
                Entity selectedHero = game.engine.getSystem(MapInteractionSystem.class).getSelectedHeroes().first();
                HeroDataComponent selectedHeroData = ComponentMapper.getFor(HeroDataComponent.class).get(selectedHero);
                game.engine.getSystem(MapInteractionSystem.class).recalculatePathForHero(selectedHero);

                console.log("Path recalculated for selected hero id: "+ selectedHeroData.id + ".");
            }
        };
        console = new GraphicalConsole(commandsContainer,
                game.assetManager.get("default_skin/uiskin.json", Skin.class),game);


        createSystems();


        gameMapEntity = new Entity()
                        .add(map.getGameMapComponent())
                        .add(new GameMapDataComponent(map,gameMapCam,game.batch,inputManager, hud));
        game.engine.addEntity(gameMapEntity);

    }

    public GameMapComponent getGameMapComponent() {return map.getGameMapComponent();}

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

        systems.add(new HudRenderSystem(21,gameMapClass,hud){});
        systems.add(new ConsoleRenderSystem(22,gameMapClass,console){});
        systems.add(new InputManagerUpdateSystem(50,gameMapClass,inputManager){});
    }

    @Override
    public void render(float delta) {
        game.gameMapManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        hud.resize(width,height);
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
        game.engine.removeEntity(gameMapEntity);
        console.dispose();
        hud.dispose();
    }

    public GraphicalConsole getConsole() {
        return console;
    }
}
