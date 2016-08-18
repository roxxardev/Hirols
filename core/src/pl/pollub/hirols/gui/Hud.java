package pl.pollub.hirols.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.pollub.hirols.Hirols;

/**
 * Created by erykp_000 on 2016-07-29.
 */
public abstract class Hud implements Disposable{

    protected Hirols game;

    protected Stage stage;
    protected Viewport viewport;

    public Hud(Hirols game) {
        this.game = game;

        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
    }

    public void update(float delta) {
        stage.act();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void resize(float width, float height) {
        stage.getViewport().setScreenSize((int)width,(int)height);
        stage.getViewport().update((int)width,(int)height, true);
    }

    public Stage getStage() {
        return stage;
    }
}
