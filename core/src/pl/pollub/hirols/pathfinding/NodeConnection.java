package pl.pollub.hirols.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

public class NodeConnection implements Connection<Node> {
    private Node fromNode;
    private Node toNode;
    private float cost;

    public NodeConnection(float cost, Node fromNode, Node toNode) {
        this.cost = cost;
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}
