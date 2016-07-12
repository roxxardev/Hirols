package pl.pollub.hirols.ui.playScreenUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import pl.pollub.hirols.Hirols;

/**
 * Created by krol22 on 24.02.16.
 */
public class RightBar extends VisTable {

    private final Stage stage;
    private Skin skin;
    private BitmapFont font;
    private VisTable topBar;
    private boolean slided = false;

    private VisTextButton move, turn;

    private VisWindow minimap;

    public RightBar(Hirols game,final Stage stage, VisTable topBar, boolean debug, Texture texture){
        this.stage = stage;
        this.topBar = topBar;

        this.skin = game.hudManager.skin;
        this.font = game.hudManager.font;

        Sprite sprite = new Sprite(texture);
        sprite.setColor(0, 0, 0, 0.3f);

        this.setWidth(this.stage.getWidth() / 4);
        this.setHeight(this.stage.getHeight() - this.topBar.getHeight());
        this.setPosition(this.stage.getWidth() - this.getWidth() / 8, 0);
        this.setBackground(new SpriteDrawable(sprite));
        this.setTouchable(Touchable.enabled);

        //TODO dodac mozliwosc przesuwania panelu bocznego w zaleznosci od predkosci
        this.addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                RightBar.this.clearActions();
                RightBar.this.setX(RightBar.this.getX() + deltaX);
                if (RightBar.this.getX() < stage.getWidth() - RightBar.this.getWidth()) {
                    RightBar.this.setX(stage.getWidth() - RightBar.this.getWidth());
                    slided = true;
                } else if (RightBar.this.getX() > stage.getWidth() - RightBar.this.getWidth() / 8) {
                    RightBar.this.setX(stage.getWidth() - RightBar.this.getWidth() / 8);
                    slided = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(RightBar.this.getX() <= stage.getWidth() - RightBar.this.getWidth()/4 && !slided || RightBar.this.getX() < stage.getWidth() - RightBar.this.getWidth()*3/4) {
                    RightBar.this.addAction(Actions.moveTo(stage.getWidth() - RightBar.this.getWidth(), 0, 0.3f));
                }
                else if(RightBar.this.getX() >= stage.getWidth() - RightBar.this.getWidth()*3/4 && slided || RightBar.this.getX() > stage.getWidth() - RightBar.this.getWidth()/4){
                    RightBar.this.addAction(Actions.moveTo(stage.getWidth() - RightBar.this.getWidth()/8, 0, 0.3f));
                }
            }
        });

        move = new VisTextButton("Move", skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        turn = new VisTextButton("Turn", skin.get("button-custom", VisTextButton.VisTextButtonStyle.class));
        move.setBounds(-this.topBar.getHeight()*3,this.getHeight()-this.topBar.getHeight()*3, this.topBar.getHeight()*2,this.topBar.getHeight()*2);
        turn.setBounds(-this.topBar.getHeight()*3,this.topBar.getHeight(), this.topBar.getHeight()*2,this.topBar.getHeight()*2);

        minimap = new VisWindow("MINIMAP");
        minimap.setKeepWithinStage(false);
        minimap.setMovable(false);

        minimap.setBounds(this.getWidth() / 10, this.getHeight() * 2 / 3, this.getWidth() * 4 / 5, this.getHeight() / 3 - this.getWidth() / 10);



        this.setDebug(debug);

        this.left().center();
        this.addActor(minimap);
        this.addActor(move);
        this.addActor(turn);
    }

    public void update() {
        if(this.hasActions()) {
            if (this.getX() < stage.getWidth() - this.getWidth() * 3 / 4)
                slided = true;
            if (this.getX() > stage.getWidth() - this.getWidth() / 4)
                slided = false;
        }
    }

    public void resize(int width, int height){
        if(width > 640)
            this.setWidth(width / 4);
        else
            this.setWidth(640 / 4);

        this.setHeight(height - topBar.getHeight());

        if(!slided)
            this.setPosition(width - topBar.getHeight(), 0);
        else
            this.setPosition(width - this.getWidth(), 0);

        minimap.setBounds(this.getWidth() / 10, this.getHeight()*2/3 , this.getWidth()*4/5, this.getHeight()/3 - this.getWidth()/10);
        move.setBounds(-this.topBar.getHeight()*3,this.getHeight()-this.topBar.getHeight()*3, this.topBar.getHeight()*2,this.topBar.getHeight()*2);
        turn.setBounds(-this.topBar.getHeight()*3,this.topBar.getHeight(), this.topBar.getHeight()*2,this.topBar.getHeight()*2);

    }
}
