package pl.pollub.hirols.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.gui.town.TownHud;

/**
 * Created by Marcin on 2016-04-26.
 */
public class TownScreen extends GameScreen {

    private TownHud townHud;

    public TownScreen(Hirols game) {
        super(game);

        townHud = new TownHud(game);
    }

    @Override
    public void show() {
        super.show();
        game.multiplexer.addProcessor(townHud.getStage());
    }

    @Override
    public void hide() {
        super.hide();
        game.multiplexer.removeProcessor(townHud.getStage());
    }

    @Override
    public void dispose() {
        super.dispose();
        townHud.dispose();
    }

    @Override
    public void render(float delta) {
        townHud.update(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.setScreen(game.gameManager.getCurrentMapScreen());
            dispose();
            return;
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(townHud.getStage().getCamera().combined);
        townHud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        townHud.resize(width,height);
    }

    @Override
    protected void createSystems() {
        //Empty for town
    }
}