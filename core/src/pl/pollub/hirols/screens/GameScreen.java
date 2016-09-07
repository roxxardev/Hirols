package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.systems.TempSystem;

/**
 * Created by Eryk on 2016-05-03.
 */
public abstract class GameScreen implements Screen, Disposable {

    protected final Hirols game;

    protected ArrayList<EntitySystem> systems = new ArrayList<EntitySystem>();

    public GameScreen(Hirols game) {
        this.game = game;
    }

    protected abstract void createSystems();

    protected void addSystemsToEngine() {
        for(EntitySystem entitySystem : systems) {
            game.engine.addSystem(entitySystem);
        }
    }

    protected void removeSystemsFromEngine() {
        for(EntitySystem entitySystem : systems) {
            game.engine.removeSystem(entitySystem);
        }
    }

    public void setSystemsProcessing(boolean processing) {
        for(EntitySystem entitySystem : systems) {
            entitySystem.setProcessing(processing);
        }
    }

    @Override
    public void show() {
        addSystemsToEngine();
        setSystemsProcessing(true);
    }

    @Override
    public void hide() {
        setSystemsProcessing(false);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("ScreenSize", String.valueOf(Gdx.graphics.getWidth()) + " " + String.valueOf(Gdx.graphics.getHeight()));
    }

    @Override
    public void dispose() {
        removeSystemsFromEngine();
        //TODO wait for ashley to fix bug and delete
        game.engine.addSystem(new TempSystem());
    }
}
