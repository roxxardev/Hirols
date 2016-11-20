package pl.pollub.hirols.pathfinding;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Eryk on 2016-11-14.
 */

public abstract class MapGraph extends DefaultIndexedGraph<Node> {

    protected int tileWidth, tileHeight;
    protected int tileMapWidth, tileMapHeight;
    protected Vector2 position = new Vector2();

    public MapGraph(Array<Node> nodes) {
        super(nodes);
    }

    public abstract Node getNodeFromPosition(Vector2 position);

    public abstract Array<Node> getAdjacentNodesFromPosition(Vector2 position);

    public abstract Array<Node> getAdjacentNodesFromNode(Node centerNode);

    public abstract void updateConnectionsToNode(float x, float y, boolean walkable);

    public abstract void updateConnectionsToNode(Vector2 position, boolean walkable);

    public void setTileWidthAndHeight(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public void setTileMapWidthAndHeight(int tileMapWidth, int tileMapHeight) {
        this.tileMapWidth = tileMapWidth;
        this.tileMapHeight = tileMapHeight;
    }

}
