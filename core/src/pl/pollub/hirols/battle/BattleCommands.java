package pl.pollub.hirols.battle;

import com.badlogic.gdx.Gdx;

import pl.pollub.hirols.console.CommandsContainer;

/**
 * Created by Eryk on 2016-12-13.
 */

public class BattleCommands extends CommandsContainer {
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
}
