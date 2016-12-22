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
    public final Vector2 additionalOffset = new Vector2();

    public TextureComponent setSize(float sizeX, float sizeY) {
        if(sprite == null) sprite = new Sprite();
        sprite.setSize(sizeX,sizeY);
        return this;
    }

    public TextureComponent setSize(Vector2 size) {
        if(sprite == null) sprite = new Sprite();
        sprite.setSize(size.x,size.y);
        return this;
    }

    public TextureComponent setAdditionalOffset(float additionalOffsetX, float additionalOffsetY) {
        if(sprite == null) sprite = new Sprite();
        additionalOffset.set(additionalOffsetX,additionalOffsetY);
        return this;
    }

    public TextureComponent setAdditionalOffset(Vector2 additionalOffset) {
        if(sprite == null) sprite = new Sprite();
        this.additionalOffset.set(additionalOffset);
        return this;
    }

    public TextureComponent setSprite(Sprite sprite) {
        this.sprite = sprite;
        return this;
    }

    @Override
    public void reset() {
        additionalOffset.set(0,0);
        sprite = null;
    }
}
