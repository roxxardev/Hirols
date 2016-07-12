package pl.pollub.hirols.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

public class Node implements IndexedNode<Node> {

    private Array<Connection<Node>> connections;
    private int index;
    private int indexX, indexY;

    public Node(int index, int indexX, int indexY) {
        this.index = index;
        this.indexX = indexX;
        this.indexY = indexY;
        connections = new Array<Connection<Node>>();
    }

    public boolean createConnection(Node toNode, float cost) {
        for(Connection<Node> nodeConnection : connections) {
            if(toNode == nodeConnection.getToNode()) {
                return false;
            }
        }
        connections.add(new NodeConnection(cost,this,toNode));
        return true;
    }

    public boolean deleteConnection(Node toNode) {
        for(Connection<Node> nodeConnection : connections) {
            if(toNode == nodeConnection.getToNode()) {
                connections.removeValue(nodeConnection,true);
                return true;
            }
        }
        return false;
    }

    public int getXIndex() {
        return indexX;
    }

    public int getYIndex() {
        return indexY;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Array<Connection<Node>> getConnections() {
        return connections;
    }
}
