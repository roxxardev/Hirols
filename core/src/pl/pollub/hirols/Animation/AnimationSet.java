package pl.pollub.hirols.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.Map;

import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by erykp_000 on 2016-07-29.
 */
public class AnimationSet {
    private AnimationType animationType;
    private Direction direction;
    private Map<AnimationType, Map<Direction,com.badlogic.gdx.graphics.g2d.Animation>> animationMap;

    private com.badlogic.gdx.graphics.g2d.Animation currentAnimation;

    public AnimationSet(AnimationType animationType, Direction direction, Map<AnimationType, Map<Direction, com.badlogic.gdx.graphics.g2d.Animation>> animationMap) {
        this.animationType = animationType;
        this.direction = direction;
        this.animationMap = animationMap;

        currentAnimation = animationMap.get(animationType).get(direction);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        currentAnimation = animationMap.get(animationType).get(direction);
    }

    public void changeAnimationType(AnimationType animationType) {
        this.animationType = animationType;
        currentAnimation = animationMap.get(animationType).get(direction);
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public Direction getDirection() {
        return direction;
    }

    public Map<AnimationType, Map<Direction, Animation>> getAnimationMap() {
        return animationMap;
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }
}
