package pl.pollub.hirols.systems.gameMapSystems;

import pl.pollub.hirols.components.player.PlayerComponent;

/**
 * Created by Eryk on 2016-09-05.
 */
public interface PlayerUpdatable {
    public abstract void updatePlayerArrays(Class<? extends PlayerComponent> playerClass);

}
