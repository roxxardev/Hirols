package pl.pollub.hirols.gui.gameMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.ChestComponent;
import pl.pollub.hirols.components.map.EnemyDataComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.MineDataComponent;
import pl.pollub.hirols.components.map.PortalComponent;
import pl.pollub.hirols.components.map.RecruitDataComponent;
import pl.pollub.hirols.components.map.ResourceComponent;
import pl.pollub.hirols.gui.AnimatedImage;
import pl.pollub.hirols.gui.LongPressWindow;
import pl.pollub.hirols.gui.TopBar;
import pl.pollub.hirols.gui.UnitsGrid;
import pl.pollub.hirols.managers.AnimationManager;
import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;
import pl.pollub.hirols.managers.enums.ResourceType;
import pl.pollub.hirols.screens.GameMapScreen;

/**
 * Created by erykp_000 on 2016-07-29.
 */
public class GameMapHud extends Hud {

    private TopBar topBar;
    private RightBar rightBar;
    private LeftBar leftBar;

    private pl.pollub.hirols.gui.AnimatedImage longPressImage;
    private LongPressWindow longPressWindow;

    GameMapScreen gameMapScreen;

    public GameMapHud(Hirols game, GameMapScreen gameMapScreen) {
        super(game, new ScreenViewport());
        this.gameMapScreen = gameMapScreen;
        createActors();
    }

    private void createActors(){
        topBar = new TopBar(game,stage);
        rightBar = new RightBar(game, this);
        leftBar = new LeftBar(game,stage,gameMapScreen);

        longPressImage = new pl.pollub.hirols.gui.AnimatedImage(AnimationManager.createAnimation(game.assetManager.get("animations/loadingLongPress.png", Texture.class), 32, 1, 0.037f), false);

        stage.addActor(longPressImage);
        stage.addActor(topBar);
        stage.addActor(rightBar);
        stage.addActor(leftBar);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        leftBar.update();
        rightBar.update();
    }

    public void hideLeftBar(){
        leftBar.hide();
    }

    public void unfocusScroll(){
        stage.setScrollFocus(null);
    }

    public void updateLongPressImage(boolean visible, float x, float y) {
        longPressImage.setVisible(visible);
        if(visible) {
            longPressImage.setPosition(x,y);
        }
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
        topBar.resize(width,height);
        leftBar.resize(width,height, topBar.getHeight());
        rightBar.resize(width,height, topBar.getHeight());
        longPressImage.setWidth(width<height ? width/13 : height/13);
        longPressImage.setHeight(longPressImage.getWidth());
    }

    @Override
    public void dispose() {
        super.dispose();

    }

    public pl.pollub.hirols.gui.AnimatedImage getLongPressImage() {
        return longPressImage;
    }

    public boolean removeLongPressWindow() {
        return (longPressWindow != null && longPressWindow.remove());
    }

    public boolean addLongPressWindow(Component component) {
        if(stage.getActors().contains(longPressWindow,true)) return false;

        if(component instanceof HeroDataComponent) {
            HeroDataComponent heroDataComponent = (HeroDataComponent)component;
            longPressWindow = new LongPressWindow("Hero information", false);
            longPressWindow.addListener(new ClickListener());
            int windowWidth = 500;
            int windowHeight = 360;
            longPressWindow.setSize(windowWidth,windowHeight);

            Image image = new Image(game.assetManager.get(heroDataComponent.hero.avatarPath, Texture.class));
            image.setScaling(Scaling.fit);
            longPressWindow.add(image).expand().fill();
            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);

            Table heroTable = new Table();
            Table heroInfoTable = new Table();
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.GOLD);
            Label heroName = new Label(heroDataComponent.hero.name , labelStyle);
            Label heroLevel = new Label("Level " + heroDataComponent.heroLevel.getLevel() , labelStyle);
            Label heroExperience = new Label("Experience " + heroDataComponent.heroLevel.getExperience() , labelStyle);
            heroInfoTable.add(heroName).expand().fill().row();
            heroInfoTable.add(heroLevel).expand().fill().row();
            heroInfoTable.add(heroExperience).expand().fill();
            heroTable.add(heroInfoTable).row();
            Table heroSkillsTable = new Table();
            heroSkillsTable.add().expand();
            heroSkillsTable.add().expand();

