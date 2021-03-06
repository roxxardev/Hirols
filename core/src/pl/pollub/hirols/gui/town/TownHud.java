package pl.pollub.hirols.gui.town;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.TownDataComponent;
import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.gui.TopBar;
import pl.pollub.hirols.gui.UnitsGrid;
import pl.pollub.hirols.managers.HudManager;

/**
 * Created by erykp_000 on 2016-10-30.
 */

public class TownHud extends Hud {
    private TopBar topBar;

    private Garrison inTown, outTown;

    private VisImageTextButton exitButton;
    private ImageButton barrackButton, townHallButton, libraryButton, marketButton, wallsButton, tavernButton;
    private TownBuilding townBuildingWindow;

    private boolean exitRequest;

    Entity inTownHero, gateHero;

    public TownHud(Hirols game, TownDataComponent townDataComponent, Entity heroAtGate) {
        super(game, new FitViewport(1280,720));

        this.inTownHero = townDataComponent.heroInTown;
        this.gateHero = heroAtGate;

        createActors();
    }

    private void createActors() {
        topBar = new TopBar(game, stage);

        ButtonGroup<VisImageTextButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.setUncheckLast(false);

        Image townBackground = new Image(game.assetManager.get("towns/townBackground.png", Texture.class));
        townBackground.setFillParent(true);

        inTown = new Garrison(buttonGroup,false);
        outTown = new Garrison(buttonGroup, true);

        exitButton = new VisImageTextButton("Exit", new VisImageTextButton.VisImageTextButtonStyle(game.hudManager.skin.get("image-text-button", VisImageTextButton.VisImageTextButtonStyle.class)));
        exitButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class),338, 208, 112, 112)));
        HudManager.moveTextLabelBelowImage(exitButton, Scaling.fit);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitRequest = true;
            }
        });

        float width = stage.getWidth();
        float height = stage.getHeight();
        topBar.resize(width,height);
        inTown.resize(width,height);
        outTown.resize(width,height);
        float buttonSize = width<height ? width/10 : height/10;
        float imagePadding = buttonSize/10;
        exitButton.setBounds(buttonSize, height - buttonSize*2, buttonSize, buttonSize);
        exitButton.pad(imagePadding);

        ComponentMapper<HeroDataComponent> heroMap = ComponentMapper.getFor(HeroDataComponent.class);
        if(inTownHero == null) inTown.update(null);
        else inTown.update(heroMap.get(inTownHero));
        if(gateHero == null) outTown.update(null);
        else outTown.update(heroMap.get(gateHero));

        ImageButton.ImageButtonStyle townBuildingStyle = game.hudManager.skin.get("townBuildingStyle", ImageButton.ImageButtonStyle.class);
        barrackButton = new ImageButton(new ImageButton.ImageButtonStyle(townBuildingStyle));
        barrackButton.setBounds(780, 200, 300, 210);
        barrackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(addTownBuildingWindow(new Barracks(game))) {
                    townBuildingWindow.setSize(800,600);
                    townBuildingWindow.setPosition(200,100);
                }
            }
        });

        townHallButton = new ImageButton(new ImageButton.ImageButtonStyle(townBuildingStyle));
        townHallButton.setBounds(440, 470, 200, 160);
        townHallButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(addTownBuildingWindow(new TownHall())) {
                    townBuildingWindow.setBounds(200,200,600,400);
                }
            }
        });

        libraryButton = new ImageButton(new ImageButton.ImageButtonStyle(townBuildingStyle));
        libraryButton.setBounds(190, 445, 150, 130);
        libraryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(addTownBuildingWindow(new Library())) {
                    townBuildingWindow.setBounds(200,200,600,400);
                }
            }
        });

        marketButton = new ImageButton(new ImageButton.ImageButtonStyle(townBuildingStyle));
        marketButton.setBounds(290, 290, 200, 145);
        marketButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(addTownBuildingWindow(new Market())) {
                    townBuildingWindow.setBounds(200,200,600,400);
                }
            }
        });

        wallsButton = new ImageButton(new ImageButton.ImageButtonStyle(townBuildingStyle));
        wallsButton.setBounds(280, 175, 190, 80);
        wallsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(addTownBuildingWindow(new Walls())) {
                    townBuildingWindow.setBounds(200,200,600,400);
                }
            }
        });

        tavernButton = new ImageButton(new ImageButton.ImageButtonStyle(townBuildingStyle));
        tavernButton.setBounds(660, 420, 230, 150);
        tavernButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(addTownBuildingWindow(new Tavern(game))) {
                    townBuildingWindow.setBounds(200,300,800,200);
                }
            }
        });

        stage.addActor(townBackground);
        stage.addActor(topBar);
        stage.addActor(inTown);
        stage.addActor(outTown);
        stage.addActor(exitButton);
        stage.addActor(barrackButton);
        stage.addActor(townHallButton);
        stage.addActor(libraryButton);
        stage.addActor(marketButton);
        stage.addActor(wallsButton);
        stage.addActor(tavernButton);
    }

    @Override
    public void resize(float width, float height) {
        super.resize(width, height);
    }

    public boolean isExitRequest() {
        boolean temp = exitRequest;
        exitRequest = false;
        return temp;
    }

    private boolean removeTownBuildingWindow() {
        return (townBuildingWindow != null && townBuildingWindow.remove());
    }

    private boolean addTownBuildingWindow(TownBuilding townBuilding) {
        if(stage.getActors().contains(townBuildingWindow,true)) return false;
        townBuildingWindow = townBuilding;
        stage.addActor(townBuildingWindow);
        return true;
    }

    private class Garrison extends Table {
        private UnitsGrid units;
        private VisImageTextButton heroButton;
        private ButtonGroup buttonGroup;
        private boolean heroOnLeft;

        public Garrison(ButtonGroup<VisImageTextButton> buttonGroup, boolean heroOnLeft) {
            this.buttonGroup = buttonGroup;
            this.heroOnLeft = heroOnLeft;

            units = new UnitsGrid(game, buttonGroup);

            heroButton = new VisImageTextButton("Hero", new VisImageTextButton.VisImageTextButtonStyle(game.hudManager.skin.get("units-style", VisImageTextButton.VisImageTextButtonStyle.class)));
            HudManager.moveTextLabelBelowImage(heroButton, Scaling.fit);
            buttonGroup.add(heroButton);

            addActor(units);
            addActor(heroButton);
        }

        public void resize(float width, float height) {
            int divider = 20;
            int buttonSize = (int) (width / divider);
            float spacing = units.getSpacing();

            units.setItemSize(buttonSize);
            units.setSize((buttonSize+spacing)*5+spacing , buttonSize + spacing*2);

            if(!heroOnLeft) {
                setSize(units.getWidth(), units.getHeight()*2);
                setPosition(buttonSize,15);
                heroButton.setBounds(units.getWidth() + spacing, 0 + spacing, buttonSize*2, buttonSize*2);
            } else {
                setSize(units.getWidth(), units.getHeight()*2);
                setPosition(stage.getWidth() - getWidth() - buttonSize, 15);
                heroButton.setSize(buttonSize*2,buttonSize*2);
                heroButton.setPosition(-heroButton.getWidth() - spacing, 0 + spacing);
            }
        }

        public void update(HeroDataComponent heroDataComponent) {
            units.update(heroDataComponent);
            if(heroDataComponent == null) return;
            heroButton.setText(heroDataComponent.name);
            heroButton.getStyle().up = new SpriteDrawable(new Sprite(game.assetManager.get(heroDataComponent.hero.avatarPath, Texture.class)));
        }

    }

}

