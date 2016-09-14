package pl.pollub.hirols.gui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.components.player.PlayerDataComponent;

/**
 * Created by erykp_000 on 2016-08-14.
 */
public class TopBar extends Table {
    private Hirols game;

    private Image playerColour;
    private VisLabel playerLabel;
    private ArrayList<Image> resourceImages = new ArrayList<Image>();
    private ArrayList<Label> resourceLabels = new ArrayList<Label>();

    private Class<? extends PlayerComponent> currentPlayer;

    private int heightDivider = 20;

    public TopBar(Hirols game, Stage stage) {
        this.game = game;
        this.setWidth(stage.getWidth());
        this.setHeight(stage.getHeight() / heightDivider);

        this.setTouchable(Touchable.enabled);
        this.setDebug(game.hudManager.debug);
        this.setBackground(game.hudManager.getTransparentBackground());

        addPlayer();
        addResources();
    }

    public boolean updatePlayer() {
        Class<? extends PlayerComponent> player = game.gameManager.getCurrentPlayerClass();
        if(currentPlayer == player) return false;
        currentPlayer = player;
        PlayerDataComponent playerData = ComponentMapper.getFor(PlayerDataComponent.class).get(game.gameManager.getCurrentPlayer());
        Color color = playerData.color;
        playerColour.setColor(color);
        playerLabel.setText(playerData.name);
        return true;
    }

    private void addPlayer() {
        playerColour = new Image(game.hudManager.getWhiteTexture());
        playerColour.setPosition(0,0);
        addActor(playerColour);

        BitmapFont font = game.assetManager.get("fonts/test2.fnt", BitmapFont.class);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        playerLabel = new VisLabel("no player", labelStyle);
        addActor(playerLabel);

        updatePlayer();
    }

    private void addResources() {
        BitmapFont font = game.assetManager.get("fonts/test2.fnt", BitmapFont.class);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Image resourceEnergyImage = new Image(game.assetManager.get("resources/energy.png", Texture.class));
        resourceImages.add(resourceEnergyImage);
        Label resourceEnergyLabel = new Label("0", labelStyle);
        resourceLabels.add(resourceEnergyLabel);
        resourceEnergyImage.setScaling(Scaling.fit);
        this.add(resourceEnergyImage).fill();
        this.add(resourceEnergyLabel);

        Image resourceFuelImage = new Image(game.assetManager.get("resources/fuel.png", Texture.class));
        resourceImages.add(resourceFuelImage);
        Label resourceFuelLabel = new Label("0", labelStyle);
        resourceLabels.add(resourceFuelLabel);
        resourceFuelImage.setScaling(Scaling.fit);
        this.add(resourceFuelImage).fill();
        this.add(resourceFuelLabel);

        Image resourceSpecialImage = new Image(game.assetManager.get("resources/coinsresource.png", Texture.class));
        resourceImages.add(resourceSpecialImage);
        resourceSpecialImage.setWidth(0);
        resourceSpecialImage.setHeight(0);
        Label resourceSpecialLabel = new Label("0", labelStyle);
        resourceLabels.add(resourceSpecialLabel);
        resourceSpecialImage.setScaling(Scaling.fit);
        this.add(resourceSpecialImage).fill();
        this.add(resourceSpecialLabel);

        Image resourceMetalImage = new Image(game.assetManager.get("resources/metal.png", Texture.class));
        resourceImages.add(resourceMetalImage);
        Label resourceMetalLabel = new Label("0", labelStyle);
        resourceLabels.add(resourceMetalLabel);
        resourceMetalImage.setScaling(Scaling.fit);
        this.add(resourceMetalImage).fill();
        this.add(resourceMetalLabel);

    }

    public void resize(float width, float height) {
        this.setWidth(width);
        float tempHeight = height / heightDivider;
        this.setHeight(tempHeight < 25 ? 25 : tempHeight);
        this.setPosition(0, height - this.getHeight());

        float resourceImageWidth = width/heightDivider;
        for(Actor actor : resourceImages) {
            getCell(actor).width(resourceImageWidth);
        }

        float resourceLabelWidth = width / heightDivider;
        for(Actor actor : resourceLabels) {
            getCell(actor).width(resourceLabelWidth);
        }

        playerLabel.setBounds(4, 0, 0,getHeight());
        playerColour.setBounds(0,0, playerLabel.getPrefWidth() + 10, getHeight());
    }

    public void setHeightDivider(int heightDivider) {
        this.heightDivider = heightDivider;
        resize(getStage().getWidth(), getStage().getHeight());
    }
}
