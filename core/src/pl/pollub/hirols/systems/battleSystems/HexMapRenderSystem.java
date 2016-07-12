package pl.pollub.hirols.systems.battleSystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import pl.pollub.hirols.battle.HexagonMapPolygon;
import pl.pollub.hirols.components.battle.BattleComponent;
import pl.pollub.hirols.components.battle.BattleDataComponent;

/**
 * Created by Eryk on 2016-05-16.
 */
public class HexMapRenderSystem extends BattleEntitySystem {

    private ShapeRenderer shapeRenderer;
    private PolygonSpriteBatch polygonSpriteBatch;
    private SpriteBatch spriteBatch;

    public HexMapRenderSystem(int priority, Class<? extends BattleComponent> battleClass, SpriteBatch batch) {
        super(priority, battleClass);
        this.spriteBatch = batch;
        shapeRenderer = new ShapeRenderer();
        polygonSpriteBatch = new PolygonSpriteBatch();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        if(battleData.size() < 1) return;
        BattleDataComponent battleDataComponent = battleDataMapper.get(battleData.first());
        HexagonMapPolygon hexagonMapPolygon = battleDataComponent.hexagonMapPolygon;
        OrthographicCamera battleCam = battleDataComponent.battleCam;

        spriteBatch.setProjectionMatrix(battleCam.combined);
        spriteBatch.begin();
        hexagonMapPolygon.renderBackground(spriteBatch);
        spriteBatch.end();

        polygonSpriteBatch.setProjectionMatrix(battleCam.combined);
        polygonSpriteBatch.begin();
        hexagonMapPolygon.renderHexagonMap(polygonSpriteBatch);
        polygonSpriteBatch.end();

        shapeRenderer.setProjectionMatrix(battleCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0.7f, 0.2f, 1);
        hexagonMapPolygon.renderHexagonsOutline(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        shapeRenderer.dispose();
        polygonSpriteBatch.dispose();
    }
}
