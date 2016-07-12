package pl.pollub.hirols.ui.battleScreenUI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import pl.pollub.hirols.Hirols;

/**
 * Created by Marcin on 2016-04-21.
 */
public class BattleScreenHud implements Disposable {
    private Hirols game;
    public Stage stage;
    private Skin skin;



    private VisLabel.LabelStyle labelStyle;
    private VisTable informationTable;
    private VisTextButton waitButton;
    private VisTextButton blockButton;
    private VisTextButton magicBookButton;

    public BattleScreenHud(Hirols game) {
        this.game = game;
        this.skin = game.hudManager.skin;

        OrthographicCamera gameCam;

        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false);

        Viewport guiPort = new ScreenViewport(gameCam);

        stage = new Stage(guiPort, game.batch);

        labelStyle = skin.get("label-white", Label.LabelStyle.class);

        informationTable = new VisTable();

        stage.addActor(informationTable);

        informationTable.setBounds(stage.getWidth() / 3, 0, stage.getWidth() / 3, stage.getHeight() / 20);

        waitButton = new VisTextButton("Wait", skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        waitButton.setBounds(stage.getHeight() / 20, stage.getHeight() / 20, stage.getWidth() / 20, stage.getWidth() / 20);

        blockButton = new VisTextButton("Block", skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        blockButton.setBounds(waitButton.getX(), stage.getHeight() - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());

        magicBookButton = new VisTextButton("Magic Book", skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        magicBookButton.setBounds(stage.getWidth() - waitButton.getX() - waitButton.getWidth(), stage.getHeight() - waitButton.getY() - waitButton.getHeight(), waitButton.getWidth(), waitButton.getHeight());

        stage.addActor(waitButton);
        stage.addActor(blockButton);
        stage.addActor(magicBookButton);

        showInformation("Cos sie pojawilo na ekranie, jakies informacje");
    }

    public void showInformation(String string) {
        informationTable.add(new VisLabel(string, labelStyle));
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
