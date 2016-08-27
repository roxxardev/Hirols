package pl.pollub.hirols.gameMap;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.GameMapComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.components.map.MapComponent;
import pl.pollub.hirols.components.map.MineComponent;
import pl.pollub.hirols.components.map.ResourceComponent;
import pl.pollub.hirols.components.map.TownComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.pathfinding.DiagonalHeuristic;
import pl.pollub.hirols.pathfinding.GraphGenerator;
import pl.pollub.hirols.pathfinding.MapGraph;
import pl.pollub.hirols.pathfinding.Node;
import pl.pollub.hirols.pathfinding.NodePath;

/**
 * Created by Eryk on 2016-04-23.
 */
public class Map {

    private final Hirols game;

    private final TiledMap tiledMap;
    private final int tileMapWidth, tileMapHeight;
    private final int tileWidth, tileHeight;
    private final Rectangle mapRect;
    private final MapGraph graph;
    private final IndexedAStarPathFinder<Node> pathFinder;
    private final DiagonalHeuristic diagonalHeuristic = new DiagonalHeuristic();

    private final Entity[][] entityMap;
    private final GameMapComponent gameMapComponent;

    public Map(Hirols game, TiledMap tiledMap, GameMapComponent gameMapComponent) {
        this.tiledMap = tiledMap;
        this.game = game;

        this.gameMapComponent = gameMapComponent;

        MapProperties properties = tiledMap.getProperties();
        tileMapWidth = properties.get("width", Integer.class);
        tileMapHeight = properties.get("height", Integer.class);
        tileWidth = properties.get("tilewidth", Integer.class);
        tileHeight = properties.get("tileheight", Integer.class);
        mapRect = new Rectangle(0,0,tileMapWidth*tileWidth,tileMapHeight*tileHeight);

        entityMap = new Entity[tileMapWidth][tileMapHeight];
        createEntities();
        graph = GraphGenerator.generateGraph(this,tileWidth,tileHeight,tileMapWidth,tileMapHeight);
        pathFinder = new IndexedAStarPathFinder<Node>(graph);
        loadObjects();

    }

    private void createEntities() {
        MapLayer groundLayer = tiledMap.getLayers().get(0);
        TiledMapTileLayer tileGroundLayer = (TiledMapTileLayer)groundLayer;
        for(int i=0; i<tileGroundLayer.getWidth();i++) {
            for(int j=0; j<tileGroundLayer.getHeight();j++) {
                TiledMapTileLayer.Cell cell = tileGroundLayer.getCell(i, j);
                Entity entity;
                if(cell!=null){
                    TiledMapTile tile = cell.getTile();
                    Object walkable = tile.getProperties().get("walkable");
                    if(walkable!=null) {
                        //Gdx.app.log("Tile", i+" "+j +" "+ walkable.toString());
                        entity = new Entity().add(new MapComponent(Boolean.parseBoolean(walkable.toString()))).add(new PositionComponent(i*tileWidth,j*tileHeight));
                        //game.engine.addEntity(entity);
                    } else {
                        //Gdx.app.log("Tile", i + " " + j + " no properties walkable");
                        entity = new Entity().add(new MapComponent(false)).add(new PositionComponent(i*tileWidth,j*tileHeight));
                    }
                } else {
                    entity = new Entity().add(new MapComponent(false)).add(new PositionComponent(i*tileWidth,j*tileHeight));
                }
                entity.add(gameMapComponent);
                entityMap[i][j] = entity;
            }
        }


    }

