package pl.pollub.hirols.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.managers.AnimationManager;
import pl.pollub.hirols.managers.HudManager;
import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public class UnitsGrid extends GridGroup {

    private Hirols game;

    private UnitsGridButton[] unitButtons = new UnitsGridButton[5];

    public UnitsGrid(Hirols game, ButtonGroup<VisImageTextButton> buttonGroup) {
        this.game = game;
        setSpacing(2);
        for(int i = 0; i < 5; i++) {
            UnitsGridButton in = new UnitsGridButton("Empty", new VisImageTextButton.VisImageTextButtonStyle(game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class)));
            addActor(in);
            unitButtons[i] = in;
            buttonGroup.add(in);
            HudManager.moveTextLabelBelowImage(in, Scaling.fit);
            in.getImageCell().expand();
            in.getLabel().setWrap(true);
        }
    }

    public void update(HeroDataComponent heroDataComponent) {
        if(heroDataComponent == null) {
            for(int i = 0; i < 5; i++) {
                UnitsGridButton button = unitButtons[i];
                button.getLabel().setText("Empty");
                button.removeUnitImage();
            }
            return;
        }

        HeroDataComponent.Army.Squad[] squads = heroDataComponent.army.getSquads();
        for(int i = 0; i < 5; i++) {
            UnitsGridButton button = unitButtons[i];
            if(squads[i] == null) {
                button.getLabel().setText("Empty");
                button.removeUnitImage();
                continue;
            }
            button.getLabel().setText(squads[i].getUnit().name + " "+squads[i].getQuantity());
            AnimationManager.AnimationInformation animationInformation = squads[i].getUnit().animationInformation;
            AnimationManager.AnimationProperties animationProperties = animationInformation.animationPropertiesMap.get(AnimationType.STAND);
            Animation animation = AnimationManager.createAnimation(animationProperties.getDirections(),game.assetManager.get(animationProperties.getPath(), Texture.class), animationProperties.getCols(), animationProperties.getRows(), animationProperties.getTime())
                    .get(Direction.S);
            Image image = new AnimatedImage(animation, true);
            int offsetTargetSize = 96;
            image.setPosition(animationInformation.offset.x - 0.6f* (offsetTargetSize - getItemWidth()), animationInformation.offset.y  + button.getLabel().getHeight());
            button.setUnitImage(image);
        }
    }

    private class UnitsGridButton extends VisImageTextButton {
        Image unitImage;

        public UnitsGridButton(String text, VisImageTextButtonStyle style) {
            super(text, style);
        }

        void setUnitImage(Image image) {
            removeUnitImage();
            this.unitImage = image;
            addActor(unitImage);
        }

        void removeUnitImage() {
            removeActor(unitImage);
        }
    }

}
