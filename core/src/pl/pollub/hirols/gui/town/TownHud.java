package pl.pollub.hirols.gui.town;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;

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

    public TownHud(Hirols game) {
        super(game);

        createActors();

        //stage.setDebugAll(true);
    }

    private void createActors() {
        topBar = new TopBar(game, stage);

        ButtonGroup<VisImageButton> buttonGroup = new ButtonGroup<VisImageButton>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.setUncheckLast(false);
        inTown = new GridGroup(40f,2);
        outTown = new GridGroup(40f,2);

        for(int i = 0; i < 5; i++) {
            VisImageButton in = new VisImageButton(new VisImageButton.VisImageButtonStyle(game.hudManager.skin.get("image-button", VisImageButton.VisImageButtonStyle.class)));
            in.getStyle().imageChecked = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112)));
            inTown.addActor(in);
            buttonGroup.add(in);

            VisImageButton out = new VisImageButton(new VisImageButton.VisImageButtonStyle(game.hudManager.skin.get("image-button", VisImageButton.VisImageButtonStyle.class)));
            out.getStyle().imageChecked = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112)));
            outTown.addActor(out);
            buttonGroup.add(out);
        }

        inTown.setDebug(true);
        outTown.setDebug(true);
        inTown.setPosition(100,400);
        inTown.setSize(300,50);

        stage.addActor(topBar);
        stage.addActor(inTown);
        stage.addActor(outTown);
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
        topBar.resize(width,height);


        inTown.setBounds(topBar.getHeight(),topBar.getHeight(), topBar.getHeight()*10 + 2, topBar.getHeight()*2);
        inTown.setItemSize((int)topBar.getHeight()*2 - 2);

        outTown.setBounds(stage.getWidth() - topBar.getHeight()*11, topBar.getHeight(), topBar.getHeight()*10 + 2, topBar.getHeight()*2);
        outTown.setItemSize((int)topBar.getHeight()*2 - 2);

    }
}

