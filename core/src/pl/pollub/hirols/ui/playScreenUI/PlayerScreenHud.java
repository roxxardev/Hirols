package pl.pollub.hirols.ui.playScreenUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogAdapter;
import com.kotcrab.vis.ui.widget.VisTable;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.managers.AnimationManager;
import pl.pollub.hirols.ui.AnimatedImage;
import pl.pollub.hirols.ui.LongPressMenu;

/**
 * Created by krol22 on 15.02.16.
 */
public class PlayerScreenHud implements Disposable{
    private Hirols game;
    private Skin skin;

    public Stage stage;
    public boolean debug;
    private Viewport guiPort;

    public pl.pollub.hirols.ui.TopBar topBar;
    public pl.pollub.hirols.ui.playScreenUI.RightBar rightBar;
    public LeftBar leftBar;

    private ImageButton menuButton;
    private VisTable leftBarTable;

    private AnimatedImage longPressLoading;
    private LongPressMenu longPressMenus;

    public PlayerScreenHud(Hirols game){
        this.game = game;
        this.skin = game.hudManager.skin;
        OrthographicCamera gameCam;

        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false);

        guiPort = new ScreenViewport(gameCam);
        stage = new Stage(guiPort, game.batch);

        debug = game.hudManager.debug;

        VisUI.load();
        createActors();
    }

    private void createActors(){
        topBar = new pl.pollub.hirols.ui.TopBar(game, stage, debug);
        rightBar = new pl.pollub.hirols.ui.playScreenUI.RightBar(game, stage, topBar, debug);
        leftBar = new LeftBar(game, stage, topBar, debug);

        longPressMenus = new LongPressMenu(game, stage, debug);
        longPressLoading = new AnimatedImage(AnimationManager.createAnimation(game.assetManager.get("animations/loadingLongPress.png", Texture.class), 32, 1, 0.034375f), false);
        longPressLoading.setZIndex(0);

        stage.addActor(longPressLoading);
        stage.addActor(topBar);
        stage.addActor(rightBar);
        stage.addActor(leftBar);
        stage.addActor(longPressMenus);

        //createLeftBar();
    }

    private void createLeftBar(){
        final ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle(game.hudManager.buttonStyleRoundedOver);

        leftBarTable = new VisTable(){{
            setBounds(stage.getWidth()/14 +20, -stage.getWidth() / 14, stage.getWidth() / 14, stage.getWidth() / 14);

            Sprite sprite = new Sprite(game.assetManager.get("ui/options-image.png", Texture.class));
            sprite.setSize(stage.getWidth()/24,stage.getWidth()/24);
            imageButtonStyle.imageUp = new SpriteDrawable(sprite);

            addActor(new ImageButton(imageButtonStyle) {{
                setBounds(0, 0, stage.getWidth() / 14, stage.getWidth() / 14);
            }});

            sprite = new Sprite(game.assetManager.get("ui/save-image.png", Texture.class));
            sprite.setSize(stage.getWidth() / 24, stage.getWidth() / 24);
            imageButtonStyle.imageUp = new SpriteDrawable(sprite);

            addActor(new ImageButton(imageButtonStyle) {{
                setBounds(stage.getWidth() / 14 + 10, 0, stage.getWidth() / 14, stage.getWidth() / 14);
            }});

            sprite = new Sprite(game.assetManager.get("ui/load-image.png", Texture.class));
            sprite.setSize(stage.getWidth() / 24, stage.getWidth() / 24);
            imageButtonStyle.imageUp = new SpriteDrawable(sprite);
            addActor(new ImageButton(imageButtonStyle) {{
                setBounds(stage.getWidth() / 7 + 20, 0, stage.getWidth() / 14, stage.getWidth() / 14);
            }});

            sprite = new Sprite(game.assetManager.get("ui/exit-image.png", Texture.class));
            sprite.setSize(stage.getWidth() / 24, stage.getWidth() / 24);
            imageButtonStyle.imageUp = new SpriteDrawable(sprite);
            addActor(new ImageButton(imageButtonStyle){{
                setBounds(stage.getWidth()*3/14+30,0,stage.getWidth() / 14, stage.getWidth() / 14);
            }});
        }};

        Sprite sprite = new Sprite(game.assetManager.get("ui/menu-image.png", Texture.class));
        sprite.setSize(stage.getWidth() / 24, stage.getWidth() / 24);
        imageButtonStyle.imageUp = new SpriteDrawable(sprite);

        menuButton = new ImageButton(imageButtonStyle){
            {
                setBounds(10, 10, stage.getWidth() / 14, stage.getWidth() / 14);
                addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (isChecked()) {
                            leftBarTable.addAction(Actions.moveTo(stage.getWidth()/14 +20, 10, 0.3f));
                        }else
                            leftBarTable.addAction(Actions.moveTo(stage.getWidth()/14 +20,-stage.getWidth()/14 , 0.3f));
                    }
                });
            }};

        stage.addActor(menuButton);
        stage.addActor(leftBarTable);
    }

    public void update(float delta){
        stage.act(delta);
        longPressLoading.update(delta);
        leftBar.update();
        rightBar.update();
    }

    public void handleLongPressLoading(boolean visible, float x, float y){
        longPressLoading.setVisible(visible);
        if(visible)
            longPressLoading.setPosition(x, y);
    }

    public void unfocusScroll(){
        stage.setScrollFocus(null);
    }

    public LongPressMenu getLongPressMenu(){
        return longPressMenus;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getViewport().setScreenSize(width, height);
        topBar.resize(width, height);
        rightBar.resize(width, height);
        leftBar.resize(width, height);
    }

    public void hideLeftBar(){
        leftBar.hide();
    }

    @Override
    public void dispose() {
        stage.dispose();
        VisUI.dispose();
    }
    public boolean isNewTurn(){
        boolean temp =  rightBar.newTurn;
        rightBar.newTurn = false;
        return temp;
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
