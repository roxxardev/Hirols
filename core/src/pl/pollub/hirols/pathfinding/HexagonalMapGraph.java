package pl.pollub.hirols.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Eryk on 2016-11-14.
 */

public class HexagonalMapGraph extends MapGraph {
    public HexagonalMapGraph(Array<Node> nodes) {
        super(nodes);
    }

    @Override
    public Node getNodeFromPosition(Vector2 position) {
        return null;
    }

    @Override
    public Array<Node> getAdjacentNodesFromPosition(Vector2 position) {
        return null;
    }

    @Override
    public Array<Node> getAdjacentNodesFromNode(Node centerNode) {
        return null;
    }

    @Override
    public void updateConnectionsToNode(float x, float y, boolean walkable) {

    }

    @Override
    public void updateConnectionsToNode(Vector2 position, boolean walkable) {

    }
}
