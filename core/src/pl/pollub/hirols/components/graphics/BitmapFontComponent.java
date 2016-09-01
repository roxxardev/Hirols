package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eryk on 2015-12-02.
 */
public class BitmapFontComponent implements Component, Pool.Poolable {
    public BitmapFont bitmapFont;
    public CharSequence sequence;
    public float scale;
    public Color color;

    public BitmapFontComponent init(BitmapFont bitmapFont, CharSequence sequence) {
        this.bitmapFont = bitmapFont;
        this.sequence = sequence;
        color = bitmapFont.getColor();
        scale = 1;
        return this;
    }

    @Override
    public void reset() {
        bitmapFont = null;
        sequence = null;
        scale = 0f;
        color.set(Color.CLEAR);
    }
}
