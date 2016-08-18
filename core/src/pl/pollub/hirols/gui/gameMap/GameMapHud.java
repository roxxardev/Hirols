package pl.pollub.hirols.gui.gameMap;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.gui.TopBar;
import pl.pollub.hirols.ui.AnimatedImage;
import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.ui.LongPressMenu;

/**
 * Created by erykp_000 on 2016-07-29.
 */
public class GameMapHud extends Hud {

    private TopBar topBar;
    private RightBar rightBar;
    private LeftBar leftBar;

    private AnimatedImage longPressLoading;
    private LongPressMenu longPressMenu;

    public GameMapHud(Hirols game) {
        super(game);
        createActors();
    }

    private void createActors(){
        topBar = new TopBar(game,stage);
        rightBar = new RightBar(game, stage);
        leftBar = new LeftBar(game,stage);

        stage.addActor(topBar);
        stage.addActor(rightBar);
        stage.addActor(leftBar);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        leftBar.update();
        rightBar.update();
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
        topBar.resize(width,height);
        leftBar.resize(width,height, topBar.getHeight());
        rightBar.resize(width,height, topBar.getHeight());
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}
