package pl.pollub.hirols.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisTextButton;

import pl.pollub.hirols.Hirols;

/**
 * Created by Marcin on 2016-04-22.
 */
public class HudManager {
    private Hirols game;
    public Skin skin;

    private Texture whiteTexture;
    private SpriteDrawable transparentBackground;
    public BitmapFont bitmapFont;
    public boolean debug;

    public Button.ButtonStyle buttonStyleRoundedOver;
    public Button.ButtonStyle buttonStyleRoundedChecked;

    public VisImageButton.VisImageButtonStyle imageButtonStyle;
    public VisTextButton.VisTextButtonStyle textButtonStyle;
    public VisImageTextButton.VisImageTextButtonStyle imageTextButtonStyle;


    public HudManager(Hirols game){
        this.game = game;
        skin = new Skin();

        bitmapFont = game.assetManager.get("fonts/test2.fnt", BitmapFont.class);
        bitmapFont.getData().setScale(1);

        int width = 1, height= 1, radius = 10;
        Pixmap pixmapWhite = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        pixmapWhite.setColor(1f,1f,1f,1f);
        pixmapWhite.fill();

        whiteTexture = new Texture(pixmapWhite);

        createStyles();

        debug = false;

        pixmapWhite.dispose();

        transparentBackground = new SpriteDrawable(new Sprite(whiteTexture) {{
            setColor(0,0,0,0.3f);
        }});

        VisUI.load();
    }

    private void createStyles(){
        final Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Sprite sprite = new Sprite(whiteTexture);
        sprite.setColor(0f, 0f, 0f, 0.7f);
        buttonStyle.up = new SpriteDrawable(sprite);

        sprite = new Sprite(whiteTexture);
        sprite.setColor(1f,1f,1f, 0.7f);
        buttonStyle.down =  new SpriteDrawable(sprite);

        sprite = new Sprite(whiteTexture);
        sprite.setColor(0f, 0f, 0f, 1f);
        buttonStyle.over = new SpriteDrawable(sprite);
        //buttonStyle.font = font;

        sprite = new Sprite(whiteTexture);
        sprite.setColor(1f,0f,0f,0.1f);
        buttonStyle.checked = new SpriteDrawable(sprite);

        final SpriteDrawable spriteDrawable = new SpriteDrawable(new Sprite(game.assetManager.get("resources/fuel.png", Texture.class)));

        skin.add("button-custom", buttonStyle, Button.ButtonStyle.class);

        Label.LabelStyle labelStyleWhite = new Label.LabelStyle(bitmapFont, Color.WHITE);

        skin.add("label-white", labelStyleWhite, Label.LabelStyle.class);

        imageButtonStyle = new VisImageButton.VisImageButtonStyle(){{
            up = buttonStyle.up;
            down = buttonStyle.down;
            over = buttonStyle.over;
            //checked = spriteDrawable;
            imageChecked = spriteDrawable;
        }};

        skin.add("image-button", imageButtonStyle, VisImageButton.VisImageButtonStyle.class);

        textButtonStyle = new VisTextButton.VisTextButtonStyle(){{
            up = buttonStyle.up;
            down = buttonStyle.down;
            over = buttonStyle.over;
            font = bitmapFont;
        }};
        skin.add("text-button", textButtonStyle, VisTextButton.VisTextButtonStyle.class);

        imageTextButtonStyle = new VisImageTextButton.VisImageTextButtonStyle(){{
            up = buttonStyle.up;
            down = buttonStyle.down;
            over = buttonStyle.over;
            font = bitmapFont;
        }};
        skin.add("image-text-button", imageTextButtonStyle, VisImageTextButton.VisImageTextButtonStyle.class);


        VisImageTextButton.VisImageTextButtonStyle unitsStyle = new VisImageTextButton.VisImageTextButtonStyle();
        unitsStyle.up = new TextureRegionDrawable(new TextureRegion(whiteTexture)).tint(new Color(0,0,0,0.6f));
        unitsStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112));
        unitsStyle.font = game.assetManager.get("testFontSize12.ttf", BitmapFont.class);
        unitsStyle.over = new SpriteDrawable(new Sprite(whiteTexture)).tint(new Color(0,0,0,0.8f));

        skin.add("units-style", unitsStyle, VisImageTextButton.VisImageTextButtonStyle.class);

    }

    public SpriteDrawable getTransparentBackground() {
        return transparentBackground;
    }

    public Texture getWhiteTexture() {
        return whiteTexture;
    }
}
