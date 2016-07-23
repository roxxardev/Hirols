package pl.pollub.hirols.ui.playScreenUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import pl.pollub.hirols.Hirols;

/**
 * Created by krol22 on 24.02.16.
 */
public class LeftBar extends VisTable {
    private Hirols game;
    private Stage stage;
    private VisTable topBar;
    private boolean slided = false;
    private Skin skin;

    private VisImageButton optionsButton, saveButton, loadButton, exitToMenuButton;
    public boolean optionsButtonClicked = false;
    private Image image;

    public LeftBar(Hirols game, final Stage stage, VisTable topBar, boolean debug){
        this.game = game;
        this.stage = stage;
        this.topBar = topBar;
        this.skin = game.hudManager.skin;

        this.setBackground(new SpriteDrawable(new Sprite(game.hudManager.whiteTexture){{
            setColor(0, 0, 0, 0.3f);
        }}));
        this.setBounds(-stage.getWidth() / 4, 0,this.topBar.getHeight() * 3,stage.getHeight() - this.topBar.getHeight());
        this.setTouchable(Touchable.enabled);

        //TODO dodac mozliwosc przesuwania panelu bocznego w zaleznosci od predkosci
        this.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                LeftBar.this.clearActions();
                LeftBar.this.setX(LeftBar.this.getX() + deltaX);
                //leftbar x nie moze byc wysuniety bardziej niz 0 i mniej niz -leftbar.width
                if (LeftBar.this.getX() < -LeftBar.this.getWidth()) {
                    LeftBar.this.setX(-LeftBar.this.getWidth());
                    slided = false;
                } else if (LeftBar.this.getX() > 0) {
                    LeftBar.this.setX(0);
                    slided = true;
                }
                image.setVisible(false);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //nadaj ackje moveTo w zaleznosci od slided
                if (LeftBar.this.getX() <= -LeftBar.this.getWidth() / 4 && slided || LeftBar.this.getX() < -LeftBar.this.getWidth() + LeftBar.this.getWidth() / 4) {
                    LeftBar.this.addAction(Actions.moveTo(-LeftBar.this.getWidth(), 0, 0.3f));
                    image.setVisible(true);
                } else if (LeftBar.this.getX() >= -LeftBar.this.getWidth() + LeftBar.this.getWidth() / 4 && !slided || LeftBar.this.getX() > -LeftBar.this.getWidth() / 4) {
                    LeftBar.this.addAction(Actions.moveTo(0, 0, 0.3f));
                }
            }
        });

        createActors();

        this.setDebug(debug);

    }

    private void createActors(){
        optionsButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(skin.get("image-button",VisImageButton.VisImageButtonStyle.class)){{
            imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),0,178,142,142)){{
                setSize(topBar.getHeight(),topBar.getHeight());
            }});
        }});

        saveButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(skin.get("image-button",VisImageButton.VisImageButtonStyle.class)){{
            imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),142, 190, 98, 130)){{
                setSize(topBar.getHeight(),topBar.getHeight());
            }});
        }});
        loadButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(skin.get("image-button",VisImageButton.VisImageButtonStyle.class)){{
            imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),240, 178, 98, 144)){{
                setSize(topBar.getHeight(),topBar.getHeight());
            }});
        }});
        exitToMenuButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(skin.get("image-button",VisImageButton.VisImageButtonStyle.class)){{
            imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112)){{
                setSize(topBar.getHeight(),topBar.getHeight());
            }});
        }});

        optionsButton.setBounds(this.topBar.getHeight()/2, this.getHeight() - this.getHeight() / 3, this.topBar.getHeight()*2, this.topBar.getHeight()*2);
        saveButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 12 / 10, optionsButton.getWidth(), optionsButton.getHeight());
        loadButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 24 / 10, optionsButton.getWidth(), optionsButton.getHeight());
        exitToMenuButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 36 / 10, optionsButton.getWidth(), optionsButton.getHeight());

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("optionsClicked", "true");
                optionsButtonClicked = true;
            }
        });

        image = new Image(game.assetManager.get("ui/menuDrag.png", Texture.class));
        image.setBounds(this.getWidth(), stage.getHeight()/2 - stage.getHeight()/6, stage.getWidth()/20, stage.getHeight()/3);

        this.addActor(image);
        this.addActor(optionsButton);
        this.addActor(saveButton);
        this.addActor(loadButton);
        this.addActor(exitToMenuButton);

    }
    public void update(){
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

    public void hide(){
        if(slided) {
            this.addAction(Actions.moveTo(-this.getWidth(), 0, 0.3f));
            image.setVisible(true);
        }
    }

    public void resize(int width, int height){
        int tempWidth, tempHeight;
        if (width > 1280) {
            tempWidth = 1280;
            tempHeight = 720;
            this.setHeight(height- topBar.getHeight());
        }
        else {
            tempWidth = width;
            tempHeight = height;
            this.setHeight(tempHeight - topBar.getHeight());
        }


        if(!slided)
            this.setPosition(-this.getWidth(),0);
        else
            this.setPosition(0, 0);


        optionsButton.setPosition(this.topBar.getHeight()/2, this.getHeight() - this.getHeight() / 3);
        saveButton.setPosition(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 12 / 10);
        loadButton.setPosition(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 24 / 10);
        exitToMenuButton.setPosition(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 36 / 10);
        image.setPosition(this.getWidth(), height/3);
    }

}
