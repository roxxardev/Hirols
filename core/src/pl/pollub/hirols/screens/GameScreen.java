package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;

/**
 * Created by Eryk on 2016-05-03.
 */
public abstract class GameScreen implements Screen {

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
        setSystemsProcessing(true);
        addSystemsToEngine();
    }

    @Override
    public void hide() {
        setSystemsProcessing(false);
        removeSystemsFromEngine();
    }

    public void dispose() {
        removeSystemsFromEngine();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("ScreenSize", String.valueOf(Gdx.graphics.getWidth()) + " " + String.valueOf(Gdx.graphics.getHeight()));
    }
}
