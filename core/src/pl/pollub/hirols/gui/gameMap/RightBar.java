package pl.pollub.hirols.gui.gameMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import pl.pollub.hirols.Hirols;

/**
 * Created by erykp_000 on 2016-08-14.
 */
public class RightBar extends Table {
    private final Hirols game;
    private final Stage stage;

    private boolean slided = false;

    private VisImageButton moveButton, turnButton;
    private VisTextButton changeScrollPane;
    private VisScrollPane scrollPaneHeroes, scrollPaneTowns;
    private GridGroup gridGroupHeroes, gridGroupTowns;
    private Image rightBarDragImage;
    private VisWindow miniMapWindow;

    public RightBar(Hirols game, final Stage stage) {
        this.game = game;
        this.stage = stage;

        setTouchable(Touchable.enabled);
        setBackground(game.hudManager.getTransparentBackground());
        setDebug(game.hudManager.debug);

        createActors();

        resize(stage.getWidth(), stage.getHeight(), 10);

        //TODO zmienic z Marcinowego na jakies czytelne
        addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                clearActions();
                setX(getX() + deltaX);
                if(getX() < stage.getWidth() - getWidth()) {
                    setX(stage.getWidth() - getWidth());
                    slided = true;
                } else if(getX() > stage.getWidth() - getWidth() / 8) {
                    setX(stage.getWidth() - stage.getWidth()/30);
                    slided = false;
                }
                rightBarDragImage.addAction(Actions.fadeOut(0.3f));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(getX() <= stage.getWidth() - getWidth() / 4 && !slided || getX() < stage.getWidth() - getWidth()*3/4) {
                    addAction(Actions.moveTo(stage.getWidth() - getWidth(), 0, 0.3f));
                } else if(getX() >= stage.getWidth() - getWidth() * 3/4 && slided || getX() > stage.getWidth() - getWidth() / 4) {
                    addAction(Actions.moveTo(stage.getWidth(), 0, 0.3f));
                    rightBarDragImage.addAction(Actions.fadeIn(0.3f));
                }
            }
        });
    }

    private void createActors() {
        moveButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(game.hudManager.skin.get("image-button", VisImageButton.VisImageButtonStyle.class)));
        moveButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class), 0, 2, 128, 176)));
        turnButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(game.hudManager.skin.get("image-button", VisImageButton.VisImageButtonStyle.class)));
        turnButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class), 128, 0, 104, 178)));

        turnButton.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                //new turn
            }
        });

        addActor(moveButton);
        addActor(turnButton);

        miniMapWindow = new VisWindow("MINIMAP");
        miniMapWindow.setKeepWithinStage(false);
        miniMapWindow.setMovable(false);

        gridGroupHeroes = new GridGroup();
        for(int i = 0; i < 5; i++) {
            VisImageTextButton hero = new VisImageTextButton("hero" + i, new SpriteDrawable(new Sprite(game.assetManager.get("temp/portrait.png", Texture.class))));
            hero.clearChildren();
            hero.add(hero.getImage()).expand().fill().row();
            hero.add(hero.getLabel());
            gridGroupHeroes.addActor(hero);
        }

        gridGroupTowns = new GridGroup();
        for(int i = 0; i < 7; i++) {
            VisImageTextButton town = new VisImageTextButton("town" + i, new SpriteDrawable(new Sprite(game.assetManager.get("temp/town.gif", Texture.class))));
            town.clearChildren();
            town.add(town.getImage()).expand().fill().row();
            town.add(town.getLabel());
            gridGroupTowns.addActor(town);
        }

        scrollPaneHeroes = new VisScrollPane(gridGroupHeroes) {{
            setForceScroll(false, true);
            setScrollingDisabled(true, false);
            setVisible(true);
            setFadeScrollBars(false);
        }};

        scrollPaneTowns = new VisScrollPane(gridGroupTowns) {{
            setForceScroll(false, true);
            setScrollingDisabled(true, false);
            setVisible(false);
            setFadeScrollBars(false);
        }};

        changeScrollPane = new VisTextButton("HEROES TOWNS", game.hudManager.skin.get("text-button", VisTextButton.VisTextButtonStyle.class)) {{
            addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (scrollPaneHeroes.isVisible()) {
                        scrollPaneHeroes.setVisible(false);
                        scrollPaneTowns.setVisible(true);
                    } else {
                        scrollPaneHeroes.setVisible(true);
                        scrollPaneTowns.setVisible(false);
                    }
                }
            });
        }};

        rightBarDragImage = new Image(game.assetManager.get("ui/minimapDrag.png", Texture.class));

        addActor(miniMapWindow);
        addActor(changeScrollPane);
        addActor(scrollPaneHeroes);
        addActor(scrollPaneTowns);
        addActor(rightBarDragImage);
    }

    public void update() {
        if (this.hasActions()) {
            if (this.getX() < stage.getWidth() - this.getWidth() * 3 / 4)
                slided = true;
            if (this.getX() > stage.getWidth() - this.getWidth() / 4)
                slided = false;
        }
    }

    public void resize(float width, float height, float topBarHeight) {
        //TODO wysokosc topbara
        if(slided) {
            setSize(width / 4, height - topBarHeight);
            setPosition(width - getWidth(), 0);
        } else {
            setSize(width / 4, height - topBarHeight);
            setPosition(width, 0);
        }

        float buttonSize = width<height ? width/10 : height/10;
        float imagePadding = buttonSize/10;

        moveButton.setSize(buttonSize,buttonSize);
        moveButton.setPosition(-buttonSize*3/2, getHeight() - buttonSize*3/2);
        moveButton.pad(imagePadding);

        turnButton.setSize(buttonSize,buttonSize);
        turnButton.setPosition(-buttonSize*3/2, buttonSize/2);
        turnButton.pad(imagePadding);

        float pad = width<height ? width/30 : height/30;

        miniMapWindow.setBounds(pad, getHeight()*2/3, getWidth() - 2*pad, getHeight()/3 - pad);

        changeScrollPane.setBounds(pad, pad, getWidth() - 2 * pad, pad);
        scrollPaneHeroes.setBounds(pad, pad+changeScrollPane.getY() + pad, getWidth() - 2 * pad, getHeight() * 2 / 3 - changeScrollPane.getHeight() -3*pad);
        scrollPaneHeroes.setDebug(true);
        scrollPaneTowns.setBounds(pad, pad+changeScrollPane.getY() + pad, getWidth() - 2 * pad, getHeight() * 2 / 3 - changeScrollPane.getHeight() -3*pad);
        int gridSize = scrollPaneHeroes.getWidth() < scrollPaneHeroes.getHeight() ? (int)(scrollPaneHeroes.getWidth() - pad) : (int)((scrollPaneHeroes.getWidth() - pad)/2);
        gridGroupHeroes.setItemSize(gridSize - 2*gridGroupHeroes.getSpacing(),gridSize- 2*gridGroupHeroes.getSpacing());
        gridGroupTowns.setItemSize(gridSize- 2*gridGroupTowns.getSpacing(),gridSize- 2*gridGroupTowns.getSpacing());

        rightBarDragImage.setSize(buttonSize, 3*buttonSize);
        rightBarDragImage.setPosition(-rightBarDragImage.getWidth(), getHeight()/2 - rightBarDragImage.getHeight()/2);

    }
}