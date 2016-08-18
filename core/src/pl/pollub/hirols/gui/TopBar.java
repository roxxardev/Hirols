package pl.pollub.hirols.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

import pl.pollub.hirols.Hirols;

/**
 * Created by erykp_000 on 2016-08-14.
 */
public class TopBar extends Table {
    private Hirols game;

    private int heightDivider = 20;

    public TopBar(Hirols game, Stage stage) {
        this.game = game;
        this.setWidth(stage.getWidth());
        this.setHeight(stage.getHeight() / heightDivider);

        this.center();
        this.setTouchable(Touchable.enabled);
        this.setDebug(game.hudManager.debug);
        this.setBackground(game.hudManager.getTransparentBackground());

        addResources();

        resize(stage.getWidth(), stage.getHeight());

        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("HUD", "Clicked on topBar.");
            }
        });
    }

    private void addResources() {
        BitmapFont font = game.assetManager.get("fonts/test2.fnt", BitmapFont.class);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Image resourceEnergyImage = new Image(game.assetManager.get("resources/energy.png", Texture.class));
        Label resourceEnergyLabel = new Label("0", labelStyle);
        resourceEnergyImage.setScaling(Scaling.fit);
        this.add(resourceEnergyImage).fill();
        this.add(resourceEnergyLabel);

        Image resourceFuelImage = new Image(game.assetManager.get("resources/fuel.png", Texture.class));
        Label resourceFuelLabel = new Label("0", labelStyle);
        resourceFuelImage.setScaling(Scaling.fit);
        this.add(resourceFuelImage).fill();
        this.add(resourceFuelLabel);

        Image resourceSpecialImage = new Image(game.assetManager.get("resources/coinsresource.png", Texture.class));
        Label resourceSpecialLabel = new Label("0", labelStyle);
        resourceSpecialImage.setScaling(Scaling.fit);
        this.add(resourceSpecialImage).fill();
        this.add(resourceSpecialLabel);

        Image resourceMetalImage = new Image(game.assetManager.get("resources/metal.png", Texture.class));
        Label resourceMetalLabel = new Label("0", labelStyle);
        resourceMetalImage.setScaling(Scaling.fit);
        this.add(resourceMetalImage).fill();
        this.add(resourceMetalLabel);

    }

    public void resize(float width, float height) {
        this.setWidth(width);
        float tempHeight = height / heightDivider;
        this.setHeight(tempHeight < 25 ? 25 : tempHeight);
        this.setPosition(0, height - this.getHeight());

        for(Cell cell : getCells()) {
            cell.width(width/20);
        }
    }

    public void setHeightDivider(int heightDivider) {
        this.heightDivider = heightDivider;
        resize(getStage().getWidth(), getStage().getHeight());
    }
}
