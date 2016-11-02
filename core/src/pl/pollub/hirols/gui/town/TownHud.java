package pl.pollub.hirols.gui.town;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.gui.TopBar;

/**
 * Created by erykp_000 on 2016-10-30.
 */

public class TownHud extends Hud {
    private TopBar topBar;

    private Garrison inTown, outTown;

    private VisImageButton exitButton;

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

        stage.addActor(townBackground);
        stage.addActor(topBar);
        stage.addActor(inTown);
        stage.addActor(outTown);
    }

    private void moveTextLabelBelowImage(VisImageTextButton button) {
        button.getImage().setScaling(Scaling.stretch);
        button.clearChildren();
        button.add(button.getImage()).expand().fill().row();
        button.add(button.getLabel());
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
        topBar.resize(width,height);

        inTown.resize(width,height);
        outTown.resize(width,height);
    }

    private class Garrison extends Table {
        private GridGroup units = new GridGroup();
        private VisImageTextButton heroButton;
        private ButtonGroup buttonGroup;
        private boolean heroOnLeft;

        public Garrison(ButtonGroup<VisImageTextButton> buttonGroup, boolean heroOnLeft) {
            this.buttonGroup = buttonGroup;
            this.heroOnLeft = heroOnLeft;

            units.setSpacing(2);

            for(int i = 0; i < 5; i++) {
                VisImageTextButton in = new VisImageTextButton("Dupa", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
                units.addActor(in);
                buttonGroup.add(in);
                moveTextLabelBelowImage(in);
            }

            heroButton = new VisImageTextButton("Hero", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
            moveTextLabelBelowImage(heroButton);
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

    }
}

