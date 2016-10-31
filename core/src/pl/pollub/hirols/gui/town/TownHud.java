package pl.pollub.hirols.gui.town;

import com.kotcrab.vis.ui.widget.VisImageButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.gui.TopBar;

/**
 * Created by erykp_000 on 2016-10-30.
 */

public class TownHud extends Hud {
    private TopBar topBar;

    private VisImageButton exitButton;
    public TownHud(Hirols game, TopBar topBar) {
        super(game);
        this.topBar = topBar;
    }

    private void createActors() {

    }
}

