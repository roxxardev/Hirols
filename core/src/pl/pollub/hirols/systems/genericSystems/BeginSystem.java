package pl.pollub.hirols.systems.genericSystems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Eryk on 2016-05-01.
 */
public class BeginSystem extends EntitySystem {

    private SpriteBatch batch;

    public BeginSystem(int priority, SpriteBatch batch) {
        super(priority);
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
        //Gdx.app.log("BeginSystem", priority + " Batch begin");
        batch.begin();
    }
}
