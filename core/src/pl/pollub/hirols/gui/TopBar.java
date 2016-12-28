package pl.pollub.hirols.gui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.HashMap;
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

    private Label playerLabel;
    private Label dayLabel;
    private ArrayList<Image> resourceImages = new ArrayList<>();
    private ArrayList<Label> resourceLabels = new ArrayList<>();
    private Map<ResourceType, Resource> resourceMap = new HashMap<>(4);

    private Class<? extends PlayerComponent> currentPlayer;

    private int heightDivider = 18;

    public TopBar(Hirols game, Stage stage) {
        this.game = game;
        this.setWidth(stage.getWidth());
        this.setHeight(stage.getHeight() / heightDivider);

        this.setTouchable(Touchable.enabled);
        this.setDebug(false);
        this.setBackground(game.hudManager.getTransparentBackground());

        addPlayer();
        addResources();
        addDayLabel();
        updatePlayer();
    }

    public boolean updatePlayer() {
        Class<? extends PlayerComponent> player = game.gameManager.getCurrentPlayerClass();
        if(currentPlayer == player) return false;
        currentPlayer = player;
        PlayerDataComponent playerData = ComponentMapper.getFor(PlayerDataComponent.class).get(game.gameManager.getCurrentPlayer());
        Color color = new Color(playerData.color);
        playerLabel.getStyle().background = new SpriteDrawable(new Sprite(game.hudManager.getWhiteTexture())).tint(color);
        playerLabel.setText(" Player: "+playerData.name + " ");
        int week = (playerData.day / 7) + 1;
        dayLabel.setText("Week " + week + ", Day "+playerData.day+" ");

        updateResources();
        return true;
    }

    private void addPlayer() {
        BitmapFont font = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GOLD);
        playerLabel = new Label("no player", labelStyle);
        add(playerLabel).expand().fill().padRight(30);
        playerLabel.setWrap(true);
        playerLabel.setEllipsis(true);
        playerLabel.setAlignment(Align.center);
    }

    private void addDayLabel() {
        BitmapFont font = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GOLD);
        dayLabel = new Label("Week 0, Day 0", labelStyle);
        dayLabel.setWrap(true);
        dayLabel.setEllipsis(true);
        dayLabel.setAlignment(Align.center);
        add(dayLabel).expand().fill();
    }

    private void addResources() {
        BitmapFont font = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GOLD);

        defaults().expandY().padRight(5);

        Resource wood = new Resource(game.assetManager.get("resources/LogPile.png", Texture.class), ResourceType.WOOD, 0, labelStyle);
        add(wood.image);
        add(wood.label).fill();
        resourceImages.add(wood.image);
        resourceLabels.add(wood.label);

        Resource gold = new Resource(game.assetManager.get("resources/GoldPile.png", Texture.class), ResourceType.GOLD, 0, labelStyle);
        add(gold.image);
        add(gold.label).fill();
        resourceImages.add(gold.image);
        resourceLabels.add(gold.label);

        Resource metal = new Resource(game.assetManager.get("resources/CoalPile.png", Texture.class), ResourceType.METAL, 0, labelStyle);
        add(metal.image);
        add(metal.label).fill();
        resourceImages.add(metal.image);
        resourceLabels.add(metal.label);

        Resource stone = new Resource(game.assetManager.get("resources/StonePile.png", Texture.class), ResourceType.STONE, 0, labelStyle);
        add(stone.image);
        add(stone.label).fill();
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
            label.setAlignment(Align.left);
        }

        public void update(int n) {
            label.setText(n+"");
        }
    }
}
