package pl.pollub.hirols.gui.town;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
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

    private VisImageButton exitButton;
    private GridGroup inTown, outTown;

    private Table inTownTable, outTownTable;
    private VisImageTextButton heroInTown, heroOutOfTown;

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
        inTown = new GridGroup(40f,2);
        outTown = new GridGroup(40f,2);

        for(int i = 0; i < 5; i++) {
            VisImageTextButton in = new VisImageTextButton("Dupa", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
            inTown.addActor(in);
            buttonGroup.add(in);
            in.getImage().setScaling(Scaling.stretch);
            in.clearChildren();
            in.add(in.getImage()).expand().fill().row();
            in.add(in.getLabel());

            VisImageTextButton out = new VisImageTextButton("Dupa", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
            out.getImage().setScaling(Scaling.stretch);
            out.clearChildren();
            out.add(out.getImage()).expand().fill().row();
            out.add(out.getLabel());
            outTown.addActor(out);
            buttonGroup.add(out);
        }

        heroInTown = new VisImageTextButton("Hero", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
        heroInTown.getImage().setScaling(Scaling.stretch);
        heroInTown.clearChildren();
        heroInTown.add(heroInTown.getImage()).expand().fill().row();
        heroInTown.add(heroInTown.getLabel());
        buttonGroup.add(heroInTown);

        inTownTable = new Table();
        inTownTable.addActor(inTown);
        inTownTable.addActor(heroInTown);

        heroOutOfTown = new VisImageTextButton("Hero", game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class));
        heroOutOfTown.getImage().setScaling(Scaling.stretch);
        heroOutOfTown.clearChildren();
        heroOutOfTown.add(heroOutOfTown.getImage()).expand().fill().row();
        heroOutOfTown.add(heroOutOfTown.getLabel());
        buttonGroup.add(heroOutOfTown);

        outTownTable = new Table();
        outTownTable.addActor(outTown);
        outTownTable.addActor(heroOutOfTown);

        Image townBackground = new Image(game.assetManager.get("towns/snow-town.png", Texture.class));
        townBackground.setFillParent(true);
        stage.addActor(townBackground);
        stage.addActor(topBar);
        stage.addActor(inTownTable);
        stage.addActor(outTownTable);
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
        topBar.resize(width,height);


        inTown.setSize(topBar.getHeight()*10 + 2, topBar.getHeight()*2);
        inTown.setItemSize((int)topBar.getHeight()*2 - 2);
        inTownTable.setBounds(topBar.getHeight(),topBar.getHeight(),inTown.getWidth(), inTown.getHeight()*2);
        heroInTown.setBounds(inTownTable.getWidth(), 0, inTown.getItemWidth()*2, inTown.getHeight()*2);

        outTown.setSize(topBar.getHeight()*10 + 2, topBar.getHeight()*2);
        outTown.setItemSize((int)topBar.getHeight()*2 - 2);
        outTownTable.setBounds(stage.getWidth() - topBar.getHeight()*11, topBar.getHeight(), inTownTable.getWidth(), inTownTable.getHeight());
        heroOutOfTown.setBounds(-outTown.getItemWidth()*2, 0, outTown.getItemWidth()*2, outTown.getHeight()*2);
    }
}

