package pl.pollub.hirols.pathfinding;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MapGraph extends DefaultIndexedGraph<Node> {

    private int tileWidth, tileHeight;
    private int tileMapWidth, tileMapHeight;
    private Vector2 position = new Vector2();

    public MapGraph(Array<Node> nodes, int tileWidth, int tileHeight, int tileMapWidth, int tileMapHeight) {
        super(nodes);
        setTileWidthAndHeight(tileWidth,tileHeight);
        setTileMapWidthAndHeight(tileMapWidth,tileMapHeight);
    }

    public Node getNodeFromPosition(Vector2 position) throws IndexOutOfBoundsException {
        int nodeXIndex = ((int) (position.x / tileWidth));
        int nodeYIndex = ((int) (position.y / tileHeight));
        int nodeIndex = tileMapWidth * nodeYIndex + nodeXIndex;

        if( nodeIndex > nodes.size) {
            throw new IndexOutOfBoundsException("getNodeFromPosition exception");
        }

        return nodes.get(nodeIndex);
    }

    public Array<Node> getAdjacentNodesFromPosition(Vector2 position) {
        Node centerNode = getNodeFromPosition(position);
        return getAdjacentNodesFromNode(centerNode);
    }

    public Array<Node> getAdjacentNodesFromNode(Node centerNode) {
        int x = centerNode.getXIndex();
        int y = centerNode.getYIndex();
        Array<Node> adjacentNodes = new Array<Node>();
        for (int dx = (x > 0 ? -1 : 0); dx <= (x < tileMapWidth -1 ? 1 : 0); dx++) {
            for (int dy = (y > 0 ? -1 : 0); dy <= (y < tileMapHeight - 1 ? 1 : 0); dy++) {
                if (dx == 0 && dy == 0) continue;
                this.position.set(x*tileWidth + dx * tileWidth, y*tileHeight + dy * tileHeight);
                adjacentNodes.add(getNodeFromPosition(this.position));
            }
        }
        return adjacentNodes;
    }

    public void updateConnectionsToNode(float x, float y, boolean walkable) {
        updateConnectionsToNode(position.set(x,y),walkable);
    }

    public void updateConnectionsToNode(Vector2 position, boolean walkable) {
        Node node = getNodeFromPosition(position);

        if(walkable) {
            for(Node adjacentNode : getAdjacentNodesFromNode(node)) {
                adjacentNode.createConnection(node, GraphGenerator.getDiagonalCost(adjacentNode,node));
            }
        } else {
            for(Node adjacentNode : getAdjacentNodesFromNode(node)) {
                adjacentNode.deleteConnection(node);
            }
        }
    }

    public void setTileWidthAndHeight(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public void setTileMapWidthAndHeight(int tileMapWidth, int tileMapHeight) {
        this.tileMapWidth = tileMapWidth;
        this.tileMapHeight = tileMapHeight;
    }
}
