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
import com.badlogic.gdx.utils.Scaling;
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
    private BitmapFont bitmapFont;
    public boolean debug;

    public Button.ButtonStyle buttonStyleRoundedOver;
    public Button.ButtonStyle buttonStyleRoundedChecked;

    public HudManager(Hirols game){
        this.game = game;
        skin = game.assetManager.get("ui/default_skin/uiskin.json", Skin.class);

        bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);

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
        if(!VisUI.isLoaded())
        VisUI.load();
    }

    private void createStyles(){
        Sprite sprite = new Sprite(whiteTexture);
        sprite.setColor(0f, 0f, 0f, 0.7f);
        SpriteDrawable drawableUp = new SpriteDrawable(sprite);

        sprite = new Sprite(whiteTexture);
        sprite.setColor(1f,1f,1f, 0.7f);
        SpriteDrawable drawableDown=  new SpriteDrawable(sprite);

        sprite = new Sprite(whiteTexture);
        sprite.setColor(0f, 0f, 0f, 1f);
        SpriteDrawable drawableOver = new SpriteDrawable(sprite);

        sprite = new Sprite(whiteTexture);
        sprite.setColor(1f,0f,0f,0.1f);
        SpriteDrawable drawableChecked = new SpriteDrawable(sprite);


        Label.LabelStyle labelStyleWhite = new Label.LabelStyle(bitmapFont, Color.WHITE);
        skin.add("label-white", labelStyleWhite, Label.LabelStyle.class);

        VisImageButton.VisImageButtonStyle imageButtonStyle = new VisImageButton.VisImageButtonStyle();
        imageButtonStyle.up = drawableUp;
        imageButtonStyle.down = drawableDown;
        imageButtonStyle.over = drawableOver;
        skin.add("image-button", imageButtonStyle, VisImageButton.VisImageButtonStyle.class);

        VisTextButton.VisTextButtonStyle textButtonStyle = new VisTextButton.VisTextButtonStyle();
        textButtonStyle.up = drawableUp;
        textButtonStyle.down = drawableDown;
        textButtonStyle.over = drawableOver;
        textButtonStyle.font = bitmapFont;
        skin.add("text-button", textButtonStyle, VisTextButton.VisTextButtonStyle.class);

        VisImageTextButton.VisImageTextButtonStyle imageTextButtonStyle = new VisImageTextButton.VisImageTextButtonStyle();
        imageTextButtonStyle.up = drawableUp;
        imageTextButtonStyle.down = drawableDown;
        imageTextButtonStyle.over = drawableOver;
        imageTextButtonStyle.font = bitmapFont;
        imageTextButtonStyle.fontColor = Color.GOLD;
        skin.add("image-text-button", imageTextButtonStyle, VisImageTextButton.VisImageTextButtonStyle.class);


        VisImageTextButton.VisImageTextButtonStyle unitsStyle = new VisImageTextButton.VisImageTextButtonStyle();
        unitsStyle.up = new TextureRegionDrawable(new TextureRegion(whiteTexture)).tint(new Color(0,0,0,0.6f));
        unitsStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112));
        unitsStyle.imageCheckedOver = new TextureRegionDrawable((TextureRegionDrawable)unitsStyle.imageChecked).tint(new Color(0,0,0,0.99f));
        unitsStyle.font = game.assetManager.get("testFontSize12.ttf", BitmapFont.class);
        unitsStyle.imageOver = new SpriteDrawable(new Sprite(whiteTexture)).tint(new Color(0,0,0,0.8f));
        skin.add("units-style", unitsStyle, VisImageTextButton.VisImageTextButtonStyle.class);

        VisTextButton.VisTextButtonStyle menuStyle = new VisTextButton.VisTextButtonStyle();
        menuStyle.up = drawableUp.tint(new Color(1,1,1,0.1f));
        menuStyle.down = drawableUp.tint(new Color(1,1,1,0.5f));
        menuStyle.over = drawableUp.tint(new Color(1,1,1,0.2f));
        menuStyle.font = game.assetManager.get("testFontSize32.ttf", BitmapFont.class);
        menuStyle.fontColor = Color.GOLD;
        skin.add("mainMenuStyle", menuStyle, VisTextButton.VisTextButtonStyle.class);

    }

    public SpriteDrawable getTransparentBackground() {
        return transparentBackground;
    }

    public Texture getWhiteTexture() {
        return whiteTexture;
    }

    public static void moveTextLabelBelowImage(VisImageTextButton button, Scaling scaling) {
        button.getImage().setScaling(scaling);
        button.clearChildren();
        button.add(button.getImage()).expand().fill().row();
        button.add(button.getLabel()).fill();
    }
}
