package pl.pollub.hirols.ui.playScreenUI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import pl.pollub.hirols.Hirols;

/**
 * Created by krol22 on 24.02.16.
 */
public class LeftBar extends VisTable {
    private VisTable topBar;
    private boolean slided = false;
    private Skin skin;

    private VisTextButton optionsButton;
    private VisTextButton saveButton;
    private VisTextButton loadButton;
    private VisTextButton menuButton;

    public LeftBar(Hirols game, final Stage stage, VisTable topBar, boolean debug){
        this.topBar = topBar;
        this.skin = game.hudManager.skin;

        this.setBounds(-stage.getWidth()/4, 0, stage.getWidth() / 4, stage.getHeight() - this.topBar.getHeight());

        //TODO dodac mozliwosc przesuwania panelu bocznego w zaleznosci od predkosci
        this.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                LeftBar.this.clearActions();
                LeftBar.this.setX(LeftBar.this.getX() + deltaX);
                //leftbar x nie moze byc wysuniety bardziej niz 0 i mniej niz -leftbar.width
                if (LeftBar.this.getX() < -LeftBar.this.getWidth()) {
                    LeftBar.this.setX(-LeftBar.this.getWidth());
                    slided = false;
                } else if (LeftBar.this.getX() > 0) {
                    LeftBar.this.setX(0);
                    slided = true;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //nadaj ackje moveTo w zaleznosci od slided
                if (LeftBar.this.getX() <= -LeftBar.this.getWidth() / 4 && slided || LeftBar.this.getX() < -LeftBar.this.getWidth() + LeftBar.this.getWidth() / 4) {
                    LeftBar.this.addAction(Actions.moveTo(-LeftBar.this.getWidth(), 0, 0.3f));
                } else if (LeftBar.this.getX() >= -LeftBar.this.getWidth() + LeftBar.this.getWidth() / 4 && !slided || LeftBar.this.getX() > -LeftBar.this.getWidth() / 4) {
                    LeftBar.this.addAction(Actions.moveTo(0, 0, 0.3f));
                }
            }
        });

        VisTextButton dragButton = new VisTextButton("DRAG");
        dragButton.setBounds(0, 0, this.getWidth() + 40, 40);

        optionsButton = new VisTextButton("OPTIONS",skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        saveButton = new VisTextButton("SAVE",skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        loadButton = new VisTextButton("LOAD",skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        menuButton = new VisTextButton("MENU",skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));

        optionsButton.setBounds(1, this.getHeight() - this.getHeight() / 3, this.getWidth() * 2 / 3, this.getHeight() / 8);
        saveButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 12 / 10, optionsButton.getWidth(), optionsButton.getHeight());
        loadButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 24 / 10, optionsButton.getWidth(), optionsButton.getHeight());
        menuButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 36 / 10, optionsButton.getWidth(), optionsButton.getHeight());

        this.addActor(dragButton);
        this.addActor(optionsButton);
        this.addActor(saveButton);
        this.addActor(loadButton);
        this.addActor(menuButton);

        this.setDebug(debug);

    }

    public void update(){
        //jezeli pozycja x leftbara jest mniejsza od -3/4 leftbar width to schowany
        //jezeli pozycja x leftbara jest wieksza niz -1/4 leftbar width to wysuniety
        if(this.hasActions()) {
            if (this.getX() < -this.getWidth()*3/ 4 && slided) {
                slided = false;
            }
            if (this.getX() > -this.getWidth() / 4 && !slided) {
                slided = true;
            }
        }
    }

    public void resize(int width, int height){

        this.setWidth(width / 4);

        this.setHeight(height-this.topBar.getHeight());

        if(!slided)
            this.setPosition(-this.getWidth(),0);
        else
            this.setPosition(0, 0);

        optionsButton.setBounds(1, this.getHeight() - this.getHeight() / 3, this.getWidth() * 2 / 3, this.getHeight() / 8);
        saveButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 12 / 10, optionsButton.getWidth(), optionsButton.getHeight());
        loadButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 24 / 10, optionsButton.getWidth(), optionsButton.getHeight());
        menuButton.setBounds(optionsButton.getX(), optionsButton.getY() - optionsButton.getHeight() * 36 / 10, optionsButton.getWidth(), optionsButton.getHeight());
    }

}
