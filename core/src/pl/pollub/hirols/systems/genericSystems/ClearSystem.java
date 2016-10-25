package pl.pollub.hirols.systems.genericSystems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Eryk on 2016-05-01.
 */
public class ClearSystem extends EntitySystem {
    public ClearSystem(int priority) {
        super(priority);
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
