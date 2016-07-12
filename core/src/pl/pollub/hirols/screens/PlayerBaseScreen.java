package pl.pollub.hirols.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import pl.pollub.hirols.Hirols;

/**
 * Created by krol22 on 28.02.16.
 */
public class PlayerBaseScreen implements Screen{

    private Hirols game;

    public PlayerBaseScreen(Hirols game){

        this.game = game;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
