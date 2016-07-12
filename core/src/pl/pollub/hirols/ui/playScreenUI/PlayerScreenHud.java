package pl.pollub.hirols.ui.playScreenUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogAdapter;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.managers.AnimationManager;

/**
 * Created by krol22 on 15.02.16.
 */
public class PlayerScreenHud implements Disposable{
    private Hirols game;
    private Skin skin;

    public Stage stage;
    public boolean debug;
    private Viewport guiPort;

    private TopBar topBar;
    private RightBar rightBar;
    private LeftBar leftBar;
    private pl.pollub.hirols.ui.AnimatedImage longPressLoading;
    private pl.pollub.hirols.ui.LongPressMenu longPressMenus;


    public PlayerScreenHud(Hirols game){
        this.game = game;
        this.skin = game.hudManager.skin;
        OrthographicCamera gameCam;

        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false);

        guiPort = new ScreenViewport(gameCam);
        stage = new Stage(guiPort, game.batch);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.rgba8888(1f, 1f, 1f, 1f));
        pixmap.fill();

        Texture whiteTexture = new Texture(pixmap);

        debug = game.hudManager.debug;

        VisUI.load();
        createActors(whiteTexture);

        pixmap.dispose();
    }

    private void createActors(Texture texture){
        topBar = new TopBar(stage, game.assetManager, debug, texture);
        rightBar = new RightBar(game, stage, topBar, debug, texture);
        leftBar = new LeftBar(game, stage, topBar, debug);

        longPressLoading = new pl.pollub.hirols.ui.AnimatedImage( stage,  AnimationManager.createAnimation(game.assetManager.get("animations/loadingLongPress.png", Texture.class), 32, 1, 0.034375f), false);
        longPressMenus = new pl.pollub.hirols.ui.LongPressMenu(game, stage, debug, texture);

        stage.addActor(longPressLoading);
        stage.addActor(topBar);
        stage.addActor(rightBar);
        stage.addActor(leftBar);
        stage.addActor(longPressMenus);
    }

    public void update(float delta){
        stage.act(delta);
        longPressLoading.update(delta);
        leftBar.update();
        rightBar.update();
    }

    public void handleLongPressLoading(boolean load, Vector2 targetPosition){
        longPressLoading.setPositionToRender(targetPosition);
        longPressLoading.setVisible(load);
    }

    public pl.pollub.hirols.ui.LongPressMenu getLongPressMenu(){
        return longPressMenus;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getViewport().setScreenSize(width, height);
        topBar.resize(width, height);
        rightBar.resize(width, height);
        leftBar.resize(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        VisUI.dispose();
    }

    public void showOkWindow(String title, String text){
        Dialogs.showOKDialog(stage, title, text);
    }

    public void showYesOrNoWindow(String title, String text){
        Dialogs.showOptionDialog(stage, title, text, Dialogs.OptionDialogType.YES_NO, new OptionDialogAdapter(){
            @Override
            public void yes () {
                Dialogs.showOKDialog(stage, "result", "pressed: yes");
            }

            @Override
            public void no () {
                Dialogs.showOKDialog(stage, "result", "pressed: no");
            }
        });
    }

}
