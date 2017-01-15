package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by erykp_000 on 2016-12-24.
 */

public class BannerComponent implements Component, Pool.Poolable {
    public Sprite bannerSprite;
    public Color color = new Color();
    public Vector2 offset = new Vector2();

    public BannerComponent init(Sprite bannerSprite, Color color, int offsetX, int offsetY) {
        this.bannerSprite = bannerSprite;
        this.color.set(color);
        this.offset.set(offsetX, offsetY);
        return this;
    }

    @Override
    public void reset() {
        bannerSprite = null;
        color.set(Color.CLEAR);
        offset.set(0,0);
    }
}
