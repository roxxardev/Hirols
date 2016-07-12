package pl.pollub.hirols.battle;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eryk on 2016-04-17.
 */
public class HexagonMapPolygon {
    private int mapWidth;
    private int mapHeight;
    private float h,r,b,a;
    private float hexagonSideLength;
    private Vector2 margin;
    private Sprite backgroundSprite;

    private HexagonTilePolygon[][] hexagons;

    private final Vector2 p1 = new Vector2();
    private final Vector2 p2 = new Vector2();
    private final Vector2 p3 = new Vector2();

    private final TextureRegion regionFromPixMap;
    private final EarClippingTriangulator earClippingTriangulator = new EarClippingTriangulator();

    public HexagonMapPolygon(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        hexagons = new HexagonTilePolygon[mapWidth][mapHeight];
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f,1f,1f,1f);
        pixmap.fill();
        regionFromPixMap = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();
    }

    public HexagonMapPolygon(int mapWidth, int mapHeight, float hexagonSideLength, Vector2 margin) {
        this(mapWidth, mapHeight);
        init(hexagonSideLength, margin);
    }

    private void init(Vector2 margin) {
        this.margin = margin;
        recalculate();
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                hexagons[x][y] = new HexagonTilePolygon();
                hexagons[x][y].init(this, x, y);
            }
        }
    }

    public void init(float hexagonSideLength, Vector2 margin) {
        this.hexagonSideLength = hexagonSideLength;
        init(margin);
    }

    private void recalculate() {
        h = (float) Math.sin(Math.toRadians(30)) * hexagonSideLength;
        r = (float) Math.cos(Math.toRadians(30)) * hexagonSideLength;
        b = hexagonSideLength + 2 * h;
        a = 2 * r;
    }

    public void setSideAndUpdate(float hexagonSideLength) {
        this.hexagonSideLength = hexagonSideLength;
        recalculate();
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                hexagons[x][y].update(this);
            }
        }
    }

    public void renderBackground(SpriteBatch batch) {
        backgroundSprite.draw(batch);
    }

    public void renderHexagonMap(PolygonSpriteBatch batch) {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                HexagonTilePolygon hexagon = hexagons[x][y];
                hexagon.sprite.draw(batch);
            }
        }
    }

    public void renderHexagonsOutline(ShapeRenderer shapeRenderer) {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                HexagonTilePolygon hexagonTilePolygon = hexagons[x][y];
                shapeRenderer.polygon(hexagonTilePolygon.vertices);
            }
        }
    }

    public HexagonTilePolygon getHexagonTile(int x, int y) {
        if(x >= 0 && x < getMapWidth() && y>=0 && y<getMapHeight()) {
            return hexagons[x][y];
        }
        return null;
    }


    public HexagonTilePolygon getHexagonTileFromPoint(Vector2 point) {
        float rectangleA =2*r;
        float rectangleB = h+hexagonSideLength;
        int rectX = (int)Math.floor((point.x - margin.x) / rectangleA);
        int rectY = (int)Math.floor((point.y - margin.y) / rectangleB);
        if(!(rectX >=0 && rectX<mapWidth+1 && rectY>=0 && rectY<mapHeight+1)) return null;
        float rectPositionX = rectX*rectangleA+margin.x;
        float rectPositionY = rectY*(h+hexagonSideLength)+margin.y;
        int hexX,hexY;
        if(rectY % 2 == 0) {
            p1.set(rectPositionX, rectPositionY);
            p2.set(rectPositionX + r, rectPositionY);
            p3.set(rectPositionX, rectPositionY + h);
            if(pointInTriangle(point,p1,p2,p3)) {
                hexX = rectX -1;
                hexY = rectY -1;
            } else if(pointInTriangle(point,p1.set(rectPositionX+2*r,rectPositionY),p2,p3.set(rectPositionX + 2*r,rectPositionY+h))){
                hexX = rectX;
                hexY = rectY-1;
            } else {
                hexX = rectX;
                hexY = rectY;
            }
        } else {
            p1.set(rectPositionX, rectPositionY);
            p2.set(rectPositionX + 2*r, rectPositionY);
            p3.set(rectPositionX + r, rectPositionY + h);
            if(pointInTriangle(point, p1,p2,p3)) {
                hexX = rectX;
                hexY = rectY-1;
            } else {
                if(point.x < rectPositionX + rectangleA /2) {
                    hexX = rectX-1;
                    hexY = rectY;
                } else {
                    hexX = rectX;
                    hexY = rectY;
                }
            }
        }
        return getHexagonTile(hexX,hexY);
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public float getHexagonSideLength() {
        return hexagonSideLength;
    }

    public float getH() {
        return h;
    }

    public float getR() {
        return r;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    public TextureRegion getRegionFromPixMap() {
        return regionFromPixMap;
    }

    public EarClippingTriangulator getEarClippingTriangulator() {
        return earClippingTriangulator;
    }

    public Vector2 getMargin() {
        return margin;
    }

    private float sign (Vector2 p1, Vector2 p2, Vector2 p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    private boolean pointInTriangle (Vector2 pt, Vector2 v1, Vector2 v2, Vector2 v3) {
        boolean b1, b2, b3;

        b1 = sign(pt, v1, v2) < 0.0f;
        b2 = sign(pt, v2, v3) < 0.0f;
        b3 = sign(pt, v3, v1) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }

    public void setBackgroundSprite(Sprite backgroundSprite) {
        this.backgroundSprite = backgroundSprite;
    }

    public Sprite getBackgroundSprite() {
        return backgroundSprite;
    }

    public void dispose() {
        regionFromPixMap.getTexture().dispose();
    }
}
