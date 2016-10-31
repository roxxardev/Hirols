package pl.pollub.hirols.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.ui.townScreenUI.TownScreenHud;

/**
 * Created by Marcin on 2016-04-26.
 */
public class TownScreen extends GameScreen {

    private TownScreenHud townScreenHud;

    public TownScreen(Hirols game) {
        super(game);

        townScreenHud = new TownScreenHud(game);
    }

    @Override
    public void show() {
        super.show();
        game.multiplexer.addProcessor(townScreenHud.stage);
    }

    @Override
    public void hide() {
        super.hide();
        game.multiplexer.removeProcessor(townScreenHud.stage);
    }

    @Override
    public void dispose() {
        super.dispose();
        townScreenHud.dispose();
    }

    @Override
    public void render(float delta) {
        townScreenHud.update(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(game.gameManager.getCurrentMapScreen());
            dispose();
            return;
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(townScreenHud.stage.getCamera().combined);
        townScreenHud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    protected void createSystems() {
        //Empty for town
    }
}