    private void loadObjects() {
        ComponentMapper<MapComponent> mapComponentMapper = ComponentMapper.getFor(MapComponent.class);

        Random random = new Random();
        MapLayer objectLayer = tiledMap.getLayers().get(1);
        MapObjects mapObjects = objectLayer.getObjects();
        for(MapObject object: mapObjects) {
            String objectName = object.getName();
            Object typ = object.getProperties().get("type");
            if(typ != null) {
                String type = typ.toString();
                Vector2 position = Pools.obtain(Vector2.class);
                position.set(Float.parseFloat(object.getProperties().get("x").toString()), Float.parseFloat(object.getProperties().get("y").toString()));
                //TODO probably dividing bug
                int x = ((int) position.x)/tileWidth;
                int y = ((int) position.y)/tileHeight;
                boolean walkable = false;
                if(type.equals("castle")) {
                    float enterPositionX = position.x;
                    float enterPositionY = position.y;
                    Vector2 enterPosition = Pools.obtain(Vector2.class);
                    if(object.getProperties().containsKey("isEnter")) {
                        walkable = Boolean.valueOf(object.getProperties().get("isEnter").toString());
                        boolean isEnter = walkable;
                        if(!isEnter) {
                            enterPositionX = Float.parseFloat(object.getProperties().get("EnterX").toString());
                            enterPositionY = mapRect.getHeight() - tileHeight - Float.parseFloat(object.getProperties().get("EnterY").toString());
                        }
                        Gdx.app.log("Castle Object", objectName + " " +position.toString() + " Enter: "+isEnter +enterPosition.set(enterPositionX,enterPositionY).toString());
                    } else {
                        Gdx.app.log("Castle Object", objectName + " " +position.toString() + " has no enter");
                    }
                    Entity base = entityMap[x][y];
                    base
                            .add(new TownComponent(enterPosition.set(enterPositionX,enterPositionY)));
                    //mapComponentMapper.get(base).walkable = walkable;
                    //graph.updateConnectionsToNode(new Vector2(position.x,position.y),walkable);
                    game.engine.addEntity(base);
                    Pools.free(enterPosition);
                } else if(type.equals("resources")) {
                    Gdx.app.log("Resource Object", objectName + " " + position.toString());
                    Entity resource = entityMap[x][y];
                    resource
                            .add(new ResourceComponent(pl.pollub.hirols.managers.enums.Resource.fromString(objectName), random.nextInt(7) + 1))
                            .add(new TextureComponent(game.assetManager.get("gold.png", Texture.class)))
                            .add(new RenderableComponent());
                    mapComponentMapper.get(resource).walkable = false;

                    graph.updateConnectionsToNode(position,false);
                    game.engine.addEntity(resource);
                } else if(type.equals("mine")) {
                    Gdx.app.log("Mine Object", objectName + " " + position.toString());
                    Entity mine = entityMap[x][y];
                    mine
                            .add(new MineComponent());
                    mapComponentMapper.get(mine).walkable = false;
                    graph.updateConnectionsToNode(position,false);
                    game.engine.addEntity(mine);
                }
                Pools.free(position);
            }
        }
    }

    public List<Entity> getAdjacentEntities(float positionX, float positionY) {
        List<Entity> adjacentEntities = new ArrayList<Entity>();

        int x = (int)Math.floor(positionX / tileWidth);
        int y = (int)Math.floor(positionY / tileHeight);
        for (int dx = (x > 0 ? -1 : 0); dx <= (x < tileMapWidth -1 ? 1 : 0); dx++) {
            for (int dy = (y > 0 ? -1 : 0); dy <= (y < tileMapHeight - 1 ? 1 : 0); dy++) {
                if (dx == 0 && dy == 0) continue;
                adjacentEntities.add(getEntity(x+dx,y+dy));
            }
        }
        return adjacentEntities;
    }

    public void updateGraphConnectionsToNode(float x, float y, boolean walkable) {
        graph.updateConnectionsToNode(x,y,walkable);
    }

    public void findPath(Vector2 startNodePos, Vector2 endNodePos, NodePath nodePath) {
        Node startNode = graph.getNodeFromPosition(startNodePos);
        Node endNode = graph.getNodeFromPosition(endNodePos);

        nodePath.clear();
        pathFinder.searchNodePath(startNode,endNode,diagonalHeuristic,nodePath);
    }

    public Entity getEntity(int indexX, int indexY) {
        return (indexX >= 0 && indexX < tileMapWidth && indexY >= 0 && indexY < tileMapHeight) ? entityMap[indexX][indexY] : null;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public GameMapComponent getGameMapComponent() {
        return gameMapComponent;
    }

    public int getTileMapWidth() {
        return tileMapWidth;
    }

    public int getTileMapHeight() {
        return tileMapHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public Rectangle getMapRect() {
        return mapRect;
    }

    public void dispose() {
        tiledMap.dispose();
    }

}
