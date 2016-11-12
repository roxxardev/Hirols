package pl.pollub.hirols.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.managers.HudManager;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public class UnitsGrid extends GridGroup {

    private Hirols game;

    private VisImageTextButton[] unitButtons = new VisImageTextButton[5];

    public UnitsGrid(Hirols game, ButtonGroup<VisImageTextButton> buttonGroup) {
        this.game = game;
        setSpacing(2);
        for(int i = 0; i < 5; i++) {
            VisImageTextButton in = new VisImageTextButton("Empty", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
            addActor(in);
            unitButtons[i] = in;
            buttonGroup.add(in);
            HudManager.moveTextLabelBelowImage(in, Scaling.stretch);
        }
    }

    public void update(HeroDataComponent heroDataComponent) {
        HeroDataComponent.Army.Squad[] squads = heroDataComponent.army.getSquads();
        for(int i = 0; i < 5; i++) {
            VisImageTextButton button = unitButtons[i];
            if(squads[i] == null) {
                button.getLabel().setText("Empty");
                button.getStyle().imageUp = null;
                continue;
            }
            button.getLabel().setText(squads[i].getUnit().name + " ["+ squads[i].getQuantity()+"]");
        }
    }



}
