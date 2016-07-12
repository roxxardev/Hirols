package pl.pollub.hirols.pathfinding;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Array;

import pl.pollub.hirols.components.map.MapComponent;
import pl.pollub.hirols.gameMap.Map;

public class GraphGenerator {
    private final static float DIAGONAL_COST = 1.414f;
    private final static float NON_DIAGONAL_COST = 1.0f;

    public static MapGraph generateGraph(Map map, int tileWidth, int tileHeight, int mapWidth, int mapHeight) {
        Array<Node> nodes = new Array<Node>();
        int index = 0;
        for(int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                nodes.add(new Node(index++, j, i));
            }
        }
        MapGraph mapGraph = new MapGraph(nodes, tileWidth, tileHeight, mapWidth, mapHeight);

        for(int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                createConnectionsWithNode(mapGraph,nodes,map,mapWidth,j,i);
            }
        }
        return mapGraph;
    }

    private static void createConnectionsWithNode(MapGraph mapGraph, Array<Node> nodes, Map map, int mapWidth, int indexX, int indexY) {
        ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);

        Node centerNode = nodes.get(mapWidth * indexY + indexX);
        Array<Node> adjacentNodes = mapGraph.getAdjacentNodesFromNode(centerNode);
        for(Node adjacentNode : adjacentNodes) {
            boolean adjacentWalkable = mapMapper.get(map.getEntity(adjacentNode.getXIndex(),adjacentNode.getYIndex())).walkable;
            if(adjacentWalkable) {
                centerNode.createConnection(adjacentNode, getDiagonalCost(centerNode,adjacentNode));
            }
        }
    }

    public static float getDiagonalCost(Node node, Node toNode) {
        return node.getXIndex() == toNode.getXIndex() || node.getYIndex() == toNode.getYIndex() ? NON_DIAGONAL_COST : DIAGONAL_COST;
    }
}
