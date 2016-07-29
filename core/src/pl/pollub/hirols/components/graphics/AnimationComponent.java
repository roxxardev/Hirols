package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.Map;

import pl.pollub.hirols.Animation.AnimationSet;
import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by Marcin on 2015-12-07.
 */
public class AnimationComponent implements Component {

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

}
