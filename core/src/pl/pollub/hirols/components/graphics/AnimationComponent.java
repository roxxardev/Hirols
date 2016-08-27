package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import pl.pollub.hirols.animation.AnimationSet;

/**
 * Created by Marcin on 2015-12-07.
 */
public class AnimationComponent implements Component, Pool.Poolable {

    public AnimationSet animationSet;
    public float stateTime = 0.0f;
    public boolean looping = false;

    public AnimationComponent(AnimationSet animationSet, boolean looping){
        this.animationSet = animationSet;
        this.looping = looping;
    }

    public AnimationComponent(AnimationSet animationSet, boolean looping, Float stateTime){
        this(animationSet,looping);
        this.stateTime = stateTime;
    }

    @Override
    public void reset() {
        animationSet = null;
        stateTime = 0.0f;
        looping = false;
    }
}
