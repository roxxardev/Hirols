package pl.pollub.hirols.ui.townScreenUI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.pollub.hirols.Hirols;

/**
 * Created by Marcin on 2016-04-22.
 */
public class TownScreenHud implements Disposable {
    private Hirols game;
    private Stage stage;
    private Skin skin;
    private Label.LabelStyle labelStyle;

    public TownScreenHud(Hirols game){
        stage = new Stage();

        this.game = game;
        this.skin = game.hudManager.skin;

        OrthographicCamera gameCam;

        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false);

        Viewport guiPort = new ScreenViewport(gameCam);

        stage = new Stage(guiPort, game.batch);

        labelStyle = skin.get("label-white", Label.LabelStyle.class);
    }
    @Override
    public void dispose(){
        stage.dispose();
    }
}
