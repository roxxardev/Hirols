package pl.pollub.hirols.systems.genericSystems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Eryk on 2016-05-01.
 */
public class EndSystem extends EntitySystem {

    private SpriteBatch batch;

    public EndSystem(int priority, SpriteBatch batch) {
        super(priority);
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
        //Gdx.app.log("EndSystem", priority + " Batch end");
        batch.end();
    }
}
