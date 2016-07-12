package pl.pollub.hirols.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class DiagonalHeuristic implements Heuristic<Node> {

    private final static float DIAGONAL_COST = 1.414f;
    private final static float NON_DIAGONAL_COST = 1.0f;

    @Override
    public float estimate(Node node, Node endNode) {

        int startXIndex = node.getXIndex();
        int startYIndex = node.getYIndex();

        int endXIndex = endNode.getXIndex();
        int endYIndex = endNode.getYIndex();

        int deltaX = Math.abs(startXIndex - endXIndex);
        int deltaY = Math.abs(startYIndex - endYIndex);

        return NON_DIAGONAL_COST * (deltaX + deltaY) + (DIAGONAL_COST - 2*NON_DIAGONAL_COST) * Math.min(deltaX,deltaY);
    }
}
