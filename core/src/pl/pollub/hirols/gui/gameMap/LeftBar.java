package pl.pollub.hirols.gui.gameMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.screens.GameMapScreen;
import pl.pollub.hirols.screens.MainMenuScreen;

/**
 * Created by erykp_000 on 2016-08-14.
 */
public class LeftBar extends Table {
    private final Hirols game;
    private final GameMapScreen gameMapScreen;

    private final Image menuDrag;
    private VisImageButton optionsButton, saveButton, loadButton, exitToMenuButton;

    private boolean slided = false;

    public LeftBar(Hirols game, Stage stage, GameMapScreen gameMapScreen) {
        this.game = game;
        this.gameMapScreen = gameMapScreen;

        this.setBackground(game.hudManager.getTransparentBackground());
        this.setTouchable(Touchable.enabled);
        setDebug(game.hudManager.debug);

        menuDrag = new Image(game.assetManager.get("ui/menuDrag.png", Texture.class));

        createActors();

        resize(stage.getWidth(),stage.getHeight(),0);

        this.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                clearActions();
                setX(getX() + deltaX);
                if(getX() < -getWidth()) {
                    setX(-getWidth());
                    slided = false;
                } else if(getX() > 0) {
                    setX(0);
                    slided = true;
                }
                menuDrag.setVisible(false);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(getX() <= -getWidth() / 4 && slided || getX() < -getWidth() + getWidth() / 4) {
                    addAction(Actions.moveTo(-getWidth(),0,0.3f));
                    menuDrag.setVisible(true);
                } else if(getX() >= -getWidth() + getWidth() / 4 && !slided || getX() > -getWidth() / 4) {
                    addAction(Actions.moveTo(0,0,0.3f));
                }
            }
        });
    }

    private void createActors(){

        VisImageButton.VisImageButtonStyle buttonStyle = new VisImageButton.VisImageButtonStyle(game.hudManager.skin.get("image-button",VisImageButton.VisImageButtonStyle.class));

        optionsButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(buttonStyle));
        optionsButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),0,178,142,142)));

        //TODO change later for options menu
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMapScreen.getConsole().setVisible(true);
            }
        });

        saveButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(buttonStyle));
        saveButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),142, 190, 98, 130)));

        loadButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(buttonStyle));
        loadButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),240, 178, 98, 144)));

        exitToMenuButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(buttonStyle));
        exitToMenuButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112)));
        exitToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                game.gameManager.dispose();
            }
        });

        addActor(menuDrag);
        addActor(optionsButton);
        addActor(saveButton);
        addActor(loadButton);
        addActor(exitToMenuButton);
    }

    public void update() {
        //TODO check
        //jezeli pozycja x leftbara jest mniejsza od -3/4 leftbar width to schowany
        //jezeli pozycja x leftbara jest wieksza niz -1/4 leftbar width to wysuniety
        if(this.hasActions()) {
            if (this.getX() < -this.getWidth()*3/ 4 && slided) {
                slided = false;
            }
            if (this.getX() > -this.getWidth() / 4 && !slided) {
                slided = true;
            }
        }
    }

    public void hide() {
        if(slided) {
            addAction(Actions.moveTo(-getWidth(),0,0.3f));
            menuDrag.setVisible(true);
        }
    }

    public void resize(float width, float height, float topBarHeight) {
        if(slided) {
            setSize(width/8,height - topBarHeight);
            setPosition(0, 0);
        } else {
            setSize(width/8,height - topBarHeight);
            setPosition(-getWidth(), 0);
        }

        float buttonSize = width<height ? width/10 : height/10;
        float imagePadding = buttonSize/10;

        optionsButton.setSize(buttonSize, buttonSize);
        optionsButton.setPosition(getWidth()/2 - buttonSize/2, getHeight()/8*2);
        optionsButton.pad(imagePadding);
        saveButton.setSize(buttonSize, buttonSize);
        saveButton.setPosition(optionsButton.getX(), getHeight()/8*3);
        saveButton.pad(imagePadding);
        loadButton.setSize(buttonSize, buttonSize);
        loadButton.setPosition(optionsButton.getX(), getHeight()/8*4);
        loadButton.pad(imagePadding);
        exitToMenuButton.setSize(buttonSize, buttonSize);
        exitToMenuButton.setPosition(optionsButton.getX(), getHeight()/8*5);
        exitToMenuButton.pad(imagePadding);

        menuDrag.setSize(buttonSize, buttonSize*3);
        menuDrag.setPosition(getWidth(), getHeight()/2 - menuDrag.getHeight()/2);
    }

}
