package pl.pollub.hirols.gui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.components.player.PlayerDataComponent;
import pl.pollub.hirols.managers.enums.ResourceType;

/**
 * Created by erykp_000 on 2016-08-14.
 */
public class TopBar extends Table {
    private Hirols game;

    private Image playerColour;
    private VisLabel playerLabel;
    private ArrayList<Image> resourceImages = new ArrayList<Image>();
    private ArrayList<Label> resourceLabels = new ArrayList<Label>();
    private Map<ResourceType, Resource> resourceMap = new HashMap<ResourceType, Resource>(4);

    private Class<? extends PlayerComponent> currentPlayer;

    private int heightDivider = 18;

    public TopBar(Hirols game, Stage stage) {
        this.game = game;
        this.setWidth(stage.getWidth());
        this.setHeight(stage.getHeight() / heightDivider);

        this.setTouchable(Touchable.enabled);
        this.setDebug(game.hudManager.debug);
        this.setBackground(game.hudManager.getTransparentBackground());

        addResources();
        addPlayer();
    }

    public boolean updatePlayer() {
        Class<? extends PlayerComponent> player = game.gameManager.getCurrentPlayerClass();
        if(currentPlayer == player) return false;
        currentPlayer = player;
        PlayerDataComponent playerData = ComponentMapper.getFor(PlayerDataComponent.class).get(game.gameManager.getCurrentPlayer());
        Color color = playerData.color;
        playerColour.setColor(color);
        playerLabel.setText(playerData.name);

        updateResources();
        return true;
    }

    private void addPlayer() {
        playerColour = new Image(game.hudManager.getWhiteTexture());
        playerColour.setPosition(0,0);
        addActor(playerColour);

        BitmapFont font = game.assetManager.get("testFontSize12.ttf", BitmapFont.class);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GOLD);
        playerLabel = new VisLabel("no player", labelStyle);
        addActor(playerLabel);

        updatePlayer();
    }

    private void addResources() {
        BitmapFont font = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GOLD);

//        Image woodImage = new Image(game.assetManager.get("resources/LogPile.png", Texture.class));
//        resourceImages.add(woodImage);
//        Label woodLabel = new Label("0", labelStyle);
//        resourceLabels.add(woodLabel);
//        woodImage.setScaling(Scaling.fit);
//        this.add(woodImage).fill();
//        this.add(woodLabel);
//
//        Image stoneImage = new Image(game.assetManager.get("resources/StonePile.png", Texture.class));
//        resourceImages.add(stoneImage);
//        Label stoneLabel = new Label("0", labelStyle);
//        resourceLabels.add(stoneLabel);
//        stoneImage.setScaling(Scaling.fit);
//        this.add(stoneImage).fill();
//        this.add(stoneLabel);
//
//        Image goldImage = new Image(game.assetManager.get("resources/GoldPile.png", Texture.class));
//        resourceImages.add(goldImage);
//        goldImage.setWidth(0);
//        goldImage.setHeight(0);
//        Label goldLabel = new Label("0", labelStyle);
//        resourceLabels.add(goldLabel);
//        goldImage.setScaling(Scaling.fit);
//        this.add(goldImage).fill();
//        this.add(goldLabel);
//
//        Image metalImage = new Image(game.assetManager.get("resources/CoalPile.png", Texture.class));
//        resourceImages.add(metalImage);
//        Label metalLabel = new Label("0", labelStyle);
//        resourceLabels.add(metalLabel);
//        metalImage.setScaling(Scaling.fit);
//        this.add(metalImage).fill();
//        this.add(metalLabel);

        Resource wood = new Resource(game.assetManager.get("resources/LogPile.png", Texture.class), ResourceType.WOOD, 0, labelStyle);
        add(wood.image).fill();
        add(wood.label);
        resourceImages.add(wood.image);
        resourceLabels.add(wood.label);

        Resource gold = new Resource(game.assetManager.get("resources/GoldPile.png", Texture.class), ResourceType.GOLD, 0, labelStyle);
        add(gold.image).fill();
        add(gold.label);
        resourceImages.add(gold.image);
        resourceLabels.add(gold.label);

        Resource metal = new Resource(game.assetManager.get("resources/CoalPile.png", Texture.class), ResourceType.METAL, 0, labelStyle);
        add(metal.image).fill();
        add(metal.label);
        resourceImages.add(metal.image);
        resourceLabels.add(metal.label);

        Resource stone = new Resource(game.assetManager.get("resources/StonePile.png", Texture.class), ResourceType.STONE, 0, labelStyle);
        add(stone.image).fill();
        add(stone.label);
        resourceImages.add(stone.image);
        resourceLabels.add(stone.label);

        resourceMap.put(wood.resourceType, wood);
        resourceMap.put(gold.resourceType, gold);
        resourceMap.put(metal.resourceType, metal);
        resourceMap.put(stone.resourceType, stone);
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

    public void updateResources() {
        PlayerDataComponent playerData = ComponentMapper.getFor(PlayerDataComponent.class).get(game.gameManager.getCurrentPlayer());

        for(Map.Entry<ResourceType, Resource> entry : resourceMap.entrySet()) {
            ResourceType resourceType = entry.getKey();
            Resource resource = entry.getValue();

            resource.update(playerData.resources.get(resourceType));
        }
    }

    private class Resource {
        final Image image;
        final Label label;
        final ResourceType resourceType;

        Resource(Texture texture, ResourceType resourceType, int n, Label.LabelStyle labelStyle) {
            this.resourceType = resourceType;
            image = new Image(texture);
            image.setScaling(Scaling.fit);
            label = new Label(n+"", labelStyle);
        }

        public void update(int n) {
            label.setText(n+"");
            Gdx.app.log("dupa", resourceType.toString() + " " + n);
        }
    }
}
