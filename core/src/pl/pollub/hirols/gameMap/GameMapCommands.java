package pl.pollub.hirols.gameMap;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.MineComponent;
import pl.pollub.hirols.components.map.MineDataComponent;
import pl.pollub.hirols.components.map.TownComponent;
import pl.pollub.hirols.components.map.TownDataComponent;
import pl.pollub.hirols.console.CommandsContainer;
import pl.pollub.hirols.systems.gameMapSystems.MapInteractionSystem;

/**
 * Created by Eryk on 2016-12-13.
 */

public class GameMapCommands extends CommandsContainer {
    private Hirols game;
    private Map gameMap;

    public GameMapCommands(Hirols game, Map gameMap) {
        this.game = game;
        this.gameMap = gameMap;
    }

    @Override
    public void exit() {
        Gdx.app.exit();
    }

    @Override
    public void showCommands() {
        console.showCommands();
    }

    @Override
    public void clear() {
        console.clear();
    }

    public void quit() { Gdx.app.exit();}

    public void setMovementPoints(float value) {
        Entity selectedHero = game.engine.getSystem(MapInteractionSystem.class).getSelectedHeroes().first();
        HeroDataComponent selectedHeroData = ComponentMapper.getFor(HeroDataComponent.class).get(selectedHero);
        selectedHeroData.movementPoints = value;
        game.engine.getSystem(MapInteractionSystem.class).resetHeroPath(selectedHeroData, true);

        console.log("SelectedComponent Hero id: "+ selectedHeroData.id +" movement points set to " + value + ".");
    }

    public void recalculatePath() {
        Entity selectedHero = game.engine.getSystem(MapInteractionSystem.class).getSelectedHeroes().first();
        HeroDataComponent selectedHeroData = ComponentMapper.getFor(HeroDataComponent.class).get(selectedHero);
        if(game.engine.getSystem(MapInteractionSystem.class).recalculatePathForHero(selectedHero)) {
            console.log("Path recalculated for selected hero id: "+ selectedHeroData.id + ".");
            return;
        }
        console.log("Hero id: "+ selectedHeroData.id + " has no path to recalculate.");
    }

    public void showPlayersMines() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Class player : game.gameManager.getPlayerClasses()) {
            stringBuilder.append("\n");
            ImmutableArray<Entity> mines = game.engine.getEntitiesFor(Family.all(player, MineComponent.class, MineDataComponent.class, gameMap.getGameMapComponentClazz()).get());


            stringBuilder.append(player.getSimpleName()).append(" mines:");
            for(Entity mine : mines) {
                stringBuilder.append(ComponentMapper.getFor(MineDataComponent.class).get(mine).type.toString()).append(", ");
            }
        }
        console.log(stringBuilder.toString());
    }

    public void showPlayersTowns() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Class player : game.gameManager.getPlayerClasses()) {
            stringBuilder.append("\n");
            ImmutableArray<Entity> towns = game.engine.getEntitiesFor(Family.all(player, TownComponent.class, TownDataComponent.class, gameMap.getGameMapComponentClazz()).get());

            stringBuilder.append(player.getSimpleName()).append(" towns:");
            for(Entity mine : towns) {
                stringBuilder.append(ComponentMapper.getFor(MineDataComponent.class).get(mine).type.toString()).append(", ");
            }
        }
        console.log(stringBuilder.toString());
    }
}