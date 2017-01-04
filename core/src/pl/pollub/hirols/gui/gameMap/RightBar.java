package pl.pollub.hirols.gui.gameMap;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.TownDataComponent;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.managers.HudManager;
import pl.pollub.hirols.screens.TownScreen;
import pl.pollub.hirols.systems.gameMapSystems.MapInteractionSystem;

/**
 * Created by erykp_000 on 2016-08-14.
 */
public class RightBar extends Table {
    private final Hirols game;
    private final Stage stage;
    private GameMapHud gameMapHud;

    private boolean slided = false;

    private VisImageButton moveButton, turnButton;
    private VisTextButton changeButton;
    private VisScrollPane scrollPaneHeroes, scrollPaneTowns;
    private GridGroupTowns gridGroupTowns;
    private GridGroupHeroes gridGroupHeroes;
    private Image rightBarDragImage;

    private Class<? extends PlayerComponent> currentPlayer;

    RightBar(Hirols game, GameMapHud gameMapHud) {
        this.game = game;
        this.stage = gameMapHud.getStage();
        this.gameMapHud = gameMapHud;

        setTouchable(Touchable.enabled);
        setBackground(game.hudManager.getTransparentBackground());
        setDebug(game.hudManager.debug);

        createActors();
        updatePlayer();

        //TODO zmienic z Marcinowego na jakies czytelne
        addListener(new ActorGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                clearActions();
                setX(getX() + deltaX);
                if (getX() < stage.getWidth() - getWidth()) {
                    setX(stage.getWidth() - getWidth());
                    slided = true;
                } else if (getX() > stage.getWidth() - getWidth() / 8) {
                    setX(stage.getWidth() - stage.getWidth() / 30);
                    slided = false;
                }
                rightBarDragImage.addAction(Actions.fadeOut(0.3f));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (getX() <= stage.getWidth() - getWidth() / 4 && !slided || getX() < stage.getWidth() - getWidth() * 3 / 4) {
                    addAction(Actions.moveTo(stage.getWidth() - getWidth(), 0, 0.3f));
                } else if (getX() >= stage.getWidth() - getWidth() * 3 / 4 && slided || getX() > stage.getWidth() - getWidth() / 4) {
                    addAction(Actions.moveTo(stage.getWidth(), 0, 0.3f));
                    rightBarDragImage.addAction(Actions.fadeIn(0.3f));
                }
            }
        });
    }

    public void updateSelectedHero(Entity selectedHero) {
        gridGroupHeroes.updateHero(selectedHero);
    }

    public boolean updatePlayer() {
        Class<? extends PlayerComponent> player = game.gameManager.getCurrentPlayerClass();
        if (currentPlayer == player) {
            updateTownsAndHeroes(gameMapHud.gameMapScreen.getGameMapComponentClass(), gameMapHud);
            return false;
        }
        currentPlayer = player;

        gridGroupHeroes.clear();
        gridGroupTowns.clear();

        updateTownsAndHeroes(gameMapHud.gameMapScreen.getGameMapComponentClass(), gameMapHud);
        return true;
    }

    private void createActors() {
        moveButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(game.hudManager.skin.get("image-button", VisImageButton.VisImageButtonStyle.class)));
        moveButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class), 0, 2, 128, 176)));
        turnButton = new VisImageButton(new VisImageButton.VisImageButtonStyle(game.hudManager.skin.get("image-button", VisImageButton.VisImageButtonStyle.class)));
        turnButton.getStyle().imageUp = new SpriteDrawable(new Sprite(new TextureRegion(game.assetManager.get("ui/button-images.png", Texture.class), 128, 0, 104, 178)));

        turnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MapInteractionSystem mapInteractionSystem = game.engine.getSystem(MapInteractionSystem.class);
                mapInteractionSystem.newTurn();
            }
        });

        moveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MapInteractionSystem mapInteractionSystem = game.engine.getSystem(MapInteractionSystem.class);
                ImmutableArray<Entity> heroes = mapInteractionSystem.getSelectedHeroes();
                if (heroes.size() < 1) return;
                Entity selectedHero = heroes.first();
                HeroDataComponent selectedHeroData = ComponentMapper.getFor(HeroDataComponent.class).get(selectedHero);
                selectedHeroData.heroPath.followPath();
            }
        });

        addActor(moveButton);
        addActor(turnButton);

        gridGroupHeroes = new GridGroupHeroes();
        gridGroupTowns = new GridGroupTowns();


        scrollPaneHeroes = new VisScrollPane(gridGroupHeroes) {{
            setForceScroll(false, true);
            setScrollingDisabled(true, false);
            setVisible(true);
            setFadeScrollBars(false);
        }};

        scrollPaneTowns = new VisScrollPane(gridGroupTowns) {{
            setForceScroll(false, true);
            setScrollingDisabled(true, false);
            setVisible(false);
            setFadeScrollBars(false);
        }};

        changeButton = new VisTextButton("HEROES TOWNS", game.hudManager.skin.get("text-button", VisTextButton.VisTextButtonStyle.class)) {{
            addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (scrollPaneHeroes.isVisible()) {
                        scrollPaneHeroes.setVisible(false);
                        scrollPaneTowns.setVisible(true);
                    } else {
                        scrollPaneHeroes.setVisible(true);
                        scrollPaneTowns.setVisible(false);
                    }
                }
            });
        }};

        rightBarDragImage = new Image(game.assetManager.get("ui/minimapDrag.png", Texture.class));

        addActor(changeButton);
        addActor(scrollPaneHeroes);
        addActor(scrollPaneTowns);
        addActor(rightBarDragImage);
    }

    public void updateTownsAndHeroes(Class<? extends GameMapComponent> gameMapComponent, GameMapHud gameMapHud) {
        ImmutableArray<Entity> playerHeroes = game.engine.getEntitiesFor(Family.all(HeroDataComponent.class, gameMapComponent, game.gameManager.getCurrentPlayerClass()).get());
        ArrayList<Entity> currentHeroesInGridGroup = new ArrayList<>(gridGroupHeroes.heroTableMap.keySet());
        for (Entity hero : currentHeroesInGridGroup) {
            if (!playerHeroes.contains(hero, true)) {
                gridGroupHeroes.removeHero(hero);
            } else {
                gridGroupHeroes.updateHero(hero);
            }
        }
        for (Entity hero : playerHeroes) {
            gridGroupHeroes.addHero(hero, gameMapHud);
        }

        ImmutableArray<Entity> playerTowns = game.engine.getEntitiesFor(Family.all(TownDataComponent.class, gameMapComponent, game.gameManager.getCurrentPlayerClass()).get());
        ArrayList<Entity> currentTownsInGridGroup = new ArrayList<>(gridGroupTowns.townButtonMap.keySet());
        for (Entity town : currentTownsInGridGroup) {
            if (!playerTowns.contains(town, true)) {
                gridGroupTowns.removeTown(town);
            }
        }
        for (Entity town : playerTowns) {
            gridGroupTowns.addTown(town);
        }
    }

    public void update() {
        if (this.hasActions()) {
            if (this.getX() < stage.getWidth() - this.getWidth() * 3 / 4)
                slided = true;
            if (this.getX() > stage.getWidth() - this.getWidth() / 4)
                slided = false;
        }
    }

    public void resize(float width, float height, float topBarHeight) {
        if (slided) {
            setSize(width / 4, height - topBarHeight);
            setPosition(width - getWidth(), 0);
        } else {
            setSize(width / 4, height - topBarHeight);
            setPosition(width, 0);
        }

        float buttonSize = width < height ? width / 10 : height / 10;
        float imagePadding = buttonSize / 10;

        moveButton.setSize(buttonSize, buttonSize);
        moveButton.setPosition(-buttonSize * 3 / 2, getHeight() - buttonSize * 3 / 2);
        moveButton.pad(imagePadding);

        turnButton.setSize(buttonSize, buttonSize);
        turnButton.setPosition(-buttonSize * 3 / 2, buttonSize / 2);
        turnButton.pad(imagePadding);

        float pad = width < height ? width / 30 : height / 30;

        changeButton.setBounds(pad, pad, getWidth() - 2 * pad, pad);
        scrollPaneHeroes.setBounds(pad, pad + changeButton.getY() + pad, getWidth() - 2 * pad, getHeight() - changeButton.getHeight() - 3 * pad);
        scrollPaneTowns.setBounds(pad, pad + changeButton.getY() + pad, getWidth() - 2 * pad, getHeight() - changeButton.getHeight() - 3 * pad);
        int gridSize = scrollPaneHeroes.getWidth() < scrollPaneHeroes.getHeight() ? (int) (scrollPaneHeroes.getWidth() - pad) : (int) ((scrollPaneHeroes.getWidth() - pad) / 2);
        gridGroupHeroes.resize(gridSize - 2 * gridGroupHeroes.getSpacing(), gridSize - 2 * gridGroupHeroes.getSpacing());
        gridGroupTowns.resize(gridSize - 2 * gridGroupTowns.getSpacing(), gridSize - 2 * gridGroupTowns.getSpacing());

        rightBarDragImage.setSize(buttonSize, 3 * buttonSize);
        rightBarDragImage.setPosition(-rightBarDragImage.getWidth(), getHeight() / 2 - rightBarDragImage.getHeight() / 2);

    }

    private class GridGroupTowns extends GridGroup {
        final Map<Entity, TownButton> townButtonMap = new HashMap<>();

        @Override
        public void clear() {
            super.clear();
            townButtonMap.clear();
        }

        public boolean addTown(Entity townEntity) {
            TownDataComponent townDataComponent = ComponentMapper.getFor(TownDataComponent.class).get(townEntity);
            if (townButtonMap.containsKey(townEntity)) {
                Gdx.app.log("Hud - > RightBar", "Town already added to GridGroup!");
                return false;
            }
            //TODO get texture path from town data
            TownButton townButton = new TownButton(townDataComponent.name, new SpriteDrawable(new Sprite(game.assetManager.get("towns/OrcTown.png", Texture.class))), townEntity);

            addActor(townButton);
            townButtonMap.put(townEntity, townButton);
            return true;
        }

        public boolean removeTown(Entity townEntity) {
            if (!townButtonMap.containsKey(townEntity)) {
                Gdx.app.log("Hud -> RightBar", "There is no such town to remove");
                return false;
            }
            removeActor(townButtonMap.remove(townEntity));
            return true;
        }


        public void resize(float itemWidth, float itemHeight) {
            setItemSize(itemWidth, itemHeight);
        }

        private class TownButton extends VisImageTextButton {

            private Entity townEntity;

            public TownButton(String text, Drawable imageUp, final Entity townEntity) {
                super(text, imageUp);
                this.townEntity = townEntity;
                HudManager.moveTextLabelBelowImage(this, Scaling.fit);
                addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.setScreen(new TownScreen(game, townEntity));
                    }
                });
            }

            public Entity getTownEntity() {
                return townEntity;
            }
        }
    }

    private class GridGroupHeroes extends GridGroup {
        final Map<Entity, HeroTable> heroTableMap = new HashMap<>();

        @Override
        public void clear() {
            super.clear();
            heroTableMap.clear();
        }

        public boolean updateHero(Entity heroEntity) {
            HeroTable heroTable = heroTableMap.get(heroEntity);
            if (heroTable == null) return false;
            HeroDataComponent heroData = ComponentMapper.getFor(HeroDataComponent.class).get(heroEntity);
            heroTable.updateMagicProgressBar(heroData.magicPoints);
            heroTable.updateMovementProgressBar(heroData.movementPoints);
            return true;
        }

        public boolean addHero(final Entity heroEntity, final GameMapHud gameMapHud) {
            final HeroDataComponent heroData = ComponentMapper.getFor(HeroDataComponent.class).get(heroEntity);

            if (heroTableMap.get(heroEntity) != null) {
                Gdx.app.log("Hud -> RightBar", "Hero already added to GridGroup!");
                return false;
            }

            Image image = new Image(game.assetManager.get(heroData.hero.avatarPath, Texture.class));

            FixedProgressBar progressBarLeft = new FixedProgressBar(0, 50, 0.5f, true);
            FixedProgressBar progressBarRight = new FixedProgressBar(0, 80, 0.5f, true);

            final HeroTable heroTable = new HeroTable(image, progressBarLeft, progressBarRight);

            image.addListener(new ActorGestureListener(20, 0.4f, 0.6f, 0.15f) {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    MapInteractionSystem mapInteractionSystem = game.engine.getSystem(MapInteractionSystem.class);
                    if (!mapInteractionSystem.changeSelectedHero(heroEntity)) {
                        mapInteractionSystem.setCameraPositionOnHero(heroEntity);

                    }
                    scrollPaneHeroes.scrollTo(0,heroTable.getY() + heroTable.getHeight()/2,0,0, true, true);
                    super.tap(event, x, y, count, button);
                }

                @Override
                public boolean longPress(Actor actor, float x, float y) {
                    gameMapHud.addLongPressWindow(heroData);
                    return super.longPress(actor, x, y);
                }
            });



            addActor(heroTable);
            heroTableMap.put(heroEntity, heroTable);
            updateHero(heroEntity);
            return true;
        }

        public void removeHero(final Entity heroEntity) {
            HeroDataComponent heroData = ComponentMapper.getFor(HeroDataComponent.class).get(heroEntity);
            if (heroTableMap.get(heroEntity) == null) {
                Gdx.app.log("Hud -> RightBar", "There is no hero id " + heroData.id + "!");
                return;
            }

            removeActor(heroTableMap.remove(heroEntity));
        }

        public void resize(float itemWidth, float itemHeight) {
            setItemSize(itemWidth, itemHeight);
            for (HeroTable table : heroTableMap.values()) {
                table.setSize(itemWidth, itemHeight);
                float progressBarWidth = itemWidth / 10;
                table.movement.getStyle().background.setMinWidth(progressBarWidth);
                table.movement.getStyle().knob.setMinWidth(progressBarWidth);
            }
        }

        private class HeroTable extends Table {
            final Image image;
            final FixedProgressBar movement, magic;
            private Sprite backgroundSprite;

            public HeroTable(Image image, final FixedProgressBar movement, FixedProgressBar magic) {
                this.image = image;
                this.movement = movement;
                this.magic = magic;


                backgroundSprite = new Sprite(game.hudManager.getWhiteTexture());
                backgroundSprite.setColor(0, 0, 0, 0.9f);
                setBackground(new SpriteDrawable(backgroundSprite));

                float duration = 0.6f;
                magic.setAnimateDuration(duration);
                movement.setAnimateDuration(duration);
                image.setHeight(0);
                image.setScaling(Scaling.fit);
                add(movement).fillY();
                add(image).expand().fill();
                add(magic).fillY();
            }

            void updateMagicProgressBar(float value) {
                magic.setValue(value);
            }

            void updateMovementProgressBar(float value) {
                movement.setValue(value);
            }
        }

        private class FixedProgressBar extends VisProgressBar {
            public FixedProgressBar(float min, float max, float stepSize, boolean vertical) {
                super(min, max, stepSize, vertical);
            }

            @Override
            public float getPrefWidth() {
                final Drawable knob = getKnobDrawable();
                final Drawable bg = (isDisabled() && getStyle().disabledBackground != null) ? getStyle().disabledBackground : getStyle().background;
                return Math.max(knob == null ? 0 : knob.getMinWidth(), bg.getMinWidth());
            }

            @Override
            public float getPrefHeight() {
                final Drawable knob = getKnobDrawable();
                final Drawable bg = (isDisabled() && getStyle().disabledBackground != null) ? getStyle().disabledBackground : getStyle().background;
                return Math.max(knob == null ? 0 : knob.getMinHeight(), bg == null ? 0 : bg.getMinHeight());
            }
        }

    }
}
