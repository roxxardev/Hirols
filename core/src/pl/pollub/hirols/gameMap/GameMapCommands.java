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
import pl.pollub.hirols.screens.GameMapScreen;
import pl.pollub.hirols.systems.gameMapSystems.MapInteractionSystem;

/**
 * Created by Eryk on 2016-12-13.
 */

public class GameMapCommands extends CommandsContainer {
    private Hirols game;
    private GameMapScreen gameMapScreen;
    private Map gameMap;

    public GameMapCommands(Hirols game, GameMapScreen gameMapScreen) {
        this.game = game;
        this.gameMapScreen = gameMapScreen;
        this.gameMap = gameMapScreen.getMap();
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
    public void help() {
        showCommands();
    }

    @Override
    public void clear() {
        console.clear();
    }

    public void quit() { Gdx.app.exit();}

    public void setMovementPoints(float value) {
        Entity selectedHero = game.engine.getSystem(MapInteractionSystem.class).getSelectedHeroes().first();
        HeroDataComponent selectedHeroData = ComponentMapper.getFor(HeroDataComponent.class).get(selectedHero);
        if (game.engine.getSystem(MapInteractionSystem.class).resetHeroPath(selectedHeroData, true)) {
            selectedHeroData.movementPoints = value;
            gameMapScreen.getHud().getRightBar().updateSelectedHero(selectedHero);
            console.log("SelectedComponent Hero id: " + selectedHeroData.id + " movement points set to " + value + ".");
        } else {
            console.log("SelectedComponent Hero id: " + selectedHeroData.id + " cannot set movement points, hero must stand!");
        }
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
            for(Entity town : towns) {
                stringBuilder.append(ComponentMapper.getFor(TownDataComponent.class).get(town).name).append(", ");
            }
        }
        console.log(stringBuilder.toString());
    }

    public void setTopBarHeightDivider(int heightDivider) {
        gameMapScreen.getHud().getTopBar().setHeightDivider(heightDivider);
        console.log("TopBar heightdivider set to " + heightDivider+".");
    }

    public void addExperience(int experience) {
        Entity selectedHero = game.engine.getSystem(MapInteractionSystem.class).getSelectedHeroes().first();
        HeroDataComponent selectedHeroData = ComponentMapper.getFor(HeroDataComponent.class).get(selectedHero);
        selectedHeroData.heroLevel.addExperience(experience);
        console.log(experience + " experience added. Hero is now " + selectedHeroData.heroLevel.getLevel() + " level with " + selectedHeroData.heroLevel.getExperience() +  " experience.");
    }
}
