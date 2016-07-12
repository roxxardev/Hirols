package pl.pollub.hirols.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.managers.AnimationManager;

/**
 * Created by Marcin on 2016-04-14.
 */
public class AnimatedImage extends Actor {
    private Stage stage;
    private Vector2 positionToRender;
    private TextureRegionDrawable textureRegionDrawable;
    private Animation animation;
    private float stateTime;
    private boolean looped;

    public AnimatedImage(Stage stage, Animation animation, boolean looped){
        this.stage = stage;
        stateTime = 0;
        this.looped = looped;
        this.animation = animation;
        textureRegionDrawable = new TextureRegionDrawable(animation.getKeyFrame(stateTime));
        this.positionToRender = new Vector2(0,0);
    }

    public void setPositionToRender(Vector2 positionToRender){
        this.positionToRender = positionToRender;
    }

    public void update(float delta){
        if(this.isVisible())
            stateTime += delta;
        else
            stateTime = 0;
        textureRegionDrawable.setRegion(animation.getKeyFrame(stateTime, false));
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        textureRegionDrawable.draw(stage.getBatch(), positionToRender.x, stage.getHeight() - positionToRender.y, 128, 128);
    }
}
