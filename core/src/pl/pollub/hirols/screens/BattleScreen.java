package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.battle.HexagonMapPolygon;
import pl.pollub.hirols.components.battle.BattleComponent;
import pl.pollub.hirols.components.battle.BattleDataComponent;
import pl.pollub.hirols.console.CommandsContainer;
import pl.pollub.hirols.console.GraphicalConsole;
import pl.pollub.hirols.managers.EngineTools;
import pl.pollub.hirols.managers.input.InputManager;
import pl.pollub.hirols.managers.input.MyGestureListener;
import pl.pollub.hirols.managers.input.MyInputProcessor;
import pl.pollub.hirols.systems.battleSystems.BattleCamUpdateSystem;
import pl.pollub.hirols.systems.battleSystems.HexMapRenderSystem;
import pl.pollub.hirols.systems.generalSystems.FontsDeathSystem;
import pl.pollub.hirols.systems.generalSystems.InputManagerUpdateSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.AnimationSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.BitmapFontRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.RenderSystem;
import pl.pollub.hirols.systems.generalSystems.physics.MovementSystem;
import pl.pollub.hirols.ui.battleScreenUI.BattleScreenHud;

/**
 * Created by Eryk on 2016-04-03.
 */
public class BattleScreen extends GameScreen {

    private final OrthographicCamera battleCam;
    private final Viewport battleViewport;

    private final InputManager inputManager;
    private final GestureDetector gestureDetector;
    private final MyInputProcessor myInputProcessor;

    private final BattleComponent battleComponent;
    private final Entity battleEntity;

    private final HexagonMapPolygon hexagonMapPolygon;

    private GraphicalConsole console;

    private BattleScreenHud hud;

    public BattleScreen(Hirols game) {
        super(game);

        battleCam = new OrthographicCamera();
        battleCam.setToOrtho(false);
        battleViewport = new ScreenViewport(battleCam);

        inputManager = new InputManager();
        gestureDetector = new GestureDetector(new MyGestureListener(inputManager));
        myInputProcessor = new MyInputProcessor(inputManager);

        battleComponent = new BattleComponent() {};

        int mapWidth = 12, mapHeight = 7;
        hexagonMapPolygon = new HexagonMapPolygon(mapWidth,mapHeight,0,new Vector2(200f,100f));
        hexagonMapPolygon.setBackgroundSprite(new Sprite(game.assetManager.get("battleBackground.png", Texture.class)));
        battleEntity = game.engine.createEntity()
                .add(battleComponent)
                .add(new BattleDataComponent(battleCam,inputManager,hexagonMapPolygon));
        game.engine.addEntity(battleEntity);

        createSystems();

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
                Gdx.app.log("TEST", s);
            }
        };
        console = new GraphicalConsole(commandsContainer,
                game.assetManager.get("default_skin/uiskin.json", Skin.class),
                game);

        hud = new BattleScreenHud(game);
    }

    @Override
    public void show() {
        super.show();
        game.multiplexer.addProcessor(hud.stage);
        game.multiplexer.addProcessor(gestureDetector);
        game.multiplexer.addProcessor(myInputProcessor);
        console.addInputProcessorToMultiplexer();
    }

    @Override
    public void hide() {
        super.hide();
        game.multiplexer.removeProcessor(hud.stage);
        game.multiplexer.removeProcessor(gestureDetector);
        game.multiplexer.removeProcessor(myInputProcessor);
        console.removeInputProcessorFromMultiplexer();
        dispose();
    }

    @Override
    protected void createSystems() {
        Class<? extends BattleComponent> battleClass = battleComponent.getClass();

        systems.add(new MovementSystem(8,battleClass));
        systems.add(new FontsDeathSystem(9,battleClass));
        systems.add(new AnimationSystem(10,battleClass));
        systems.add(new BattleCamUpdateSystem(13,battleClass));
        systems.add(new HexMapRenderSystem(14,battleClass,game.batch));

        systems.add(new RenderSystem(16, game.batch, battleClass));
        systems.add(new BitmapFontRenderSystem(17,game.batch,battleClass));
        systems.add(new InputManagerUpdateSystem(50,battleClass,inputManager));
    }

    @Override
    public void render(float delta) {
        if(inputManager.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.gameManager.getCurrentMapScreen());
            return;
        }
        hud.update(delta);

        game.engine.update(delta);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        console.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        battleViewport.update(width, height);
        battleCam.zoom = 1;
        battleCam.position.set(battleCam.viewportWidth / 2, battleCam.viewportHeight / 2, 0);
        battleCam.update();
        float offsetY = (height - width/16*9)/2;
        Sprite backgroundSprite = hexagonMapPolygon.getBackgroundSprite();
        backgroundSprite.setBounds(0,offsetY ,width,width/16*9);
        Vector2 margin = hexagonMapPolygon.getMargin();
        margin.set(backgroundSprite.getWidth()/100*10,backgroundSprite.getHeight()/100*10 + offsetY);
        hexagonMapPolygon.setSideAndUpdate((width - 2* margin.x)/((2*hexagonMapPolygon.getMapWidth()+1)* ((float) Math.cos(Math.toRadians(30)))));
        console.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        super.dispose();
        ImmutableArray<Entity> battleEntities = game.engine.getEntitiesFor(Family.all(battleComponent.getClass()).get());
        ArrayList<Entity> battleEntitiesSnapshot = EngineTools.getArraySnapshot(battleEntities);
        for(Entity entity : battleEntitiesSnapshot) {
            game.engine.removeEntity(entity);
        }
        console.dispose();
    }
}
