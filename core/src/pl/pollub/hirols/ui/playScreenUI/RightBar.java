package pl.pollub.hirols.ui.playScreenUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import pl.pollub.hirols.Hirols;

/**
 * Created by krol22 on 24.02.16.
 */
public class RightBar extends VisTable {

    private Hirols game;
    private final Stage stage;
    private Skin skin;
    private BitmapFont font;
    private VisTable topBar;
    private boolean slided = false;

    private VisImageButton move, turn;
    private VisTextButton changeScrollPane;
    private VisScrollPane scrollPane, scrollPaneHeroes, scrollPaneTowns;
    private GridGroup gridGroup, gridGroupHeroes, gridGroupTowns;
    private Image rightBarDragImage;
    private VisWindow minimap;
    public boolean newTurn;

    public RightBar(Hirols game, final Stage stage, VisTable topBar, boolean debug) {
        this.game = game;
        this.stage = stage;
        this.topBar = topBar;

        this.skin = game.hudManager.skin;

        this.setWidth(this.stage.getWidth() / 4);
        this.setHeight(this.stage.getHeight() - this.topBar.getHeight());
        this.setPosition(this.stage.getWidth(), 0);
        this.setBackground(new SpriteDrawable(new Sprite(game.hudManager.whiteTexture) {{
            setColor(0, 0, 0, 0.3f);
        }}));
        this.setTouchable(Touchable.enabled);

        //TODO dodac mozliwosc przesuwania panelu bocznego w zaleznosci od predkosci
        this.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                RightBar.this.clearActions();
                RightBar.this.setX(RightBar.this.getX() + deltaX);
                if (RightBar.this.getX() < stage.getWidth() - RightBar.this.getWidth()) {
                    RightBar.this.setX(stage.getWidth() - RightBar.this.getWidth());
                    slided = true;
                } else if (RightBar.this.getX() > stage.getWidth() - RightBar.this.getWidth() / 8) {
                    RightBar.this.setX(stage.getWidth() - RightBar.this.topBar.getHeight());
                    slided = false;
                }
                rightBarDragImage.addAction(Actions.fadeOut(0.3f));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (RightBar.this.getX() <= stage.getWidth() - RightBar.this.getWidth() / 4 && !slided || RightBar.this.getX() < stage.getWidth() - RightBar.this.getWidth() * 3 / 4) {
                    RightBar.this.addAction(Actions.moveTo(stage.getWidth() - RightBar.this.getWidth(), 0, 0.3f));
                } else if (RightBar.this.getX() >= stage.getWidth() - RightBar.this.getWidth() * 3 / 4 && slided || RightBar.this.getX() > stage.getWidth() - RightBar.this.getWidth() / 4) {
                    RightBar.this.addAction(Actions.moveTo(stage.getWidth(), 0, 0.3f));
                    rightBarDragImage.addAction(Actions.fadeIn(0.3f));
                }
            }
        });

        createActors();

        this.setDebug(debug);
    }

    private void createActors() {
        move = new VisImageButton(new VisImageButton.VisImageButtonStyle(skin.get("image-button", VisImageButton.VisImageButtonStyle.class)) {{
            imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class), 0, 2, 128, 176)) {{
                setSize(topBar.getHeight(), topBar.getHeight());
            }});
        }});
        move.setBounds(-this.topBar.getHeight() * 3, this.getHeight() - this.topBar.getHeight() * 3, this.topBar.getHeight() * 2, this.topBar.getHeight() * 2);
        this.addActor(move);

        turn = new VisImageButton(
                new VisImageButton.VisImageButtonStyle(skin.get("image-button", VisImageButton.VisImageButtonStyle.class)) {{
                    imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class), 128, 0, 104, 178)) {{
                        setSize(topBar.getHeight(), topBar.getHeight());
                    }});
                }}) {{
            addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    newTurn = true;
                }
            });
        }};
        turn.setBounds(-this.topBar.getHeight() * 3, this.topBar.getHeight(), this.topBar.getHeight() * 2, this.topBar.getHeight() * 2);

        this.addActor(turn);

        minimap = new VisWindow("MINIMAP");
        minimap.setKeepWithinStage(false);
        minimap.setMovable(false);

        minimap.setBounds(this.getWidth() / 10, this.getHeight() * 2 / 3, this.getWidth() * 4 / 5, this.getHeight() / 3 - this.getWidth() / 10);

        gridGroupHeroes = new GridGroup((float) 3) {{
            setWidth(this.getWidth() * 2 / 3);
            setHeight(this.getHeight() / 3);

            for (int i = 0; i < 5; i++) {
                VisImageTextButton spell1 = new VisImageTextButton("hero" + i, new SpriteDrawable(new Sprite(game.assetManager.get("resources/alienrsc.png", Texture.class))));
                addActor(spell1);
            }
        }};
        gridGroupHeroes.setItemSize((int) this.getWidth() * 2 / 3, (int) this.getWidth() / 5);

        gridGroupTowns = new GridGroup((float) 3) {{
            setWidth(this.getWidth() * 2 / 3);
            setHeight(this.getHeight() / 3);

            for (int i = 0; i < 7; i++) {
                VisImageTextButton spell1 = new VisImageTextButton("town" + i, new SpriteDrawable(new Sprite(game.assetManager.get("resources/alienrsc.png", Texture.class))));
                addActor(spell1);
            }
        }};
        gridGroupTowns.setItemSize((int) this.getWidth() * 2 / 3, (int) this.getWidth() / 5);

        scrollPaneHeroes = new VisScrollPane(gridGroupHeroes) {{
            setForceScroll(false, true);
            setScrollingDisabled(true, false);
            setVisible(true);
            setFadeScrollBars(false);
        }};
        scrollPaneHeroes.setBounds(topBar.getHeight(), 2 * topBar.getHeight(), this.getWidth() - 2 * topBar.getHeight(), this.getHeight() * 2 / 3 - 3 * topBar.getHeight());

        scrollPaneTowns = new VisScrollPane(gridGroupTowns) {{
            setForceScroll(false, true);
            setScrollingDisabled(true, false);
            setVisible(false);
            setFadeScrollBars(false);
        }};
        scrollPaneTowns.setBounds(topBar.getHeight(), 2 * topBar.getHeight(), this.getWidth() - 2 * topBar.getHeight(), this.getHeight() * 2 / 3 - 3 * topBar.getHeight());

        changeScrollPane = new VisTextButton("HEROES TOWNS", skin.get("text-button", VisTextButton.VisTextButtonStyle.class)) {{
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
        changeScrollPane.setBounds(topBar.getHeight(), topBar.getHeight(), this.getWidth() - 2 * topBar.getHeight(), topBar.getHeight());
        /*gridGroup = new GridGroup((float)3);
        gridGroup.setItemSize((int) (this.getWidth() - 2 * topBar.getHeight()), (int) (this.getHeight() * 2 / 3 - 2 * topBar.getHeight()));
        gridGroup.addActor(scrollPaneHeroes);
        gridGroup.addActor(scrollPaneTowns);
        gridGroup.setBounds(-this.getWidth(), 0, 2*(this.getWidth() - 2 * topBar.getHeight())+9, this.getHeight() * 2 / 3);

        scrollPane = new VisScrollPane(gridGroup){{
            setScrollingDisabled(false, true);
        }};
        scrollPane.setBounds(topBar.getHeight(), topBar.getHeight(), this.getWidth() - 2 * topBar.getHeight(), this.getHeight() *2/3 -2*topBar.getHeight());
        */

        rightBarDragImage = new Image(game.assetManager.get("ui/minimapDrag.png", Texture.class));

        rightBarDragImage.setBounds(-stage.getWidth() / 20, stage.getHeight() / 2 - stage.getHeight() / 6, stage.getWidth() / 20, stage.getHeight() / 3);
        this.addActor(minimap);
        this.addActor(changeScrollPane);
        this.addActor(scrollPaneHeroes);
        this.addActor(scrollPaneTowns);
        this.addActor(rightBarDragImage);
    }

    public void update() {
        if (this.hasActions()) {
            if (this.getX() < stage.getWidth() - this.getWidth() * 3 / 4)
                slided = true;
            if (this.getX() > stage.getWidth() - this.getWidth() / 4)
                slided = false;
        }
        if (scrollPaneHeroes.isScrollX() || scrollPaneTowns.isScrollX()) {
            Gdx.app.log("scroll", "true");
        }
    }

    public void resize(int width, int height) {
        int tempWidth, tempHeight;
        if (width > 1280) {
            tempWidth = 1280;
            tempHeight = 720;
            this.setHeight(height - topBar.getHeight());
        } else {
            tempWidth = width;
            tempHeight = height;
            this.setHeight(tempHeight - topBar.getHeight());
        }

        if (!slided)
            this.setPosition(width, 0);
        else
            this.setPosition(width - this.getWidth(), 0);

        minimap.setPosition(this.getWidth() / 10, this.getHeight() * 2 / 3);
        move.setPosition(-this.topBar.getHeight() * 3, this.getHeight() - this.topBar.getHeight() * 3);
        turn.setPosition(-this.topBar.getHeight() * 3, this.topBar.getHeight());

       /* gridGroupHeroes.setItemSize((int) this.getWidth() * 2 / 3, (int) this.getWidth() / 5);
        gridGroupTowns.setItemSize((int) this.getWidth() * 2 / 3, (int) this.getWidth() / 5);*/
        scrollPaneTowns.setPosition(topBar.getHeight(), 2 * topBar.getHeight());
        scrollPaneHeroes.setPosition(topBar.getHeight(), 2 * topBar.getHeight());
        changeScrollPane.setPosition(topBar.getHeight(), topBar.getHeight());

        rightBarDragImage.setPosition(-rightBarDragImage.getWidth(), height / 3);
    }
}