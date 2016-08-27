package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2015-11-28.
 */
public class TextureComponent implements Component, Pool.Poolable {
    public Sprite sprite;
    public Vector2 additionalOffset = new Vector2();

    public TextureComponent(float sizeX, float sizeY, float additionalOffsetX, float additionalOffsetY){
        this.sprite = new Sprite();
        this.sprite.setSize(sizeX, sizeY);
        this.additionalOffset.set(additionalOffsetX,additionalOffsetY);
    }

    public TextureComponent(float sizeX, float sizeY){
        this.sprite = new Sprite();
        this.sprite.setSize(sizeX, sizeY);
    }
    public TextureComponent(Texture texture) {
        this.sprite = new Sprite(texture);
    }
    public TextureComponent(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void reset() {
        sprite = null;
    }
}
