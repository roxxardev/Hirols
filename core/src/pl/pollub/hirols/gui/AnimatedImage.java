package pl.pollub.hirols.gui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by erykp_000 on 2016-08-19.
 */
public class AnimatedImage extends Image {

    private TextureRegionDrawable textureRegion;
    private Animation animation;
    private float stateTime = 0.0f;
    private boolean looping;

    public AnimatedImage(Animation animation, boolean looping) {
        this.animation = animation;
        this.looping = looping;

        textureRegion = new TextureRegionDrawable(animation.getKeyFrame(stateTime));
        setDrawable(textureRegion);
        setScaling(Scaling.fit);
        setSize(animation.getKeyFrame(stateTime).getRegionWidth(), animation.getKeyFrame(stateTime).getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime = isVisible() ? stateTime + delta : 0;
        textureRegion.setRegion(animation.getKeyFrame(stateTime,looping));
    }
}
