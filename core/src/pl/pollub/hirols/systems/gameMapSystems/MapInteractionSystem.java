package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.ChestComponent;
import pl.pollub.hirols.components.map.EnemyComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.MapComponent;
import pl.pollub.hirols.components.map.MineComponent;
import pl.pollub.hirols.components.map.PathComponent;
import pl.pollub.hirols.components.map.ResourceComponent;
import pl.pollub.hirols.components.map.SelectedHeroComponent;
import pl.pollub.hirols.components.map.TownComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.gameMap.Map;
import pl.pollub.hirols.managers.input.InputManager;
import pl.pollub.hirols.pathfinding.Node;

/**
 * Created by Eryk on 2016-03-04.
 */
public class MapInteractionSystem extends GameMapEntitySystem {

    private ImmutableArray<Entity> heroes;
    private ImmutableArray<Entity> pathEntities;
    private ImmutableArray<Entity> selectedHeroes;
    private ImmutableArray<Entity> unselectedHeroes;

    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<HeroDataComponent> heroDataMap = ComponentMapper.getFor(HeroDataComponent.class);
    private ComponentMapper<PathComponent> pathMap = ComponentMapper.getFor(PathComponent.class);
    private ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);
    private ComponentMapper<MineComponent> mineMap = ComponentMapper.getFor(MineComponent.class);
    private ComponentMapper<ResourceComponent> resourceMap = ComponentMapper.getFor(ResourceComponent.class);
    private ComponentMapper<TownComponent> townMap = ComponentMapper.getFor(TownComponent.class);
    private ComponentMapper<ChestComponent> chestMap = ComponentMapper.getFor(ChestComponent.class);
    private ComponentMapper<EnemyComponent> enemyMap = ComponentMapper.getFor(EnemyComponent.class);

    private final Hirols game;

    private final Texture swordPathTexture;
    private final Texture crossPathTexture;

    private final Vector3 mousePos = new Vector3();
    private final Vector2 mousePosition = new Vector2();
    private final Vector2 pathStartPosition = new Vector2();

    public MapInteractionSystem(int priority, Hirols game, Class<? extends GameMapComponent> gameMapClass) {
        super(priority, gameMapClass);
        this.game = game;
        crossPathTexture = game.assetManager.get("arrows/cross.png", Texture.class);
        swordPathTexture = game.assetManager.get("arrows/sword.png", Texture.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        heroes = engine.getEntitiesFor(Family.all(HeroDataComponent.class, gameMapClass).get());
        pathEntities = engine.getEntitiesFor(Family.all(PathComponent.class, gameMapClass).get());
        selectedHeroes = engine.getEntitiesFor(Family.all(SelectedHeroComponent.class, gameMapClass).get());
        unselectedHeroes = engine.getEntitiesFor(Family.all(HeroDataComponent.class, gameMapClass).exclude(SelectedHeroComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if(gameMapData.size() < 1) return;
        GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapData.first());

        InputManager inputManager = gameMapData.inputManager;
        Vector2 longPressPosition = inputManager.getMouseCoordsYAxisUp();
        gameMapData.hud.updateLongPressImage(inputManager.getTouchDownForLongPress(), longPressPosition.x - gameMapData.hud.getLongPressImage().getWidth()/2, longPressPosition.y);

        handleTap(gameMapData);
    }

    private void handleTap(GameMapDataComponent gameMapData) {
        if (gameMapData.inputManager.getUnreadTap()) {
            gameMapData.hud.unfocusScroll();
            gameMapData.hud.hideLeftBar();
            mousePos.set(gameMapData.inputManager.getMouseCoords().x,gameMapData.inputManager.getMouseCoords().y,0);
            gameMapData.gameMapCam.unproject(mousePos);
            mousePosition.set(mousePos.x, mousePos.y);
            Map gameMap = gameMapData.map;
            if (gameMap.getMapRect().contains(mousePosition)) {
                int mapIndexX = (int)Math.floor(mousePosition.x / gameMap.getTileWidth());
                int mapIndexY = (int)Math.floor(mousePosition.y / gameMap.getTileHeight());
                Entity mapEntity = gameMap.getEntity(mapIndexX,mapIndexY);
                if (gameMapData.inputManager.getUnreadLongPress()) {
                    handleLongPress(mapEntity, gameMap,mapIndexX, mapIndexY);
                } else {
                    handleTapInsideMap(mapEntity, gameMapData, mapIndexX, mapIndexY);
                }
            } else {
                handleTapOutsideOfMap();
            }
        }
    }

    private void handleTapInsideMap(Entity mapEntity, GameMapDataComponent gameMapData, int mapIndexX, int mapIndexY) {
        if ((selectedHeroes.size() > 0)) {
            Entity selectedHero = selectedHeroes.first();
            HeroDataComponent selectedHeroData = heroDataMap.get(selectedHero);
            PositionComponent selectedHeroPosition = posMap.get(selectedHero);
            handleTapForSelectedHero(mapEntity,gameMapData.map,selectedHeroData,selectedHeroPosition,mapIndexX,mapIndexY);
        } else {
            handleTapForUnselectedHero(mapEntity, mapIndexX, mapIndexY);
        }
    }

    private void handleTapForUnselectedHero(Entity mapEntity, int mapIndexX, int mapIndexY) {
        //TODO interaction with map objects when tap and no player is selected
        Gdx.app.log("MapInteractionSystem", "Tap inside map "
                + mousePosition.toString() + " (" + mapIndexX + "," + mapIndexY + ")" + " walkable: false, no hero selected");
        if (enemyMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on enemy");
        } else if (townMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on town");
            TownComponent townComponent = townMap.get(mapEntity);
        } else if (mineMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on mine");
        } else if (resourceMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on resource");
        } else if (chestMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on chest");
        }

    }

    private void handleTapForSelectedHero(Entity mapEntity, Map gameMap, HeroDataComponent selectedHeroData, PositionComponent selectedHeroPosition, int mapIndexX, int mapIndexY) {
        //TODO rename this shit, bo za miesiac zapomne co tu sie dzieje
        if (selectedHeroData.pathNodesPosition.isEmpty()) {
            if (!selectedHeroData.tempNodesPosition.isEmpty()) {
                Vector2 endNodePosition = selectedHeroData.endPathTargetPosition;
                if ((int)Math.floor(endNodePosition.x / gameMap.getTileWidth()) == mapIndexX && (int)Math.floor(endNodePosition.y / gameMap.getTileHeight()) == mapIndexY) {
                    selectedHeroData.pathNodesPosition = selectedHeroData.tempNodesPosition;
                    selectedHeroData.tempNodesPosition = new ArrayList<Vector3>();
                    return;
                }
            }
            if (mapMapper.get(mapEntity).walkable) {
                handleWalkableForSelectedHero(gameMap,selectedHeroData, selectedHeroPosition, mapIndexX, mapIndexY);
            } else {
                handleNonWalkableForSelectedHero(gameMap,selectedHeroData, selectedHeroPosition, mapIndexX, mapIndexY, mapEntity);
            }
        } else {
            if (selectedHeroData.pathNodesPosition.size() > 1) {
                selectedHeroData.tempNodesPosition = selectedHeroData.pathNodesPosition;
                selectedHeroData.pathNodesPosition = new ArrayList<Vector3>();
                selectedHeroData.pathNodesPosition.add(selectedHeroData.tempNodesPosition.get(0));
                selectedHeroData.tempNodesPosition.remove(0);
            }
        }

    }

    private void updateGraphConnectionsForEnemy(Map gameMap, Entity mapEntity, EnemyComponent enemyComponent, boolean walkable) {
        PositionComponent enemyPosition = posMap.get(mapEntity);
        for(Entity adjacentEntity : gameMap.getAdjacentEntities(enemyPosition.x,enemyPosition.y)) {
            if(enemyMap.has(adjacentEntity)) {
                if(enemyMap.get(adjacentEntity).id == enemyMap.get(mapEntity).id)
                    gameMap.updateGraphConnectionsToNode(posMap.get(adjacentEntity).x,posMap.get(adjacentEntity).y,walkable);
            }
        }
        gameMap.updateGraphConnectionsToNode(enemyComponent.enemyTargetPositionX,enemyComponent.enemyTargetPositionY,walkable);
    }

    private void handleNonWalkableForSelectedHero(Map gameMap, HeroDataComponent selectedHeroData, PositionComponent selectedHeroPosition, int mapIndexX, int mapIndexY, Entity mapEntity) {
        Gdx.app.log("MapInteractionSystem", "Tap inside map "
                + mousePosition.toString() + " (" + mapIndexX + "," + mapIndexY + ")" + " walkable: false, selected hero id: " + selectedHeroData.id);
        PositionComponent mapEntityPosition = posMap.get(mapEntity);
        //TODO map object action when tap and any player is selected
        if (enemyMap.has(mapEntity)) {
            EnemyComponent enemyComponent = enemyMap.get(mapEntity);
            if (enemyComponent.trueEntity) {
                Gdx.app.log("MapInteractionSystem", "Tap on enemy id: " + enemyComponent.id);

                updateGraphConnectionsForEnemy(gameMap,mapEntity,enemyComponent,true);
                if (findPath(new Vector2(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap,selectedHeroData, swordPathTexture)) {
                    Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                            + selectedHeroData.id + " Length: " + selectedHeroData.path.getCount() + " to enemy id: " + enemyComponent.id);
                    selectedHeroData.tempNodesPosition.remove(selectedHeroData.tempNodesPosition.size() - 1);
                    selectedHeroData.targetEntity = mapEntity;
                }
                updateGraphConnectionsForEnemy(gameMap,mapEntity,enemyComponent,false);

            } else {
                Gdx.app.log("MapInteractionSystem", "Tap nearby enemy id: " + enemyComponent.id);
                PositionComponent enemyPosition = posMap.get(mapEntity);
                gameMap.updateGraphConnectionsToNode(enemyPosition.x,enemyPosition.y,true);

                if (findPath(new Vector2(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, swordPathTexture)) {
                    Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                            + selectedHeroData.id + " Length: " + selectedHeroData.path.getCount() + " nearby enemy id: " + enemyComponent.id);
                    selectedHeroData.tempNodesPosition.remove(selectedHeroData.tempNodesPosition.size() - 1);
                    selectedHeroData.targetEntity = mapEntity;
                }

                gameMap.updateGraphConnectionsToNode(enemyPosition.x,enemyPosition.y,false);
            }
        } else if (townMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on town");
            TownComponent townComponent = townMap.get(mapEntity);
            gameMap.updateGraphConnectionsToNode(townComponent.enterPosition.x, townComponent.enterPosition.y, true);
            if (findPath(new Vector2(selectedHeroPosition.x, selectedHeroPosition.y), townComponent.enterPosition, gameMap, selectedHeroData, crossPathTexture)) {
                Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                        + selectedHeroData.id + " Length: " + selectedHeroData.path.getCount() + " to town");
                selectedHeroData.targetEntity = mapEntity;
            }
            gameMap.updateGraphConnectionsToNode(townComponent.enterPosition.x, townComponent.enterPosition.y, false);
        } else if (mineMap.has(mapEntity)) {
            //TODO czekaj na mape
            Gdx.app.log("MapInteractionSystem", "Tap on mine");
        } else if (resourceMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on resource");
            gameMap.updateGraphConnectionsToNode(mousePosition.x,mousePosition.y,true);
            if (findPath(new Vector2(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, crossPathTexture)) {
                Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                        + selectedHeroData.id + " Length: " + selectedHeroData.path.getCount() + " to resource");
                selectedHeroData.tempNodesPosition.remove(selectedHeroData.tempNodesPosition.size() - 1);
                selectedHeroData.targetEntity = mapEntity;
            }
            gameMap.updateGraphConnectionsToNode(mousePosition.x,mousePosition.y,false);
        } else if (chestMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on chest");
            gameMap.updateGraphConnectionsToNode(mapEntityPosition.x,mapEntityPosition.y,true);
            if (findPath(new Vector2(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, crossPathTexture)) {
                Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                        + selectedHeroData.id + " Length: " + selectedHeroData.path.getCount() + " to chest");
                selectedHeroData.tempNodesPosition.remove(selectedHeroData.tempNodesPosition.size() - 1);
                selectedHeroData.targetEntity = mapEntity;
            }
            gameMap.updateGraphConnectionsToNode(mapEntityPosition.x,mapEntityPosition.y,false);
        }
    }

    private void handleWalkableForSelectedHero(Map gameMap, HeroDataComponent selectedHeroData, PositionComponent selectedHeroPosition, int mapIndexX, int mapIndexY) {

        Gdx.app.log("MapInteractionSystem", "Tap inside map " + mousePosition.toString()
                + " (" + mapIndexX + "," + mapIndexY + ")" + " walkable: true, selected hero id: " + selectedHeroData.id);

        if (mapIndexX == (int)Math.floor(selectedHeroPosition.x / gameMap.getTileWidth()) && mapIndexY == (int)Math.floor(selectedHeroPosition.y / gameMap.getTileHeight())) {
            Gdx.app.log("MapInteractionSystem", "Tap on selected hero id: " + selectedHeroData.id);
            return;
        }

        for (Entity unselectedHero : unselectedHeroes) {
            PositionComponent unselectedHeroPosition = posMap.get(unselectedHero);
            HeroDataComponent unselectedHeroData = heroDataMap.get(unselectedHero);
            if (mapIndexX == (int)Math.floor(unselectedHeroPosition.x / gameMap.getTileWidth()) && mapIndexY == (int)Math.floor(unselectedHeroPosition.y / gameMap.getTileHeight())) {
                Gdx.app.log("MapInteractionSystem", "Tap on unselected hero id: " + unselectedHeroData.id);
                return;
            }
        }

        if (findPath(pathStartPosition.set(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, crossPathTexture))
            Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                    + selectedHeroData.id + " Length: " + selectedHeroData.path.getCount());

    }

    private void handleLongPress(Entity mapEntity, Map gameMap, int mapIndexX, int mapIndexY) {
        Gdx.app.log("MapInteractionSystem", "Long press inside map " + mousePosition.toString() + " (" + mapIndexX + "," + mapIndexY + ")");
        for (Entity hero : heroes) { //TODO action for long press on hero
            HeroDataComponent heroData = heroDataMap.get(hero);
            PositionComponent heroPosition = posMap.get(hero);
            if (mapIndexX == (int)Math.floor(heroPosition.x / gameMap.getTileWidth()) && mapIndexY == (int)Math.floor(heroPosition.y / gameMap.getTileHeight())) {
                Gdx.app.log("MapInteractionSystem", "Long press on hero id: " + heroData.id);
                break;
            }
        }
        //TODO map object action when long press
        if (enemyMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on enemy");
        } else if (townMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on town");
            TownComponent townComponent = townMap.get(mapEntity);
        } else if (mineMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on mine");
        } else if (resourceMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on resource");
        } else if (chestMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on chest");
        }
    }

    private void handleTapOutsideOfMap() {
        Gdx.app.log("MapInteractionSystem", "Tap outside map of " + mousePosition.toString());
    }

    private boolean findPath(Vector2 startNodePos, Vector2 endNodePos, Map gameMap, HeroDataComponent heroData, Texture lastPathTexture) {
        heroData.targetEntity = null;
        gameMap.findPath(startNodePos,endNodePos,heroData.path);
        if(heroData.path.getCount() > 0) {
            for(Entity pathEntity : pathEntities) {
                if(pathMap.get(pathEntity).playerID == heroData.id) {
                    getEngine().removeEntity(pathEntity);
                }
            }
            heroData.tempNodesPosition.clear();
        } else return false;

        if (heroData.path.getCount() > 1) {
            float heroMovementPoints = heroData.movementPoints;
            for (int i = 1; i < heroData.path.getCount(); i++) {
                Node node = heroData.path.get(i);
                int nodeX = node.getXIndex();
                int nodeY = node.getYIndex();

                Sprite temp = new Sprite(lastPathTexture);
                temp.setSize(96, 96);
                temp.setColor(Color.GREEN);

                Node previousNode = heroData.path.get(i-1);
                int previousNodeX = previousNode.getXIndex();
                int previousNodeY = previousNode.getYIndex();

                float movementCost = 0;
                if (previousNodeX == nodeX) {
                    movementCost = 1f;
                    if (previousNodeY < nodeY) {
                        temp.setTexture(game.assetManager.get("arrows/arrow_N.png", Texture.class)); //góra
                    } else if (previousNodeY > nodeY) {
                        temp.setTexture(game.assetManager.get("arrows/arrow_S.png", Texture.class)); //dół
                    }
                } else if (previousNodeY == nodeY) {
                    movementCost = 1f;
                    if (previousNodeX > nodeX)
                        temp.setTexture(game.assetManager.get("arrows/arrow_W.png", Texture.class)); //lewo
                    else if (previousNodeX < nodeX)
                        temp.setTexture(game.assetManager.get("arrows/arrow_E.png", Texture.class)); //prawo
                } else if (previousNodeY < nodeY) {
                    movementCost = 1.41421356f;
                    if (previousNodeX < nodeX)
                        temp.setTexture(game.assetManager.get("arrows/arrow_NE.png", Texture.class)); //góraprawo
                    else if (previousNodeX > nodeX)
                        temp.setTexture(game.assetManager.get("arrows/arrow_NW.png", Texture.class)); //góralewo
                } else if (previousNodeY > nodeY) {
                    movementCost = 1.41421356f;
                    if (previousNodeX < nodeX)
                        temp.setTexture(game.assetManager.get("arrows/arrow_SE.png", Texture.class)); //dółprawo
                    else if (previousNodeX > nodeX)
                        temp.setTexture(game.assetManager.get("arrows/arrow_SW.png", Texture.class)); //dółlewo
                }
                heroMovementPoints -= movementCost;
                if (heroMovementPoints < 0) {
                    temp.setColor(Color.RED);
                }

                heroData.tempNodesPosition.add(new Vector3(nodeX * gameMap.getTileWidth(), nodeY * gameMap.getTileHeight(), movementCost));
                Entity entity = new Entity();

                entity
                        .add(new PositionComponent(nodeX * gameMap.getTileWidth(), nodeY * gameMap.getTileHeight()))
                        .add(new RenderableComponent())
                        .add(new TextureComponent(temp))
                        .add(gameMap.getGameMapComponent())
                        .add(new PathComponent(heroData.id));

                if (i == (heroData.path.getCount() - 1)) {
                    temp.setTexture(lastPathTexture);
                    heroData.endPathTargetPosition = new Vector2(nodeX * gameMap.getTileWidth(), nodeY * gameMap.getTileHeight());
                }

                getEngine().addEntity(entity);
            }
        } else return false;

        return true;
    }

}
