package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.battle.BattleCommands;
import pl.pollub.hirols.battle.HexagonMapPolygon;
import pl.pollub.hirols.components.battle.BattleComponent;
import pl.pollub.hirols.components.battle.BattleDataComponent;
import pl.pollub.hirols.console.GraphicalConsole;
import pl.pollub.hirols.gui.battle.BattleHud;
import pl.pollub.hirols.managers.EngineTools;
import pl.pollub.hirols.managers.input.InputManager;
import pl.pollub.hirols.managers.input.MyGestureListener;
import pl.pollub.hirols.managers.input.MyInputProcessor;
import pl.pollub.hirols.systems.battleSystems.BattleCamUpdateSystem;
import pl.pollub.hirols.systems.battleSystems.BattleInteractionSystem;
import pl.pollub.hirols.systems.battleSystems.HexMapRenderSystem;
import pl.pollub.hirols.systems.generalSystems.FontsDeathSystem;
import pl.pollub.hirols.systems.generalSystems.InputManagerUpdateSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.AnimationSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.BitmapFontRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.ConsoleRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.HudRenderSystem;
import pl.pollub.hirols.systems.generalSystems.graphics.RenderSystem;
import pl.pollub.hirols.systems.generalSystems.physics.MovementSystem;

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

    private BattleHud hud;

    public BattleScreen(Hirols game) {
        super(game);

        int width = 1280;
        int height = 720;

        battleCam = new OrthographicCamera();
        battleViewport = new FitViewport(width,height,battleCam);

        inputManager = new InputManager();
        gestureDetector = new GestureDetector(new MyGestureListener(inputManager));
        myInputProcessor = new MyInputProcessor(inputManager);

        battleComponent = new BattleComponent();

        int mapWidth = 12, mapHeight = 7;
        hexagonMapPolygon = new HexagonMapPolygon(game,mapWidth,mapHeight,width/30,new Vector2(200f,100f));

        Sprite backgroundSprite = new Sprite(game.assetManager.get("battle/battleBackground.png", Texture.class));
        backgroundSprite.setBounds(0,0 ,width,height);

        hexagonMapPolygon.setBackgroundSprite(backgroundSprite);

        battleEntity = game.engine.createEntity()
                .add(battleComponent)
                .add(game.engine.createComponent(BattleDataComponent.class).init(battleCam,inputManager,hexagonMapPolygon,battleViewport));
        game.engine.addEntity(battleEntity);

        console = new GraphicalConsole(new BattleCommands(game),
                game.assetManager.get("ui/default_skin/uiskin.json", Skin.class),
                game, new FitViewport(width,height));

        hud = new BattleHud(game, new FitViewport(width,height));

        createSystems();
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
        console.removeInputProcessorFromMultiplexer();
        dispose();
    }

    @Override
    protected void createSystems() {
        Class<? extends BattleComponent> battleClass = battleComponent.getClass();

        systems.add(new BattleInteractionSystem(6,battleClass));
        systems.add(new MovementSystem(8,battleClass));
        systems.add(new FontsDeathSystem(9,battleClass));
        systems.add(new AnimationSystem(10,battleClass));
        systems.add(new BattleCamUpdateSystem(13,battleClass));
        systems.add(new HexMapRenderSystem(14,battleClass,game.batch));

        systems.add(new RenderSystem(16, game.batch, battleClass));
        systems.add(new BitmapFontRenderSystem(17,game.batch,battleClass));
        systems.add(new HudRenderSystem(21,battleClass,hud));
        systems.add(new ConsoleRenderSystem(22,battleClass,console));
        systems.add(new InputManagerUpdateSystem(50,battleClass,inputManager));
    }

    @Override
    public void render(float delta) {
        if(inputManager.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.gameManager.getCurrentMapScreen());
        }
        game.engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        battleViewport.update(width, height, true);
        console.resize(width,height);
        hud.resize(width,height);
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
