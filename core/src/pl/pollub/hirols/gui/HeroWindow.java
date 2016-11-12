package pl.pollub.hirols.gui;

import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by erykp_000 on 2016-11-11.
 */

public class HeroWindow extends VisWindow{
    public HeroWindow(String title, boolean showWindowBorder) {
        super(title, showWindowBorder);

        addCloseButton();
        //setDebug(true,true);
    }

    @Override
    protected void close() {
        remove();
    }
}
