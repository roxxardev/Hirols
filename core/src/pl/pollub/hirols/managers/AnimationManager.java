package pl.pollub.hirols.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by Marcin on 2016-01-15.
 */

public class AnimationManager {
    public static Map<Direction,Animation> createAnimation(Direction[] directions, Texture texture, int FRAME_COLS, int FRAME_ROWS, float frameDuration){
        Map<Direction,Animation> animationDirectioriesMap = new HashMap<Direction, Animation>();
        TextureRegion[][] frames = TextureRegion.split(texture,
                texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        for(int i =0 ;i <FRAME_ROWS; i++)
            animationDirectioriesMap.put(directions[i], new Animation(frameDuration, frames[i]));

        return animationDirectioriesMap;
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
}