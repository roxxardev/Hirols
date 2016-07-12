package pl.pollub.hirols.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import pl.pollub.hirols.Hirols;

/**
 * Created by Marcin on 2016-04-22.
 */
public class HudManager {
    public Skin skin;
    public BitmapFont font;
    public boolean debug;

    public HudManager(Hirols game){
        skin = new Skin();

        font = game.assetManager.get("fonts/test2.fnt", BitmapFont.class);
        font.getData().setScale(1);

        Pixmap pixmapWhite = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapWhite.setColor(Color.rgba8888(1f, 1f, 1f, 1f));
        pixmapWhite.fill();

        Texture whiteTexture = new Texture(pixmapWhite);
        VisTextButton.VisTextButtonStyle buttonStyle = new VisTextButton.VisTextButtonStyle();

        Sprite sprite = new Sprite(whiteTexture);
        sprite.setColor(0f, 0f, 0f, 0.7f);
        buttonStyle.up = new SpriteDrawable(sprite);

        sprite = new Sprite(whiteTexture);
        sprite.setColor(1f,1f,1f, 0.7f);
        buttonStyle.down =  new SpriteDrawable(sprite);

        sprite = new Sprite(whiteTexture);
        sprite.setColor(0f, 0f, 0f, 1f);
        buttonStyle.over = new SpriteDrawable(sprite);
        buttonStyle.font = font;

        skin.add("button-custom", buttonStyle);

        Label.LabelStyle labelStyleWhite = new Label.LabelStyle(font, Color.WHITE);

        skin.add("label-white", labelStyleWhite);

        debug = false;

        pixmapWhite.dispose();
    }
}
