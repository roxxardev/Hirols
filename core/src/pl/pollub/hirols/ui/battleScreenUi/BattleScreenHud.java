package pl.pollub.hirols.ui.battleScreenUi;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import pl.pollub.hirols.Hirols;

/**
 * Created by Marcin on 2016-04-21.
 */
public class BattleScreenHud implements Disposable {
    private Hirols game;
    public Stage stage;
    private Skin skin;

    private VisLabel.LabelStyle labelStyle;
    private WidgetGroup widgetGroup;
    private VisTable magicBook;
    private VisTextButton waitButton, blockButton, magicBookButton,runButton;
    private VisScrollPane scrollPane;
    private GridGroup gridGroup;
    private Label label;

    public BattleScreenHud(Hirols game, Viewport viewport) {
        this.game = game;
        this.skin = game.hudManager.skin;

        OrthographicCamera gameCam;

        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false);

        stage = new Stage(viewport, game.batch);

        labelStyle = skin.get("label-white", Label.LabelStyle.class);

        widgetGroup = new WidgetGroup();
        widgetGroup.setBounds(stage.getWidth() / 3, 0, stage.getWidth() / 3, stage.getHeight() / 20);

        stage.addActor(widgetGroup);


        waitButton = new VisTextButton("Wait", skin.get("text-button", VisTextButton.VisTextButtonStyle.class));
        waitButton.setBounds(stage.getHeight() / 20, stage.getHeight() / 20, stage.getWidth() / 20, stage.getWidth() / 20);

        blockButton = new VisTextButton("Block", skin.get("text-button", VisTextButton.VisTextButtonStyle.class));
        blockButton.setBounds(waitButton.getX(), stage.getHeight() - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());

        magicBookButton = new VisTextButton("Magic Book", skin.get("text-button", VisTextButton.VisTextButtonStyle.class)){{
            setBounds(stage.getWidth() - waitButton.getX() - waitButton.getWidth(), stage.getHeight() - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(magicBook.isVisible()){
                        magicBook.setVisible(false);
                        scrollPane.remove();
                    }
                    else {
                        scrollPane = new VisScrollPane(gridGroup){{
                            setScrollingDisabled(true, false);
                            setBounds(0, 0, magicBook.getWidth(), magicBook.getHeight());
                        }};
                        magicBook.addActor(scrollPane);
                        magicBook.setVisible(true);
                    }
                }
            });
        }};

        createMagicBook();

        runButton = new VisTextButton("Run", skin.get("text-button", VisTextButton.VisTextButtonStyle.class));
        runButton.setBounds(stage.getWidth() - waitButton.getX() - waitButton.getWidth(), waitButton.getY(), waitButton.getWidth(), waitButton.getHeight());

        stage.addActor(waitButton);
        stage.addActor(blockButton);
        stage.addActor(magicBookButton);
        stage.addActor(runButton);

    }

    public void showInformation(String string) {
        if(!widgetGroup.getChildren().contains(label,false)) {
            label = new VisLabel(string, labelStyle);
            widgetGroup.addActor(label);
        }
    }

    public void createMagicBook(){
        magicBook = new VisTable(){{
            setBounds(stage.getWidth() / 5, stage.getHeight() / 10, stage.getWidth() * 3 / 5, stage.getHeight() * 4 / 5);
            setTouchable(Touchable.enabled);

            Sprite sprite = new Sprite(game.hudManager.getWhiteTexture()){{
                setColor(0f, 0f, 0f, 0.7f);}};

            setBackground(new SpriteDrawable(sprite));
            setVisible(false);
        }};

        gridGroup = new GridGroup((int)(magicBook.getWidth() / 6), magicBook.getWidth() / 38){{
            setHeight(magicBook.getHeight());
            setWidth(magicBook.getWidth());
        }};


        for(int i =0 ; i < 20 ; i++) {
            VisImageTextButton spell1 = new VisImageTextButton("spell" + i, new SpriteDrawable(new Sprite(game.assetManager.get("resources/alienrsc.png", Texture.class)))){{
                clearChildren();
                add(getImage()).row();
                add(getLabel());
            }};

            gridGroup.addActor(spell1);
        }
        stage.addActor(magicBook);
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    //TODO resize labels
    public void resize(float width,float height){
        waitButton.setBounds(height / 20, height / 20, width / 20, width / 20);
        blockButton.setBounds(waitButton.getX(), height - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());
        magicBookButton.setBounds(width - waitButton.getX() - waitButton.getWidth(), height - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());
        runButton.setBounds(width - waitButton.getX() - waitButton.getWidth(), waitButton.getY(), waitButton.getWidth(), waitButton.getHeight());

    }
}
