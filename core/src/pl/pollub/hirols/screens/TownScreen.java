package pl.pollub.hirols.screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.TownDataComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.gui.town.TownHud;

/**
 * Created by Marcin on 2016-04-26.
 */
public class TownScreen extends GameScreen {

    private TownHud townHud;

    public TownScreen(Hirols game, Entity townEnterEntity) {
        super(game);

        TownDataComponent townDataComponent = ComponentMapper.getFor(TownDataComponent.class).get(townEnterEntity);

        ImmutableArray<Entity> heroes = game.engine.getEntitiesFor(Family.all(HeroDataComponent.class, game.gameManager.getCurrentMapScreen().getGameMapComponentClass()).get());
        PositionComponent townEnterPosition = ComponentMapper.getFor(PositionComponent.class).get(townEnterEntity);

        Entity heroAtGate = null;

        for(Entity hero : heroes) {
            PositionComponent heroPosition = ComponentMapper.getFor(PositionComponent.class).get(hero);
            if(heroPosition.x == townEnterPosition.x && heroPosition.y == townEnterPosition.y) {
                heroAtGate = hero;
                break;
            }
        }

        townHud = new TownHud(game, townDataComponent, heroAtGate);
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

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || townHud.isExitRequest()){
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