package pl.pollub.hirols.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class DiagonalHeuristic implements Heuristic<Node> {

    @Override
    public float estimate(Node node, Node endNode) {

        int startXIndex = node.getXIndex();
        int startYIndex = node.getYIndex();

        int endXIndex = endNode.getXIndex();
        int endYIndex = endNode.getYIndex();

        int deltaX = Math.abs(startXIndex - endXIndex);
        int deltaY = Math.abs(startYIndex - endYIndex);

        return GraphGenerator.NON_DIAGONAL_COST * (deltaX + deltaY) + (GraphGenerator.DIAGONAL_COST - 2*GraphGenerator.NON_DIAGONAL_COST) * Math.min(deltaX,deltaY);
    }
}
