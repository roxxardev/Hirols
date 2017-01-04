package pl.pollub.hirols.gui.town;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.gui.AnimatedImage;
import pl.pollub.hirols.managers.AnimationManager;
import pl.pollub.hirols.managers.UnitsManager;
import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public class Barracks extends TownBuilding {
    private Hirols game;

    public Barracks(Hirols game) {
        super("Koszary");
        this.game = game;

        UnitsRecruitTable units1 = new UnitsRecruitTable(game.unitsManager.orc, game.unitsManager.orcWarior);
        UnitsRecruitTable units2 = new UnitsRecruitTable(game.unitsManager.smallWyvern, game.unitsManager.wyvern);
        UnitsRecruitTable units3 = new UnitsRecruitTable(game.unitsManager.shaman, game.unitsManager.oldShaman);

        add(units1).fill();

        add(units3).fill().row();
        add(units2).fill();
    }

    @Override
    public boolean upgrade() {
        return false;
    }

    private class UnitsRecruitTable extends Table {

        public UnitsRecruitTable(UnitsManager.Unit basicUnit, UnitsManager.Unit upgradedUnit) {
            AnimationManager.AnimationInformation animationInformation = basicUnit.animationInformation;
            AnimationManager.AnimationProperties animationProperties = animationInformation.animationPropertiesMap.get(AnimationType.RUN);
            Animation animation = AnimationManager.createAnimation(animationProperties.getDirections(),game.assetManager.get(animationProperties.getPath(), Texture.class), animationProperties.getCols(), animationProperties.getRows(), animationProperties.getTime())
                    .get(Direction.SE);
            AnimatedImage unitAnimatedImage = new AnimatedImage(animation, true);
            add(unitAnimatedImage).expand().fill();

            animationInformation = upgradedUnit.animationInformation;
            animationProperties = animationInformation.animationPropertiesMap.get(AnimationType.RUN);
            animation = AnimationManager.createAnimation(animationProperties.getDirections(),game.assetManager.get(animationProperties.getPath(), Texture.class), animationProperties.getCols(), animationProperties.getRows(), animationProperties.getTime())
                    .get(Direction.SE);
            unitAnimatedImage = new AnimatedImage(animation, true);
            add(unitAnimatedImage).expand().fill().row();

            VisTextButton.VisTextButtonStyle textButtonStyle = new VisTextButton.VisTextButtonStyle(game.hudManager.skin.get("text-button", VisTextButton.VisTextButtonStyle.class));
            TextButton recruitButton = new TextButton("Recruit one unit", textButtonStyle);
            add(recruitButton).colspan(2).pad(20).expand().fill();
        }
    }
}
