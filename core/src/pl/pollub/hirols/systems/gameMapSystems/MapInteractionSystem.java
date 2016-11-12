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
import com.badlogic.gdx.utils.Pools;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.ChestComponent;
import pl.pollub.hirols.components.map.EnemyComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.MapComponent;
import pl.pollub.hirols.components.map.MineComponent;
import pl.pollub.hirols.components.map.PathComponent;
import pl.pollub.hirols.components.map.ResourceComponent;
import pl.pollub.hirols.components.SelectedComponent;
import pl.pollub.hirols.components.map.TownComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.components.player.PlayerDataComponent;
import pl.pollub.hirols.gameMap.Map;
import pl.pollub.hirols.managers.enums.Direction;
import pl.pollub.hirols.managers.input.InputManager;
import pl.pollub.hirols.pathfinding.Node;

/**
 * Created by Eryk on 2016-03-04.
 */
public class MapInteractionSystem extends GameMapEntitySystem {

    private ImmutableArray<Entity> heroes;
    private ImmutableArray<Entity> selectedHeroes;
    private ImmutableArray<Entity> unselectedHeroes;

    private ImmutableArray<Entity> playerSelectedHeroes;
    private ImmutableArray<Entity> playerUnselectedHeroes;

    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<HeroDataComponent> heroDataMap = ComponentMapper.getFor(HeroDataComponent.class);
    private ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);
    private ComponentMapper<MineComponent> mineMap = ComponentMapper.getFor(MineComponent.class);
    private ComponentMapper<ResourceComponent> resourceMap = ComponentMapper.getFor(ResourceComponent.class);
    private ComponentMapper<TownComponent> townMap = ComponentMapper.getFor(TownComponent.class);
    private ComponentMapper<ChestComponent> chestMap = ComponentMapper.getFor(ChestComponent.class);
    private ComponentMapper<EnemyComponent> enemyMap = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<PlayerDataComponent> playerDataMap = ComponentMapper.getFor(PlayerDataComponent.class);

    private final Hirols game;

    private final Texture swordPathTexture;
    private final Texture crossPathTexture;

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
        selectedHeroes = engine.getEntitiesFor(Family.all(HeroDataComponent.class, SelectedComponent.class, gameMapClass).get());
        unselectedHeroes = engine.getEntitiesFor(Family.all(HeroDataComponent.class, gameMapClass).exclude(SelectedComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapDataArray.first());

        InputManager inputManager = gameMapData.inputManager;
        Vector2 longPressPosition = inputManager.getMouseCoordsYAxisUp();
        gameMapData.hud.updateLongPressImage(inputManager.getTouchDownForLongPress(), longPressPosition.x - gameMapData.hud.getLongPressImage().getWidth()/2, longPressPosition.y);

        handleTap(gameMapData);
    }

    private void handleTap(GameMapDataComponent gameMapData) {
        if (gameMapData.inputManager.getUnreadTap()) {
            gameMapData.hud.unfocusScroll();
            gameMapData.hud.hideLeftBar();

            Vector3 mouseTemp = Pools.obtain(Vector3.class).set(gameMapData.inputManager.getMouseCoords().x,gameMapData.inputManager.getMouseCoords().y,0);
            gameMapData.gameMapCam.unproject(mouseTemp);
            Vector2 mouseWorldPosition = Pools.obtain(Vector2.class);
            mouseWorldPosition.set(mouseTemp.x, mouseTemp.y);
            Pools.free(mouseTemp);

            Map gameMap = gameMapData.map;
            if (gameMap.getMapRect().contains(mouseWorldPosition)) {
                int mapIndexX = (int)Math.floor(mouseWorldPosition.x / gameMap.getTileWidth());
                int mapIndexY = (int)Math.floor(mouseWorldPosition.y / gameMap.getTileHeight());
                if (gameMapData.inputManager.getUnreadLongPress()) {
                    handleLongPress(mapIndexX, mapIndexY, mouseWorldPosition);
                } else {
                    handleTapInsideMap(mapIndexX, mapIndexY, mouseWorldPosition);
                }
            } else {
                handleTapOutsideOfMap(mouseWorldPosition);
            }
            Pools.free(mouseWorldPosition);
        }
    }

    private void handleTapInsideMap(int mapIndexX, int mapIndexY, Vector2 mousePosition) {
        Entity currentPlayer = game.gameManager.getCurrentPlayer();
        Class<? extends PlayerComponent> currentPlayerClass = game.gameManager.attachedToPlayer(currentPlayer);
        playerSelectedHeroes = game.engine.getEntitiesFor(Family.all(HeroDataComponent.class, SelectedComponent.class, currentPlayerClass, gameMapClass).get());

        if ((playerSelectedHeroes.size() > 0)) {
            handleTapForSelectedHero(playerSelectedHeroes.first(),mapIndexX,mapIndexY, mousePosition);
        } else {
            handleTapForUnselectedHero(mapIndexX, mapIndexY, mousePosition);
        }
    }

    private void handleTapForUnselectedHero(int mapIndexX, int mapIndexY, Vector2 mousePosition) {
        GameMapDataComponent gameMapData = gameMapDataMapper.get(gameMapDataArray.first());
        Map gameMap = gameMapData.map;
        Entity mapEntity = gameMap.getEntity(mapIndexX,mapIndexY);

        Class<? extends PlayerComponent> currentPlayerClass = game.gameManager.getCurrentPlayerClass();
        playerUnselectedHeroes = game.engine.getEntitiesFor(Family.all(HeroDataComponent.class, currentPlayerClass, gameMapClass).exclude(SelectedComponent.class).get());

        for (Entity unselectedHero : playerUnselectedHeroes) {
            PositionComponent unselectedHeroPosition = posMap.get(unselectedHero);
            HeroDataComponent unselectedHeroData = heroDataMap.get(unselectedHero);
            if (mapIndexX == (int)Math.floor(unselectedHeroPosition.x / gameMap.getTileWidth()) && mapIndexY == (int)Math.floor(unselectedHeroPosition.y / gameMap.getTileHeight())) {
                Gdx.app.log("MapInteractionSystem", "Tap on unselected hero id: " + unselectedHeroData.id);
                changeSelectedHero(unselectedHero);
                return;
            }
        }

        //TODO interaction with map objects when tap and no hero is selected
        Gdx.app.log("MapInteractionSystem", "Tap inside map "
                + mousePosition.toString() + " (" + mapIndexX + "," + mapIndexY + ")" + " walkable: false, no hero selected");
        if (enemyMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on enemy");
        } else if (townMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on town");
        } else if (mineMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on mine");
        } else if (resourceMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on resource");
        } else if (chestMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on chest");
        }

    }

    public void handleTapForSelectedHero(Entity selectedHero, int mapIndexX, int mapIndexY, Vector2 mousePosition) {
        HeroDataComponent selectedHeroData = heroDataMap.get(selectedHero);
        if(selectedHeroData.heroPath.hasWalkNodes()) {
            selectedHeroData.heroPath.stopFollowing(false);
            return;
        }

        if (!selectedHeroData.pathEntities.isEmpty() && isLastNodePathClicked(selectedHeroData, mapIndexX, mapIndexY)) {
            selectedHeroData.heroPath.followPath();
            return;
        }

        searchNewPathInsideMap(selectedHero,mapIndexX,mapIndexY, mousePosition);
    }

    public boolean recalculatePathForHero(Entity hero) {
        GameMapDataComponent gameMapData = gameMapDataMapper.get(gameMapDataArray.first());
        Map gameMap = gameMapData.map;
        HeroDataComponent heroData = heroDataMap.get(hero);
        if(heroData.heroPath.hasWalkNodes() || !heroData.heroPath.hasStandNodes()) return false;

        PositionComponent targetPositionComponent = posMap.get(heroData.pathEntities.get(heroData.pathEntities.size() - 1));
        Vector2 targetPosition = Pools.obtain(Vector2.class).set(targetPositionComponent.x, targetPositionComponent.y);

        int mapIndexX = (int)Math.floor(targetPosition.x / gameMap.getTileWidth());
        int mapIndexY = (int)Math.floor(targetPosition.y / gameMap.getTileHeight());
        if(mapIndexX == 0f && mapIndexY == 0f) return false;
        searchNewPathInsideMap(hero,mapIndexX,mapIndexY,targetPosition);

        Pools.free(targetPosition);
        return true;
    }

    private void searchNewPathInsideMap(Entity selectedHero, int mapIndexX, int mapIndexY, Vector2 mousePosition) {
        GameMapDataComponent gameMapData = gameMapDataMapper.get(gameMapDataArray.first());
        Map gameMap = gameMapData.map;
        Entity mapEntity = gameMap.getEntity(mapIndexX,mapIndexY);

        if (mapMapper.get(mapEntity).walkable) {
            handleWalkableForSelectedHero(selectedHero, mapIndexX, mapIndexY, mousePosition);
        } else {
            handleNonWalkableForSelectedHero(selectedHero, mapIndexX, mapIndexY, mousePosition);
        }
    }

    private boolean isLastNodePathClicked(HeroDataComponent heroData, int mapIndexX, int mapIndexY) {
        GameMapDataComponent gameMapData = gameMapDataMapper.get(gameMapDataArray.first());
        Map gameMap = gameMapData.map;
        PositionComponent endNodePosition = posMap.get(heroData.pathEntities.get(heroData.pathEntities.size() - 1));
        return ((int)Math.floor(endNodePosition.x / gameMap.getTileWidth()) == mapIndexX && (int)Math.floor(endNodePosition.y / gameMap.getTileHeight()) == mapIndexY);
    }

    private void updateGraphConnectionsForEnemy(Map gameMap, Entity mapEntity, EnemyComponent enemyComponent, boolean walkable) {
        PositionComponent enemyPosition = posMap.get(mapEntity);
        for(Entity adjacentEntity : gameMap.getAdjacentEntities(enemyPosition.x,enemyPosition.y)) {
            if(enemyMap.has(adjacentEntity)) {
                if(enemyMap.get(adjacentEntity).enemyPosition.equals(enemyMap.get(mapEntity).enemyPosition))
                    gameMap.updateGraphConnectionsToNode(posMap.get(adjacentEntity).x,posMap.get(adjacentEntity).y,walkable);
            }
        }
        gameMap.updateGraphConnectionsToNode(enemyComponent.enemyPosition.x, enemyComponent.enemyPosition.y,walkable);
    }

    private void handleNonWalkableForSelectedHero(Entity selectedHero, int mapIndexX, int mapIndexY, Vector2 mousePosition) {
        HeroDataComponent selectedHeroData = heroDataMap.get(selectedHero);
        PositionComponent selectedHeroPosition = posMap.get(selectedHero);
        GameMapDataComponent gameMapData = gameMapDataMapper.get(gameMapDataArray.first());
        Map gameMap = gameMapData.map;
        Entity mapEntity = gameMap.getEntity(mapIndexX,mapIndexY);

        Gdx.app.log("MapInteractionSystem", "Tap inside map "
                + mousePosition.toString() + " (" + mapIndexX + "," + mapIndexY + ")" + " walkable: false, selected hero id: " + selectedHeroData.id);
        //TODO map object action when tap and any player is selected
        if (enemyMap.has(mapEntity)) {
            EnemyComponent enemyComponent = enemyMap.get(mapEntity);
            if (enemyComponent.trueEntity) {
                Gdx.app.log("MapInteractionSystem", "Tap on enemy");
                if(findPathToEnemy(pathStartPosition.set(selectedHeroPosition.x,selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, mapEntity, swordPathTexture, enemyComponent)) {
                    Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                            + selectedHeroData.id + " Length: " + selectedHeroData.heroPath.getPathSize() + " to enemy");
                }
            } else {
                Gdx.app.log("MapInteractionSystem", "Tap nearby enemy");
                if(findPathNonWalkable(pathStartPosition.set(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, mapEntity, swordPathTexture, false)) {
                    Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                            + selectedHeroData.id + " Length: " + selectedHeroData.heroPath.getPathSize() + " nearby enemy");
                }
            }
        } else if (townMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on town");
            TownComponent townComponent = townMap.get(mapEntity);
            PositionComponent townEnterPosition = posMap.get(townComponent.enterEntity);
            Vector2 townPos = Pools.obtain(Vector2.class).set(townEnterPosition.x,townEnterPosition.y);
            if(findPathNonWalkable(pathStartPosition.set(selectedHeroPosition.x, selectedHeroPosition.y), townPos, gameMap, selectedHeroData, mapEntity, crossPathTexture, true)) {
                Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                        + selectedHeroData.id + " Length: " + selectedHeroData.heroPath.getPathSize() + " to town");
            }
        } else if (mineMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on mine");
            Entity mineEnterEntity = mineMap.get(mapEntity).enterEntity;
            PositionComponent mineEnterPosition = posMap.get(mineEnterEntity);
            Vector2 minePos = Pools.obtain(Vector2.class).set(mineEnterPosition.x, mineEnterPosition.y);
            if(findPathNonWalkable(pathStartPosition.set(selectedHeroPosition.x, selectedHeroPosition.y), minePos, gameMap, selectedHeroData, mineEnterEntity, crossPathTexture, true)) {
                Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                        + selectedHeroData.id + " Length: " + selectedHeroData.heroPath.getPathSize() + " to mine");
            }
            Pools.free(minePos);

        } else if (resourceMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on resource");
            if (findPathNonWalkable(pathStartPosition.set(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, mapEntity, crossPathTexture, false)) {
                Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                        + selectedHeroData.id + " Length: " + selectedHeroData.heroPath.getPathSize() + " to resource");
            }
        } else if (chestMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Tap on chest");
            if(findPathNonWalkable(pathStartPosition.set(selectedHeroPosition.x,selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, mapEntity, crossPathTexture, false)) {
                Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                        + selectedHeroData.id + " Length: " + selectedHeroData.heroPath.getPathSize() + " to chest");
            }
        }
    }

    private void handleWalkableForSelectedHero(Entity selectedHero, int mapIndexX, int mapIndexY, Vector2 mousePosition) {
        HeroDataComponent selectedHeroData = heroDataMap.get(selectedHero);
        PositionComponent selectedHeroPosition = posMap.get(selectedHero);
        GameMapDataComponent gameMapData = gameMapDataMapper.get(gameMapDataArray.first());
        Map gameMap = gameMapData.map;

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
                changeSelectedHero(unselectedHero);
                return;
            }
        }

        if (findPath(pathStartPosition.set(selectedHeroPosition.x, selectedHeroPosition.y), mousePosition, gameMap, selectedHeroData, crossPathTexture))
            Gdx.app.log("MapInteractionSystem", "Path created for hero id: "
                    + selectedHeroData.id + " Length: " + selectedHeroData.heroPath.getPathSize());

    }

    private void handleLongPress(int mapIndexX, int mapIndexY, Vector2 mousePosition) {
        GameMapDataComponent gameMapData = gameMapDataMapper.get(gameMapDataArray.first());
        Map gameMap = gameMapData.map;
        Entity mapEntity = gameMap.getEntity(mapIndexX,mapIndexY);

        Gdx.app.log("MapInteractionSystem", "Long press inside map " + mousePosition.toString() + " (" + mapIndexX + "," + mapIndexY + ")");
        for (Entity hero : heroes) { //TODO action for long press on hero
            HeroDataComponent heroData = heroDataMap.get(hero);
            PositionComponent heroPosition = posMap.get(hero);
            if (mapIndexX == (int)Math.floor(heroPosition.x / gameMap.getTileWidth()) && mapIndexY == (int)Math.floor(heroPosition.y / gameMap.getTileHeight())) {
                Gdx.app.log("MapInteractionSystem", "Long press on hero id: " + heroData.id);
                return;
            }
        }
        //TODO map object action when long press
        if (enemyMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on enemy");
        } else if (townMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on town");
        } else if (mineMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on mine");
        } else if (resourceMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on resource");
        } else if (chestMap.has(mapEntity)) {
            Gdx.app.log("MapInteractionSystem", "Long Press on chest");
        }
    }

    public boolean deselectHero(Class<? extends PlayerComponent> playerClass) {
        ImmutableArray<Entity> selected = game.engine.getEntitiesFor(Family.all(playerClass, SelectedComponent.class, HeroDataComponent.class, gameMapClass).get());
        if(selected.size() == 0) return true;
        if(heroDataMap.get(selected.first()).heroPath.hasWalkNodes()) return false;

        //TODO deselect everything that belongs to selected hero

        selected.first().remove(SelectedComponent.class);
        return true;
    }

    public boolean changeSelectedHero(Entity hero) {
        //TODO player hero
        if(!deselectHero(game.gameManager.getCurrentPlayerClass())) return false;

        hero.add(game.engine.createComponent(SelectedComponent.class));
        recalculatePathForHero(hero);
        PositionComponent heroPosition = posMap.get(hero);
        gameMapDataMapper.get(gameMapDataArray.first()).gameMapCam.position.set(heroPosition.x,heroPosition.y,0);
        return true;
    }

    private void handleTapOutsideOfMap(Vector2 mousePosition) {
        Gdx.app.log("MapInteractionSystem", "Tap outside map of " + mousePosition.toString());
    }

    public boolean findPathToEnemy(Vector2 startNodePos, Vector2 endNodePos, Map gameMap, HeroDataComponent heroData, Entity mapEntity, Texture lastPathTexture, EnemyComponent enemyComponent) {
        boolean success = false;
        updateGraphConnectionsForEnemy(gameMap,mapEntity,enemyComponent,true);
        if (findPath(startNodePos, endNodePos, gameMap,heroData, lastPathTexture)) {
            success = true;
            heroData.heroPath.getStand().removeLastElement();
            heroData.heroPath.setTargetEntity(mapEntity);
        }
        updateGraphConnectionsForEnemy(gameMap,mapEntity,enemyComponent,false);
        return success;
    }

    public boolean findPathNonWalkable(Vector2 startNodePos, Vector2 endNodePos, Map gameMap, HeroDataComponent heroData, Entity mapEntity, Texture lastPathTexture, boolean targetEnter) {
        boolean success = false;
        gameMap.updateGraphConnectionsToNode(endNodePos.x, endNodePos.y, true);
        if (findPath(startNodePos, endNodePos, gameMap, heroData, lastPathTexture)) {
            success = true;
            if(!targetEnter) heroData.heroPath.getStand().removeLastElement();
            heroData.heroPath.setTargetEntity(mapEntity);
        }
        gameMap.updateGraphConnectionsToNode(endNodePos.x, endNodePos.y, false);
        return success;
    }

    public void resetHeroPath(HeroDataComponent heroData, boolean resetNodePath) {
        heroData.heroPath.reset(resetNodePath);
        for(Entity path : heroData.pathEntities) {
            game.engine.removeEntity(path);
        }
        heroData.pathEntities.clear();
    }

    public boolean findPath(Vector2 startNodePos, Vector2 endNodePos, Map gameMap, HeroDataComponent heroData, Texture lastPathTexture) {
        if(heroData.heroPath.hasWalkNodes()) throw new IllegalArgumentException("Hero must stand to find new Path!");

        gameMap.findPath(startNodePos,endNodePos,heroData.heroPath.getPath());
        if(heroData.heroPath.getPathSize() < 1)  return false;

        resetHeroPath(heroData, false);

        if (heroData.heroPath.getPathSize() > 1) {
            float heroMovementPoints = heroData.movementPoints;
            for (int i = 1; i < heroData.heroPath.getPathSize(); i++) {
                Node node = heroData.heroPath.getPath().get(i);
                int nodeX = node.getXIndex();
                int nodeY = node.getYIndex();

                Sprite tempSprite = new Sprite(lastPathTexture);
                tempSprite.setSize(gameMap.getTileWidth(), gameMap.getTileHeight());
                tempSprite.setColor(Color.GREEN);

                Node previousNode = heroData.heroPath.getPath().get(i-1);
                int previousNodeX = previousNode.getXIndex();
                int previousNodeY = previousNode.getYIndex();

                float movementCost = 0;

                Direction direction = null;

                if (previousNodeX == nodeX) {
                    movementCost = 1f;
                    if (previousNodeY < nodeY) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_N.png", Texture.class)); //góra
                        direction = Direction.N;
                    } else if (previousNodeY > nodeY) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_S.png", Texture.class)); //dół
                        direction = Direction.S;
                    }
                } else if (previousNodeY == nodeY) {
                    movementCost = 1f;
                    if (previousNodeX > nodeX) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_W.png", Texture.class)); //lewo
                        direction = Direction.W;
                    }
                    else if (previousNodeX < nodeX) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_E.png", Texture.class)); //prawo
                        direction = Direction.E;
                    }
                } else if (previousNodeY < nodeY) {
                    movementCost = 1.41421356f;
                    if (previousNodeX < nodeX) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_NE.png", Texture.class)); //góraprawo
                        direction = Direction.NE;
                    }
                    else if (previousNodeX > nodeX) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_NW.png", Texture.class)); //góralewo
                        direction = Direction.NW;
                    }
                } else if (previousNodeY > nodeY) {
                    movementCost = 1.41421356f;
                    if (previousNodeX < nodeX) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_SE.png", Texture.class)); //dółprawo
                        direction = Direction.SE;
                    }
                    else if (previousNodeX > nodeX) {
                        tempSprite.setTexture(game.assetManager.get("arrows/arrow_SW.png", Texture.class)); //dółlewo
                        direction = Direction.SW;
                    }
                }
                heroMovementPoints -= movementCost;
                if (heroMovementPoints < 0) {
                    tempSprite.setColor(Color.RED);
                }

                Vector2 position = Pools.obtain(Vector2.class).set(nodeX * gameMap.getTileWidth(), nodeY * gameMap.getTileHeight());
                heroData.heroPath.getStand().addElement(position.x,position.y, movementCost);
                Entity pathEntity = game.engine.createEntity();

                pathEntity
                        .add(game.engine.createComponent(PositionComponent.class).init(position))
                        .add(game.engine.createComponent(RenderableComponent.class))
                        .add(game.engine.createComponent(TextureComponent.class).setSprite(tempSprite))
                        .add(game.engine.createComponent(gameMap.getGameMapComponentClazz()))
                        .add(game.engine.createComponent(PathComponent.class).init(heroData.id,direction));

                if (i == (heroData.heroPath.getPathSize() - 1)) {
                    tempSprite.setTexture(lastPathTexture);
                }

                Pools.free(position);
                getEngine().addEntity(pathEntity);
                heroData.pathEntities.add(pathEntity);
            }
        } else return false;

        return true;
    }

    public ImmutableArray<Entity> getSelectedHeroes() {
        return selectedHeroes;
    }
}
