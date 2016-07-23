package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.Map;

/**
 * Created by Marcin on 2015-12-07.
 */
public class AnimationComponent implements Component {
    public String animationName;
    public String renderedAnimationName;
    public String direction;
    public float stateTime;
    public Map<String, Map<String,Animation>> animationMap;
    public boolean looping = false;

    public AnimationComponent(String animationName, String direction, Map<String, Map<String,Animation>> animationMap, boolean looping){
        this.animationName = animationName;
        this.direction = direction;
        this.animationMap = animationMap;
        renderedAnimationName = animationName;
        stateTime = .0f;
        this.looping = looping;
    }

    public AnimationComponent(String animationName, String direction, Map<String, Map<String,Animation>> animationMap, boolean looping, Float stateTime){
        this.animationName = animationName;
        this.direction = direction;
        this.animationMap = animationMap;
        renderedAnimationName = animationName;
        this.stateTime = stateTime;
        this.looping = looping;
    }

}
