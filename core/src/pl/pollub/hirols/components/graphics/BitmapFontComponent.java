package pl.pollub.hirols.components.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Eryk on 2015-12-02.
 */
public class BitmapFontComponent implements Component {
    public BitmapFont bitmapFont;
    public CharSequence sequence;
    public float scale;
    public Color color;

    public BitmapFontComponent(BitmapFont bitmapFont, CharSequence sequence, Color color, float scale) {
        this.bitmapFont = bitmapFont;
        this.sequence = sequence;
        this.scale=scale;
        this.color=color;
    }

    public BitmapFontComponent(BitmapFont bitmapFont, CharSequence sequence) {
        this.bitmapFont = bitmapFont;
        this.sequence = sequence;
        color = bitmapFont.getColor();
        scale = 1;
    }
}
