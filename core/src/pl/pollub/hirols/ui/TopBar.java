package pl.pollub.hirols.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisTable;

import pl.pollub.hirols.Hirols;


/**
 * Created by krol22 on 24.02.16.
 */
public class TopBar extends VisTable {
    private Hirols game;
    private BitmapFont font;


    public TopBar(Hirols game, Stage stage, boolean debug){
        this.game = game;
        this.setWidth(stage.getWidth());
        this.setHeight(stage.getHeight() / 20);
        this.setPosition(0, stage.getHeight() - this.getHeight());

        this.font = game.assetManager.get("fonts/test2.fnt", BitmapFont.class);
        this.center();
        initAndAddResources();


        this.setBackground(new SpriteDrawable(new Sprite(game.hudManager.whiteTexture){{
            setColor(0,0,0,0.3f);
        }}));
        this.setTouchable(Touchable.enabled);

        this.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }

        });
        this.setDebug(debug);
    }

    private void initAndAddResources() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(this.font, Color.WHITE);

        //float height = this.getHeight() * 2 / 5;
        //float width = height;


        Image resourceMetalImage, resourceEnergyImage, resourceFuelImage, resourceSpecialImage;
        Label resourceMetalLabel, resourceEnergyLabel, resourceFuelLabel, resourceSpecialLabel;

        Texture textureEnergy = game.assetManager.get("resources/energy.png");
        resourceEnergyImage = new Image(textureEnergy);
        //resourceEnergyImage.setWidth(width);
        //resourceEnergyImage.setHeight(height);
        resourceEnergyLabel = new Label("0", labelStyle);

        Texture textureFuel = game.assetManager.get("resources/fuel.png", Texture.class);
        resourceFuelImage = new Image(new TextureRegion(textureFuel));
        //resourceFuelImage.setWidth(width);
        //resourceFuelImage.setHeight(height);
        resourceFuelLabel = new Label("0", labelStyle);

        Texture textureMetal = game.assetManager.get("resources/metal.png", Texture.class);

        resourceMetalImage = new Image(new TextureRegion(textureMetal));
        //resourceMetalImage.setWidth(width);
        //resourceMetalImage.setHeight(height);
        resourceMetalLabel = new Label("0", labelStyle);

        Texture textureSpecial = game.assetManager.get("resources/coinsresource.png", Texture.class);

        resourceSpecialImage = new Image(new TextureRegion(textureSpecial));
        //resourceSpecialImage.setSize((float)(this.getHeight()*0.805),this.getHeight());
        resourceSpecialLabel = new Label("0", labelStyle);

        this.add(resourceFuelImage);
        this.add(resourceFuelLabel).width(this.getWidth()/ 30);
        this.add(resourceMetalImage);
        this.add(resourceMetalLabel).width(this.getWidth() / 30);
        this.add(resourceSpecialImage).width(this.getWidth() / 30);
        this.add(resourceSpecialLabel).width(this.getWidth() / 30);
        this.add(resourceEnergyImage);
        this.add(resourceEnergyLabel).width(this.getWidth() / 30);
        this.setTouchable(Touchable.enabled);
    }

    public void resize(float width,float height){
        this.setWidth(width);
        this.setHeight(height / 20);
        this.setPosition(0, height - this.getHeight());
    }

}
