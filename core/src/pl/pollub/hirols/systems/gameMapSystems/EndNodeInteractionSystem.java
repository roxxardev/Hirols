package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.LifePeriodComponent;
import pl.pollub.hirols.components.PlayerComponent;
import pl.pollub.hirols.components.PlayerDataComponent;
import pl.pollub.hirols.components.graphics.BitmapFontComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.components.graphics.TransparencyComponent;
import pl.pollub.hirols.components.map.ChestComponent;
import pl.pollub.hirols.components.map.EnemyComponent;
import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.MapComponent;
import pl.pollub.hirols.components.map.MineComponent;
import pl.pollub.hirols.components.map.PathComponent;
import pl.pollub.hirols.components.map.ResourceComponent;
import pl.pollub.hirols.components.map.SelectedHeroComponent;
import pl.pollub.hirols.components.map.TownComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.components.physics.VelocityComponent;
import pl.pollub.hirols.screens.BattleScreen;
import pl.pollub.hirols.screens.TownScreen;

/**
 * Created by Eryk on 2016-05-15.
 */
public class EndNodeInteractionSystem extends GameMapEntitySystem {

    private ImmutableArray<Entity> selectedHeroes;
    private ImmutableArray<Entity> pathEntities;
    private ImmutableArray<Entity> players;

    private ComponentMapper<ResourceComponent> resourceMap = ComponentMapper.getFor(ResourceComponent.class);
    private ComponentMapper<HeroDataComponent> heroDataMap = ComponentMapper.getFor(HeroDataComponent.class);
    private ComponentMapper<PathComponent> pathMap = ComponentMapper.getFor(PathComponent.class);
    private ComponentMapper<PlayerDataComponent> playerDataMap = ComponentMapper.getFor(PlayerDataComponent.class);
    private ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);
    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TownComponent> townMap = ComponentMapper.getFor(TownComponent.class);
    private ComponentMapper<MineComponent> mineMap = ComponentMapper.getFor(MineComponent.class);
    private ComponentMapper<ChestComponent> chestMap = ComponentMapper.getFor(ChestComponent.class);
    private ComponentMapper<EnemyComponent> enemyMap = ComponentMapper.getFor(EnemyComponent.class);

    private final Hirols game;

    public EndNodeInteractionSystem(int priority, Class<? extends GameMapComponent> gameMapClass, Hirols game) {
        super(priority, gameMapClass);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        pathEntities = engine.getEntitiesFor(Family.all(PathComponent.class,gameMapClass).get());
        selectedHeroes = engine.getEntitiesFor(Family.all(SelectedHeroComponent.class, gameMapClass).get());
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, PlayerDataComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if(gameMapData.size() < 1 || selectedHeroes.size() < 1) return;
        GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapData.first());
        Entity selectedHero = selectedHeroes.first();
        HeroDataComponent selectedHeroData = heroDataMap.get(selectedHero);
        if(!(selectedHeroData.pathNodesPosition.isEmpty() && selectedHeroData.tempNodesPosition.isEmpty() && selectedHeroData.targetEntity != null)) return;

        Entity targetEntity = selectedHeroData.targetEntity;

        for(Entity pathEntity : pathEntities) {
            PathComponent pathComponent = pathMap.get(pathEntity);
            if(pathComponent.playerID != selectedHeroData.id) continue;

            if(resourceMap.has(targetEntity)) {
                ResourceComponent resourceComponent = resourceMap.get(targetEntity);
                //TODO current player
                playerDataMap.get(players.first()).resources.put(resourceComponent.resourceType,resourceComponent.amount);
                String resourceText = "Picked up " + resourceComponent.amount + " " + resourceComponent.resourceType + " !";
                targetEntity.remove(ResourceComponent.class);
                targetEntity.remove(RenderableComponent.class);
                targetEntity.remove(TextureComponent.class);
                selectedHeroData.targetEntity = null;
                mapMapper.get(targetEntity).walkable = true;
                PositionComponent resourcePosition = posMap.get(targetEntity);
                gameMapData.map.updateGraphConnectionsToNode(resourcePosition.x,resourcePosition.y,true);
                BitmapFont bitmapFont = game.assetManager.get("testFontSize32.ttf", BitmapFont.class);
                getEngine().addEntity(new Entity()
                        .add(new LifePeriodComponent(3000))
                        .add(new BitmapFontComponent(new BitmapFont(bitmapFont.getData(), bitmapFont.getRegion(), bitmapFont.usesIntegerPositions()), resourceText))
                        .add(new TransparencyComponent(1))
                        .add(new RenderableComponent())
                        .add(new PositionComponent(resourcePosition.x,resourcePosition.y))
                        .add(new VelocityComponent(new Vector2()))
                        .add(gameMapData.map.getGameMapComponent()));
                getEngine().removeEntity(pathEntity);
                Gdx.app.log("EndNodeInteractionSystem", "Interaction with resource: " + resourceText);
                return;
            } else if(chestMap.has(targetEntity)) {
                ChestComponent chestComponent = chestMap.get(targetEntity);
                selectedHeroData.targetEntity = null;
                Gdx.app.log("EndNodeInteractionSystem", "Interaction with chest: ");
            } else if(enemyMap.has(targetEntity)) {
                selectedHeroData.targetEntity = null;
                Gdx.app.log("EndNodeInteractionSystem", "Interaction with enemy: ");
                getEngine().removeEntity(pathEntity);
                game.setScreen(new BattleScreen(game));
            }

        }
        if(townMap.has(targetEntity)) {
            TownComponent townComponent = townMap.get(targetEntity);
            selectedHeroData.targetEntity = null;
            Gdx.app.log("EndNodeInteractionSystem", "Interaction with town: ");
            game.setScreen(new TownScreen(game));
        } else if(mineMap.has(targetEntity)) {
            Gdx.app.log("EndNodeInteractionSystem", "Interaction with mine: ");
        }

    }
}
