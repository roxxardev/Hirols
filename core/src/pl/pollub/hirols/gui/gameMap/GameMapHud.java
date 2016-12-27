package pl.pollub.hirols.gui.gameMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.gui.LongPressWindow;
import pl.pollub.hirols.gui.TopBar;
import pl.pollub.hirols.gui.UnitsGrid;
import pl.pollub.hirols.managers.AnimationManager;
import pl.pollub.hirols.gui.Hud;
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

    private GameMapScreen gameMapScreen;

    public GameMapHud(Hirols game, GameMapScreen gameMapScreen) {
        super(game, new ScreenViewport());
        this.gameMapScreen = gameMapScreen;
        createActors();
    }

    private void createActors(){
        topBar = new TopBar(game,stage);
        rightBar = new RightBar(game, this,gameMapScreen.getGameMapComponentClass());
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
            Label playerName = new Label(heroDataComponent.hero.name , labelStyle);
            Label playerLevel = new Label("Level " + heroDataComponent.heroLevel.getLevel() , labelStyle);
            Label playerExperience = new Label("Experience " + heroDataComponent.heroLevel.getExperience() , labelStyle);
            heroInfoTable.add(playerName).expand().fill().row();
            heroInfoTable.add(playerLevel).expand().fill().row();
            heroInfoTable.add(playerExperience).expand().fill();
            heroTable.add(heroInfoTable).row();
            Table heroSkillsTable = new Table();
            heroSkillsTable.add().expand();
            heroSkillsTable.add().expand();

            heroSkillsTable.add().expand();
            heroSkillsTable.add().expand();
            heroSkillsTable.add().expand();
            heroTable.add(heroSkillsTable).expand().fill();
            longPressWindow.add(heroTable).expand().fill().row();

            ButtonGroup<VisImageTextButton> buttonGroup = new ButtonGroup<VisImageTextButton>();

            buttonGroup.setMaxCheckCount(1);
            buttonGroup.setMinCheckCount(0);
            buttonGroup.setUncheckLast(false);

            UnitsGrid unitsGrid = new UnitsGrid(game, buttonGroup);
            unitsGrid.setItemSize(windowWidth/5 - 5);
            unitsGrid.update(heroDataComponent);
            longPressWindow.add(unitsGrid).colspan(2).expand().fill();
        } else return false;


        stage.addActor(longPressWindow);
        return true;
    }

    public TopBar getTopBar() {
        return topBar;
    }

    public RightBar getRightBar() {
        return rightBar;
    }
}
