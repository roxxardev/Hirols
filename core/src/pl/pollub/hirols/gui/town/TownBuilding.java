package pl.pollub.hirols.gui.town;

import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public abstract class TownBuilding extends VisWindow {

    public TownBuilding(String title) {
        super(title);
        addCloseButton();

    }

    public abstract boolean upgrade();

}