            heroSkillsTable.add().expand();
            heroSkillsTable.add().expand();
            heroSkillsTable.add().expand();
            heroTable.add(heroSkillsTable).expand().fill();
            longPressWindow.add(heroTable).expand().fill().row();

            ButtonGroup<VisImageTextButton> buttonGroup = new ButtonGroup<>();

            buttonGroup.setMaxCheckCount(1);
            buttonGroup.setMinCheckCount(0);
            buttonGroup.setUncheckLast(false);

            UnitsGrid unitsGrid = new UnitsGrid(game, buttonGroup);
            unitsGrid.setItemSize(windowWidth/5 - 5);
            unitsGrid.update(heroDataComponent);
            longPressWindow.add(unitsGrid).colspan(2).expand().fill();
        } else if(component instanceof EnemyDataComponent) {
            EnemyDataComponent enemyDataComponent = (EnemyDataComponent)component;

            longPressWindow = new LongPressWindow("Enemy information", false);
            longPressWindow.addListener(new ClickListener());
            int windowWidth = 300;
            int windowHeight = 300;
            longPressWindow.setSize(windowWidth,windowHeight);

            AnimationManager.AnimationInformation animationInformation = enemyDataComponent.unit.animationInformation;
            AnimationManager.AnimationProperties animationProperties = animationInformation.animationPropertiesMap.get(AnimationType.RUN);
            Animation animation = AnimationManager.createAnimation(animationProperties.getDirections(),game.assetManager.get(animationProperties.getPath(), Texture.class), animationProperties.getCols(), animationProperties.getRows(), animationProperties.getTime())
                    .get(Direction.SE);
            AnimatedImage unitAnimatedImage = new AnimatedImage(animation, true);
            longPressWindow.add(unitAnimatedImage).expand().fill().row();

            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.GOLD);
            Label unitName = new Label(enemyDataComponent.unit.name , labelStyle);
            longPressWindow.add(unitName).pad(3).row();
            int quantity = enemyDataComponent.quantity;
            String numberString;
            if(quantity < 5) {
                numberString = "Kilka";
            } else if(quantity < 12) {
                numberString = "Malo";
            } else if(quantity < 23) {
                numberString = "Grupa";
            } else if(quantity < 45) {
                numberString = "Wiele";
            } else {
                numberString = "Bardzo duzo";
            }
            Label number = new Label(numberString, labelStyle);
            longPressWindow.add(number).pad(3);

        } else if(component instanceof MineDataComponent) {
            MineDataComponent mineDataComponent = (MineDataComponent)component;

            longPressWindow = new LongPressWindow("Mine information", false);
            longPressWindow.addListener(new ClickListener());
            int windowWidth = 300;
            int windowHeight = 300;
            longPressWindow.setSize(windowWidth,windowHeight);

            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.GOLD);
            ResourceType resourceType = mineDataComponent.type;
            String mineName;
            String resourceTexturePath = "resources/GoldPile.png";
            switch (resourceType) {
                case GOLD:
                    mineName = "Kopalnia zlota";
                    resourceTexturePath = "resources/GoldPile.png";
                    break;
                case WOOD:
                    mineName = "Tartak";
                    resourceTexturePath = "resources/LogPile.png";
                    break;
                case METAL:
                    mineName = "Kopalnia metalu";
                    resourceTexturePath = "resources/CoalPile.png";
                    break;
                case STONE:
                    mineName = "Kamieniolom";
                    resourceTexturePath = "resources/StonePile.png";
                    break;
                default:
                    mineName = "no name";
                    break;
            }
            Label mineLabel = new Label(mineName , labelStyle);
            Label resourceTypeLabel = new Label("Resource type ", labelStyle);
            Image resourceImage = new Image(game.assetManager.get(resourceTexturePath, Texture.class));
            resourceImage.setScaling(Scaling.fit);
            Label info = new Label(mineDataComponent.amountPerWeek + " per week", labelStyle);
            longPressWindow.add(mineLabel).pad(5).row();
            longPressWindow.add(resourceTypeLabel).pad(5);
            longPressWindow.add(resourceImage).row();
            longPressWindow.add(info).pad(5);

        } else if(component instanceof ResourceComponent) {
            ResourceComponent resourceComponent = (ResourceComponent)component;
            longPressWindow = new LongPressWindow("Resource information", false);
            longPressWindow.addListener(new ClickListener());
            int windowWidth = 200;
            int windowHeight = 200;
            longPressWindow.setSize(windowWidth,windowHeight);
            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.GOLD);
            Label resourceName = new Label(resourceComponent.resourceType.name(), labelStyle);
            longPressWindow.add(resourceName);

        } else if(component instanceof ChestComponent) {
            longPressWindow = new LongPressWindow("Chest information", false);
            longPressWindow.addListener(new ClickListener());
            int windowWidth = 200;
            int windowHeight = 200;
            longPressWindow.setSize(windowWidth,windowHeight);
            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.GOLD);
            Label label = new Label("Chest", labelStyle);
            longPressWindow.add(label);

        } else if(component instanceof RecruitDataComponent) {
            RecruitDataComponent recruitDataComponent = (RecruitDataComponent)component;
            longPressWindow = new LongPressWindow("Recruit building information", false);
            longPressWindow.addListener(new ClickListener());
            int windowWidth = 450;
            int windowHeight = 200;
            longPressWindow.setSize(windowWidth,windowHeight);

            AnimationManager.AnimationInformation animationInformation = recruitDataComponent.unit.animationInformation;
            AnimationManager.AnimationProperties animationProperties = animationInformation.animationPropertiesMap.get(AnimationType.RUN);
            Animation animation = AnimationManager.createAnimation(animationProperties.getDirections(),game.assetManager.get(animationProperties.getPath(), Texture.class), animationProperties.getCols(), animationProperties.getRows(), animationProperties.getTime())
                    .get(Direction.SE);
            AnimatedImage unitAnimatedImage = new AnimatedImage(animation, true);
            longPressWindow.add(unitAnimatedImage).expandY().fill();

            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.GOLD);
            Label unitName = new Label(recruitDataComponent.unit.name + "\n" + recruitDataComponent.amountPerWeek +" per week\nYou can recruit units here", labelStyle);
            unitName.setWrap(true);
            longPressWindow.add(unitName).expand().fill().pad(3);

        } else if(component instanceof PortalComponent) {
            longPressWindow = new LongPressWindow("Portal information", false);
            longPressWindow.addListener(new ClickListener());
            int windowWidth = 400;
            int windowHeight = 200;
            longPressWindow.setSize(windowWidth,windowHeight);
            BitmapFont bitmapFont = game.assetManager.get("testFontSize18.ttf", BitmapFont.class);
            Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, Color.GOLD);
            Label label = new Label("Portal",labelStyle);
            Label info = new Label("Moves your hero to other location where same portal is", labelStyle);
            info.setWrap(true);
            longPressWindow.add(label).pad(5).row();
            longPressWindow.add(info).expand().fill();

        } else return false;

        longPressWindow.setPosition(200,200);
        stage.addActor(longPressWindow);
        return true;
    }

    public void newTurn() {
        topBar.updatePlayer();
        rightBar.updatePlayer();
    }

    public TopBar getTopBar() {
        return topBar;
    }

    public RightBar getRightBar() {
        return rightBar;
    }
}
