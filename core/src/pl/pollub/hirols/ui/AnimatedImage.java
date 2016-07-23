package pl.pollub.hirols.ui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Marcin on 2016-04-14.
 */
public class AnimatedImage extends Actor {
    private TextureRegionDrawable textureRegionDrawable;
    private Animation animation;
    private float previousStateTime, stateTime;
    private boolean looped;

    public AnimatedImage( Animation animation, boolean looped){
        stateTime = 0;
        previousStateTime = 0;
        this.looped = looped;
        this.animation = animation;
        textureRegionDrawable = new TextureRegionDrawable(animation.getKeyFrame(stateTime));

        this.setWidth(animation.getKeyFrame(stateTime).getRegionWidth());
        this.setHeight(animation.getKeyFrame(stateTime).getRegionHeight());
    }
    public void update(float delta){
        if(this.isVisible()) {
            stateTime += delta;
        }else{
            stateTime = 0;
        }
        if(previousStateTime != stateTime)
            textureRegionDrawable.setRegion(animation.getKeyFrame(stateTime, looped));
        previousStateTime = stateTime;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        textureRegionDrawable.draw(batch, this.getX() , this.getStage().getHeight() - this.getY(), this.getWidth(),this.getHeight());
    }
}
