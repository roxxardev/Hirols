package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eryk on 2015-11-28.
 */
public class TextureComponent implements Component {
    public Sprite sprite;
    public Vector2 additionalOffset;

    public TextureComponent(float sizeX, float sizeY, float additionalOffsetX, float additionalOffsetY){
        this.sprite = new Sprite();
        this.sprite.setSize(sizeX, sizeY);
        this.additionalOffset = new Vector2(additionalOffsetX,additionalOffsetY);
    }

    public TextureComponent(float sizeX, float sizeY){
        this.sprite = new Sprite();
        this.sprite.setSize(sizeX, sizeY);
        this.additionalOffset = new Vector2();
    }
    public TextureComponent(Texture texture) {
        this.sprite = new Sprite(texture);
        this.additionalOffset = new Vector2();
    }
    public TextureComponent(Sprite sprite) {
        this.sprite = sprite;
        this.additionalOffset = new Vector2();
    }
}
