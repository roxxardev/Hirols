package pl.pollub.hirols.gui.gameMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.gui.HeroWindow;
import pl.pollub.hirols.gui.TopBar;
import pl.pollub.hirols.managers.AnimationManager;
import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.screens.GameMapScreen;

/**
 * Created by erykp_000 on 2016-07-29.
 */
public class GameMapHud extends Hud {

    private TopBar topBar;
    private RightBar rightBar;
    private LeftBar leftBar;

    private pl.pollub.hirols.gui.AnimatedImage longPressImage;
    private HeroWindow heroWindow;

    private GameMapScreen gameMapScreen;

    public GameMapHud(Hirols game, GameMapScreen gameMapScreen) {
        super(game, new ScreenViewport());
        this.gameMapScreen = gameMapScreen;
        createActors();
    }

    private void createActors(){
        topBar = new TopBar(game,stage);
        rightBar = new RightBar(game, this,gameMapScreen.getGameMapComponentClass());
        leftBar = new LeftBar(game,stage,gameMapScreen);

        longPressImage = new pl.pollub.hirols.gui.AnimatedImage(AnimationManager.createAnimation(game.assetManager.get("animations/loadingLongPress.png", Texture.class), 32, 1, 0.034375f), false);

        stage.addActor(longPressImage);
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

    public void hideLeftBar(){
        leftBar.hide();
    }

    public void unfocusScroll(){
        stage.setScrollFocus(null);
    }

    public void updateLongPressImage(boolean visible, float x, float y) {
        longPressImage.setVisible(visible);
        if(visible) {
            longPressImage.setPosition(x,y);
        }
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
        topBar.resize(width,height);
        leftBar.resize(width,height, topBar.getHeight());
        rightBar.resize(width,height, topBar.getHeight());
        longPressImage.setWidth(width<height ? width/13 : height/13);
        longPressImage.setHeight(longPressImage.getWidth());
    }

    @Override
    public void dispose() {
        super.dispose();

    }

    public pl.pollub.hirols.gui.AnimatedImage getLongPressImage() {
        return longPressImage;
    }

    public void addHeroWindow() {
        if(stage.getActors().contains(heroWindow,true)) return;

        heroWindow = new HeroWindow("test", false);
        heroWindow.setSize(400,400);
        stage.addActor(heroWindow);
    }

    public TopBar getTopBar() {
        return topBar;
    }
}
