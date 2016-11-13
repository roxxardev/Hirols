package pl.pollub.hirols.gui.town;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.gui.TopBar;
import pl.pollub.hirols.gui.UnitsGrid;
import pl.pollub.hirols.managers.HudManager;

/**
 * Created by erykp_000 on 2016-10-30.
 */

public class TownHud extends Hud {
    private TopBar topBar;

    private Garrison inTown, outTown;

    private VisImageTextButton exitButton;
    private boolean exitRequest;

    public TownHud(Hirols game) {
        super(game);

        createActors();
    }

    private void createActors() {
        topBar = new TopBar(game, stage);

        ButtonGroup<VisImageTextButton> buttonGroup = new ButtonGroup<VisImageTextButton>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.setUncheckLast(false);

        Image townBackground = new Image(game.assetManager.get("towns/snow-town.png", Texture.class));
        townBackground.setFillParent(true);

        inTown = new Garrison(buttonGroup,false);
        outTown = new Garrison(buttonGroup, true);

        exitButton = new VisImageTextButton("Exit", game.hudManager.skin.get("image-text-button", VisImageTextButton.VisImageTextButtonStyle.class));
        exitButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112)));
        HudManager.moveTextLabelBelowImage(exitButton, Scaling.fit);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitRequest = true;
            }
        });

        stage.addActor(townBackground);
        stage.addActor(topBar);
        stage.addActor(inTown);
        stage.addActor(outTown);
        stage.addActor(exitButton);
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
        topBar.resize(width,height);

        inTown.resize(width,height);
        outTown.resize(width,height);

        float buttonSize = width<height ? width/10 : height/10;
        float imagePadding = buttonSize/10;

        exitButton.setBounds(buttonSize, height - buttonSize*2, buttonSize, buttonSize);
        exitButton.pad(imagePadding);
    }

    public boolean isExitRequest() {
        boolean temp = exitRequest;
        exitRequest = false;
        return temp;
    }

    private class Garrison extends Table {
        private UnitsGrid units;
        private VisImageTextButton heroButton;
        private ButtonGroup buttonGroup;
        private boolean heroOnLeft;

        public Garrison(ButtonGroup<VisImageTextButton> buttonGroup, boolean heroOnLeft) {
            this.buttonGroup = buttonGroup;
            this.heroOnLeft = heroOnLeft;

            units = new UnitsGrid(game, buttonGroup);

            heroButton = new VisImageTextButton("Hero", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
            HudManager.moveTextLabelBelowImage(heroButton, Scaling.stretch);
            buttonGroup.add(heroButton);

            addActor(units);
            addActor(heroButton);
        }

        public void resize(float width, float height) {
            int divider = 20;
            int buttonSize = (int) (width / divider);
            float spacing = units.getSpacing();

            units.setItemSize(buttonSize);
            units.setSize((buttonSize+spacing)*5+spacing , buttonSize + spacing*2);

            if(!heroOnLeft) {
                setSize(units.getWidth(), units.getHeight()*2);
                setPosition(buttonSize,buttonSize);
                heroButton.setBounds(units.getWidth() + spacing, 0 + spacing, buttonSize*2, buttonSize*2);
            } else {
                setSize(units.getWidth(), units.getHeight()*2);
                setPosition(stage.getWidth() - getWidth() - buttonSize, buttonSize);
                heroButton.setSize(buttonSize*2,buttonSize*2);
                heroButton.setPosition(-heroButton.getWidth() - spacing, 0 + spacing);
            }
        }

        public void update(HeroDataComponent heroDataComponent) {
            units.update(heroDataComponent);
        }

    }
}

