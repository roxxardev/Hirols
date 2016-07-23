package pl.pollub.hirols.ui.townScreenUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.ui.TopBar;

/**
 * Created by Marcin on 2016-04-22.
 */
public class TownScreenHud implements Disposable {
    private Hirols game;
    public Stage stage;
    private Skin skin;
    private boolean debug;

    private Image townImage;
    private VisTable leftBar, rightBar, middleBar, recruitsBar;
    private VisImageButton exitTownButton;
    private VisTextButton moveEntitiesUpButton, heroInTown, heroOutOfTown;
    private VisTextButton moveEntitiesDownButton;
    private GridGroup inTownUnits, outOfTownUnits;
    private TopBar topBar;

    public TownScreenHud(Hirols game){
        stage = new Stage();

        this.game = game;
        this.skin = game.hudManager.skin;

        OrthographicCamera gameCam;

        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false);

        Viewport guiPort = new ScreenViewport(gameCam);

        stage = new Stage(guiPort, game.batch);
        debug = game.hudManager.debug;

        Gdx.input.setInputProcessor(stage);

        debug = false;

        createActors();
    }

    public void createActors(){
        topBar = new TopBar(game, stage, debug);

        exitTownButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(skin.get("image-button",VisImageButton.VisImageButtonStyle.class)){{
            imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112)){{
                setSize(topBar.getHeight(),topBar.getHeight());
            }});
        }});
        exitTownButton.setBounds(topBar.getHeight(), stage.getHeight()- 4*topBar.getHeight(), topBar.getHeight()*2, topBar.getHeight()*2);

        inTownUnits = new GridGroup((int)topBar.getHeight()*2, 2){{
            setBounds(topBar.getHeight(),topBar.getHeight(), topBar.getHeight()*11, topBar.getHeight()*2);
            for(int i =0; i <5; i++){
                addActor(new VisTextButton("empty",skin.get("text-button", VisTextButton.VisTextButtonStyle.class)));
            }
        }};

        outOfTownUnits = new GridGroup((int)topBar.getHeight()*2, 2){{
            setBounds(stage.getWidth() - topBar.getHeight()*12, topBar.getHeight(), topBar.getHeight()*11, topBar.getHeight()*2);
            for(int i =0; i <5; i++){
                addActor(new VisTextButton("empty",skin.get("text-button", VisTextButton.VisTextButtonStyle.class)));
            }
        }};

        Label gridLabel1 = new Label("InTownUnits", skin.get("label-white", Label.LabelStyle.class));
        gridLabel1.setPosition(inTownUnits.getX(),inTownUnits.getHeight()+inTownUnits.getY());
        Label gridLabel2 = new Label("OutOfTownUnits", skin.get("label-white", Label.LabelStyle.class));
        gridLabel2.setPosition(outOfTownUnits.getX(),outOfTownUnits.getHeight()+outOfTownUnits.getY());

        heroInTown = new VisTextButton("empty", skin.get("text-button", VisTextButton.VisTextButtonStyle.class));
        heroInTown.setBounds(inTownUnits.getX()+inTownUnits.getWidth()-10,inTownUnits.getY(),topBar.getHeight()*3,topBar.getHeight()*3);
        heroOutOfTown = new VisTextButton("empty", skin.get("text-button", VisTextButton.VisTextButtonStyle.class));
        heroOutOfTown.setBounds(outOfTownUnits.getX()-topBar.getHeight()*3 - 10 ,outOfTownUnits.getY(),topBar.getHeight()*3,topBar.getHeight()*3);

        townImage = new Image(game.assetManager.get("towns/snow-town.png", Texture.class));
        townImage.setBounds(0, 0, stage.getWidth(), stage.getHeight());

        stage.addActor(townImage);
        stage.addActor(topBar);
        stage.addActor(exitTownButton);
        stage.addActor(inTownUnits);
        stage.addActor(outOfTownUnits);
        stage.addActor(heroInTown);
        stage.addActor(heroOutOfTown);
        stage.addActor(gridLabel1);
        stage.addActor(gridLabel2);
    }

    public void update(float delta){
        stage.act(delta);
    }

    public void resize(int width, int height) {

    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}
