package pl.pollub.hirols.battle;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.utils.ShortArray;

/**
 * Created by Eryk on 2016-04-17.
 */
public class HexagonTilePolygon {
    private int x, y;
    private float positionX, positionY;
    public PolygonSprite sprite;
    public float[] vertices = new float[] {};

    public HexagonTilePolygon(HexagonMapPolygon hexagonMap, int x, int y) {
        this.x = x;
        this.y = y;
        sprite = new PolygonSprite(new PolygonRegion(hexagonMap.getRegionFromPixMap(),vertices, new short[] {}));
        sprite.setColor(0,0,0,0.15f);
        update(hexagonMap);
    }

    public void update(HexagonMapPolygon hexagonMap) {
        float xPixel = x * 2 * hexagonMap.getR() + hexagonMap.getR() * (y % 2);
        float yPixel = y * (hexagonMap.getH() + hexagonMap.getHexagonSideLength());
        positionX = xPixel + hexagonMap.getMargin().x;
        positionY = yPixel + hexagonMap.getMargin().y;
        vertices = new float[] {
                positionX,positionY+hexagonMap.getH(),
                positionX+hexagonMap.getR(),positionY,
                positionX+2*hexagonMap.getR(),positionY+hexagonMap.getH(),
                positionX+2*hexagonMap.getR(),positionY+hexagonMap.getH()+hexagonMap.getHexagonSideLength(),
                positionX+hexagonMap.getR(),positionY+hexagonMap.getB(),
                positionX,positionY+hexagonMap.getH()+hexagonMap.getHexagonSideLength()};

        ShortArray triangulatedIndices = hexagonMap.getEarClippingTriangulator().computeTriangles(vertices);
        sprite.setRegion(new PolygonRegion(hexagonMap.getRegionFromPixMap(),vertices,triangulatedIndices.toArray()));
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
