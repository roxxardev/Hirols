package pl.pollub.hirols.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by Marcin on 2016-01-15.
 */

public class AnimationManager {
    public static Map<Direction,Animation> createAnimation(Direction[] directions, Texture texture, int FRAME_COLS, int FRAME_ROWS, float frameDuration){
        Map<Direction,Animation> animationDirectionsMap = new HashMap<Direction, Animation>();
        TextureRegion[][] frames = TextureRegion.split(texture,
                texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        for(int i =0 ;i <FRAME_ROWS; i++)
            animationDirectionsMap.put(directions[i], new Animation(frameDuration, frames[i]));

        return animationDirectionsMap;
    }

    public static Animation createAnimation(Texture texture, int FRAME_COLS, int FRAME_ROWS, float frameDuration){
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        TextureRegion[] frames = new TextureRegion[FRAME_COLS*FRAME_ROWS];
        int index = 0;
        for(int i = 0 ; i < FRAME_ROWS; i++)
            for (int j = 0 ; j < FRAME_COLS; j++)
                frames[index++]  = tmp[i][j];

        return new Animation(frameDuration, frames);
    }

    public static Map<AnimationType, Map<Direction, Animation>> createUnitAnimationMaps(UnitsManager.Unit unit, Hirols game) {
        Map<AnimationType, Map<Direction, Animation>> animationMap = new HashMap<AnimationType, Map<Direction, Animation>>();

        for(Map.Entry<AnimationType, AnimationProperties> entry: unit.animationInformation.animationPropertiesMap.entrySet()) {
            AnimationType animationType = entry.getKey();
            AnimationProperties animationProperties = entry.getValue();

            if(game.assetManager.isLoaded(animationProperties.getPath(), Texture.class))
            animationMap.put(animationType, createAnimation(animationProperties.getDirections(), game.assetManager.get(animationProperties.getPath(), Texture.class), animationProperties.getCols(), animationProperties.getRows(), animationProperties.getTime()));
        }

        return animationMap;
    }

    public static Map<AnimationType, Map<Direction, Animation>> createHeroAnimationMap(Hirols game, UnitsManager.Hero hero) {
        Map<AnimationType, Map<Direction, Animation>> animationMap = new HashMap<AnimationType, Map<Direction, Animation>>();

        for(Map.Entry<AnimationType, AnimationProperties> entry: hero.animationInformation.animationPropertiesMap.entrySet()) {
            AnimationType animationType = entry.getKey();
            AnimationProperties animationProperties = entry.getValue();

            if(game.assetManager.isLoaded(animationProperties.getPath(), Texture.class))
            animationMap.put(animationType, createAnimation(animationProperties.getDirections(), game.assetManager.get(animationProperties.getPath(), Texture.class), animationProperties.getCols(), animationProperties.getRows(), animationProperties.getTime()));
        }

        return animationMap;
    }

    public static class AnimationInformation {

        public final Map<AnimationType, AnimationProperties> animationPropertiesMap = new HashMap<AnimationType, AnimationProperties>();
        public final Vector2 offset = new Vector2();
        public final Vector2 size = new Vector2();

        public AnimationInformation(int sizeX, int sizeY, int offsetX, int offsetY) {
            offset.set(offsetX, offsetY);
            size.set(sizeX, sizeY);
        }
    }

    public static class AnimationProperties {
        private String path;
        private Direction[] directions;
        private int cols, rows;
        private float time;

        public AnimationProperties(Direction[] directions, String path, int cols, int rows, float time) {
            this.directions = directions;
            this.path = path;
            this.cols = cols;
            this.rows = rows;
            this.time = time;
        }

        public String getPath() {
            return path;
        }

        public Direction[] getDirections() {
            return directions;
        }

        public int getCols() {
            return cols;
        }

        public int getRows() {
            return rows;
        }

        public float getTime() {
            return time;
        }
    }
}