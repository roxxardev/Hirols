package pl.pollub.hirols.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import javax.xml.soap.Text;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.TownComponent;

/**
 * Created by Marcin on 2016-04-14.
 */
public class LongPressMenu extends VisTable {

    private Hirols game;
    private Stage stage;
    private boolean debug;
    private Skin skin;
    private VisLabel.LabelStyle labelStyle;
    private Sprite sprite;

    public LongPressMenu(Hirols game, Stage stage, boolean debug){
        this.game = game;
        this.stage = stage;
        this.debug = debug;
        this.skin = game.hudManager.skin;

        sprite = new Sprite(game.hudManager.whiteTexture);
        sprite.setColor(0,0,0,0.7f);

        labelStyle = skin.get("label-white", Label.LabelStyle.class);
    }

    public void createLongPressMenuOnEnemy(){
        VisTable infoMenu = new VisTable(true){{
            setDebug(debug);
            setBounds(stage.getWidth() / 2 - stage.getWidth() / 8, stage.getHeight() / 2 - stage.getHeight() / 8, stage.getWidth() / 4, stage.getHeight() / 4);
            setBackground(new SpriteDrawable(sprite));
            setTouchable(Touchable.enabled);
            addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }
            });
        }};

        VisLabel label = new VisLabel("EnemyInfoMenu", labelStyle);
        //label.setPosition(enemyInfoMenu.getWidth()/2,enemyInfoMenu.getHeight()/2);
        infoMenu.add(label);

        this.addActor(infoMenu);
    }

    public void createLongPressMenuOnTown(TownComponent townComponent){
        VisTable infoMenu = new VisTable(true){{
            setDebug(debug);
            setBounds(stage.getWidth() / 2 - stage.getWidth() / 8, stage.getHeight() / 2 - stage.getHeight() / 8, stage.getWidth() / 4, stage.getHeight() / 4);
            setBackground(new SpriteDrawable(sprite));
            setTouchable(Touchable.enabled);
            addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }
            });
        }};


        VisLabel label = new VisLabel("Town name "+townComponent.name, labelStyle);
        //label.setPosition(enemyInfoMenu.getWidth()/2,enemyInfoMenu.getHeight()/2);
        infoMenu.add(label);

        this.addActor(infoMenu);
    }

    public void createLongPressMenuOnMine(){
        VisTable infoMenu = new VisTable(true){{
            setDebug(debug);
            setBounds(stage.getWidth() / 2 - stage.getWidth() / 8, stage.getHeight() / 2 - stage.getHeight() / 8, stage.getWidth() / 4, stage.getHeight() / 4);
            setBackground(new SpriteDrawable(sprite));
            setTouchable(Touchable.enabled);
            addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }
            });
        }};


        VisLabel label = new VisLabel("MineInfoMenu", labelStyle);
        //label.setPosition(enemyInfoMenu.getWidth()/2,enemyInfoMenu.getHeight()/2);
        infoMenu.add(label);

        this.addActor(infoMenu);
    }

    public void createLongPressMenuOnResource(){
        VisTable infoMenu = new VisTable(true){{
            setDebug(debug);
            setBounds(stage.getWidth() / 2 - stage.getWidth() / 8, stage.getHeight() / 2 - stage.getHeight() / 8, stage.getWidth() / 4, stage.getHeight() / 4);
            setBackground(new SpriteDrawable(sprite));
            setTouchable(Touchable.enabled);
            addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }
            });
        }};

        VisLabel label = new VisLabel("ResourceInfoMenu", labelStyle);
        //label.setPosition(enemyInfoMenu.getWidth()/2,enemyInfoMenu.getHeight()/2);
        infoMenu.add(label);

        this.addActor(infoMenu);
    }

    public void createLongPressMenuOnChest(){
        VisTable infoMenu = new VisTable(true){{
            setDebug(debug);
            setBounds(stage.getWidth() / 2 - stage.getWidth() / 8, stage.getHeight() / 2 - stage.getHeight() / 8, stage.getWidth() / 4, stage.getHeight() / 4);
            setBackground(new SpriteDrawable(sprite));
            setTouchable(Touchable.enabled);
            addCaptureListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }
            });
        }};

        VisLabel label = new VisLabel("ChestInfoMenu", labelStyle);
        //label.setPosition(enemyInfoMenu.getWidth()/2,enemyInfoMenu.getHeight()/2);
        infoMenu.add(label);

        this.addActor(infoMenu);
    }

    public void removeMenus(){
        this.getChildren().clear();
    }
}

