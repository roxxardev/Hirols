package pl.pollub.hirols.gui.battle;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.gui.Hud;

/**
 * Created by erykp_000 on 2016-08-09.
 */
public class BattleHud extends Hud {

    private VisImageTextButton waitButton, blockButton, magicBookButton, runButton;

    public BattleHud(Hirols game, Viewport viewport) {
        super(game, viewport);

        createActors();
    }

    private void createActors() {
        waitButton = new VisImageTextButton("Wait", new VisImageTextButton.VisImageTextButtonStyle(game.hudManager.skin.get("image-text-button", VisImageTextButton.VisImageTextButtonStyle.class)));
        blockButton = new VisImageTextButton("Block", new VisImageTextButton.VisImageTextButtonStyle(game.hudManager.skin.get("image-text-button", VisImageTextButton.VisImageTextButtonStyle.class)));
        magicBookButton = new VisImageTextButton("Magic Book", new VisImageTextButton.VisImageTextButtonStyle(game.hudManager.skin.get("image-text-button", VisImageTextButton.VisImageTextButtonStyle.class)));
        runButton = new VisImageTextButton("Run", new VisImageTextButton.VisImageTextButtonStyle(game.hudManager.skin.get("image-text-button", VisImageTextButton.VisImageTextButtonStyle.class)));

        waitButton.setBounds(stage.getHeight() / 20, stage.getHeight() / 20, stage.getWidth() / 20, stage.getWidth() / 20);
        blockButton.setBounds(waitButton.getX(), stage.getHeight() - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());
        magicBookButton.setBounds(stage.getWidth() - waitButton.getX() - waitButton.getWidth(), stage.getHeight() - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());
        runButton.setBounds(stage.getWidth() - waitButton.getX() - waitButton.getWidth(), waitButton.getY(), waitButton.getWidth(), waitButton.getHeight());


        stage.addActor(waitButton);
        stage.addActor(blockButton);
        stage.addActor(magicBookButton);
        stage.addActor(runButton);
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
