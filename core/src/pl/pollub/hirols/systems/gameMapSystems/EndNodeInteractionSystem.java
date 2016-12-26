package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.LifePeriodComponent;
import pl.pollub.hirols.components.TextureRenderableRemovalComponent;
import pl.pollub.hirols.components.map.BannerComponent;
import pl.pollub.hirols.components.map.EnemyDataComponent;
import pl.pollub.hirols.components.map.RecruitComponent;
import pl.pollub.hirols.components.map.RecruitDataComponent;
import pl.pollub.hirols.components.map.maps.PortalComponent;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.components.player.PlayerDataComponent;
import pl.pollub.hirols.components.graphics.BitmapFontComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TransparencyComponent;
import pl.pollub.hirols.components.map.ChestComponent;
import pl.pollub.hirols.components.map.EnemyComponent;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.components.map.GameMapDataComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.MapComponent;
import pl.pollub.hirols.components.map.MineComponent;
import pl.pollub.hirols.components.map.ResourceComponent;
import pl.pollub.hirols.components.SelectedComponent;
import pl.pollub.hirols.components.map.TownComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.components.physics.VelocityComponent;
import pl.pollub.hirols.screens.BattleScreen;
import pl.pollub.hirols.screens.TownScreen;

/**
 * Created by Eryk on 2016-05-15.
 */
public class EndNodeInteractionSystem extends GameMapEntitySystem {

    private ComponentMapper<ResourceComponent> resourceMap = ComponentMapper.getFor(ResourceComponent.class);
    private ComponentMapper<HeroDataComponent> heroDataMap = ComponentMapper.getFor(HeroDataComponent.class);
    private ComponentMapper<PlayerDataComponent> playerDataMap = ComponentMapper.getFor(PlayerDataComponent.class);
    private ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);
    private ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TownComponent> townMap = ComponentMapper.getFor(TownComponent.class);
    private ComponentMapper<MineComponent> mineMap = ComponentMapper.getFor(MineComponent.class);
    private ComponentMapper<ChestComponent> chestMap = ComponentMapper.getFor(ChestComponent.class);
    private ComponentMapper<EnemyComponent> enemyMap = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<EnemyDataComponent> enemyDataMap = ComponentMapper.getFor(EnemyDataComponent.class);
    private ComponentMapper<PortalComponent> portalMap = ComponentMapper.getFor(PortalComponent.class);
    private ComponentMapper<BannerComponent> bannerMap = ComponentMapper.getFor(BannerComponent.class);
    private ComponentMapper<RecruitComponent> recruitMap = ComponentMapper.getFor(RecruitComponent.class);
    private ComponentMapper<RecruitDataComponent> recruitDataMap = ComponentMapper.getFor(RecruitDataComponent.class);


    private final Hirols game;

    public EndNodeInteractionSystem(int priority, Class<? extends GameMapComponent> gameMapClass, Hirols game) {
        super(priority, gameMapClass);
        this.game = game;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        Class<? extends PlayerComponent> currentPlayerClass = game.gameManager.getCurrentPlayerClass();
        ImmutableArray<Entity> selectedHeroes = getEngine().getEntitiesFor(Family.all(HeroDataComponent.class, SelectedComponent.class, gameMapClass, currentPlayerClass).get());
        if (selectedHeroes.size() < 1) return;
        GameMapDataComponent gameMapData = gameMapDataMapper.get(this.gameMapDataArray.first());
        Entity selectedHero = selectedHeroes.first();
        HeroDataComponent selectedHeroData = heroDataMap.get(selectedHero);
        if (!(!selectedHeroData.heroPath.hasWalkNodes() && !selectedHeroData.heroPath.hasStandNodes() && selectedHeroData.heroPath.getTargetEntity() != null))
            return;

        Entity targetEntity = selectedHeroData.heroPath.getTargetEntity();
        PlayerDataComponent playerData = playerDataMap.get(game.gameManager.getCurrentPlayer());

        if(selectedHeroData.pathEntities.isEmpty()){
            if (townMap.has(targetEntity)) {
                TownComponent townComponent = townMap.get(targetEntity);
                Gdx.app.log("EndNodeInteractionSystem", "Interaction with town: ");
                game.setScreen(new TownScreen(game,townComponent.enterEntity));
            } else if (mineMap.has(targetEntity)) {
                Gdx.app.log("EndNodeInteractionSystem", "Interaction with mine");
                Class<? extends PlayerComponent> mineOwner = game.gameManager.attachedToPlayer(targetEntity);
                if(mineOwner != null) targetEntity.remove(mineOwner);
                if(mineOwner != currentPlayerClass) {
                    bannerMap.get(targetEntity).color.set(playerData.color);
                    targetEntity.add(game.engine.createComponent(currentPlayerClass));
                    Gdx.app.log("EndNodeInteractionSystem", "Mine taken by "+currentPlayerClass.getSimpleName() + " from " + ((mineOwner != null) ? mineOwner.getSimpleName() : "no one"));
                }
            } else if(portalMap.has(targetEntity)) {
                Gdx.app.log("EndNodeInteractionSystem", "Interaction with portal");
                Entity destinationPortalEntity = portalMap.get(targetEntity).destinationMapEntity;
                PositionComponent destinationPortalPosition = posMap.get(destinationPortalEntity);
                posMap.get(selectedHero).init(destinationPortalPosition.x, destinationPortalPosition.y);
                gameMapData.gameMapCam.position.set(destinationPortalPosition.x, destinationPortalPosition.y, 0);
            } else if(recruitMap.has(targetEntity)) {
                Gdx.app.log("EndNodeInteractionSystem", "Interaction with recruit building");
                Class<? extends PlayerComponent> recruitBuildingOwner = game.gameManager.attachedToPlayer(targetEntity);
                if(recruitBuildingOwner != null ) targetEntity.remove(recruitBuildingOwner);
                if(recruitBuildingOwner != currentPlayerClass) {
                    bannerMap.get(targetEntity).color.set(playerData.color);
                    targetEntity.add(game.engine.createComponent(currentPlayerClass));
                    Gdx.app.log("EndNodeInteractionSystem", "Recruit building taken by "+currentPlayerClass.getSimpleName() + " from " + ((recruitBuildingOwner != null) ? recruitBuildingOwner.getSimpleName() : "no one"));
                }
                RecruitDataComponent recruitData = recruitDataMap.get(targetEntity);
                if(selectedHeroData.army.addUnit(recruitData.unit, recruitData.currentNumber)) {
                    Gdx.app.log("EndNodeInteractionSystem", "Recruited " + recruitData.currentNumber + " " + recruitData.unit.name);
                    recruitData.currentNumber = 0;
                }
            }
            selectedHeroData.heroPath.setTargetEntity(null);
            return;
        }

        Entity pathEntity = selectedHeroData.pathEntities.remove(0);

        if (resourceMap.has(targetEntity)) {
            ResourceComponent resourceComponent = resourceMap.get(targetEntity);

            playerData.resources.put(resourceComponent.resourceType, playerData.resources.get(resourceComponent.resourceType) + resourceComponent.amount);
            String resourceText = "Picked up " + resourceComponent.amount + " " + resourceComponent.resourceType.toString().toLowerCase() + " !";
            gameMapData.hud.getTopBar().updateResources();
            targetEntity.remove(ResourceComponent.class);
            targetEntity
                    .add(game.engine.createComponent(LifePeriodComponent.class).init(1000))
                    .add(game.engine.createComponent(TransparencyComponent.class))
                    .add(game.engine.createComponent(TextureRenderableRemovalComponent.class));
            mapMapper.get(targetEntity).walkable = true;
            PositionComponent resourcePosition = posMap.get(targetEntity);
            gameMapData.map.updateGraphConnectionsToNode(resourcePosition.x, resourcePosition.y, true);
            BitmapFont bitmapFont = game.assetManager.get("testFontSize32.ttf", BitmapFont.class);
            BitmapFontComponent bitmapFontComponent = game.engine.createComponent(BitmapFontComponent.class).init(new BitmapFont(bitmapFont.getData(), bitmapFont.getRegion(), bitmapFont.usesIntegerPositions()), resourceText);
            bitmapFontComponent.bitmapFont.setColor(new Color(0,0,0,1));
            getEngine().addEntity(game.engine.createEntity()
                    .add(game.engine.createComponent(LifePeriodComponent.class).init(3000))
                    .add(bitmapFontComponent)
                    .add(game.engine.createComponent(TransparencyComponent.class).init(1))
                    .add(game.engine.createComponent(RenderableComponent.class))
                    .add(game.engine.createComponent(PositionComponent.class).init(resourcePosition.x - gameMapData.map.getTileWidth(), resourcePosition.y + gameMapData.map.getTileHeight()))
                    .add(game.engine.createComponent(VelocityComponent.class))
                    .add(game.engine.createComponent(gameMapData.map.getGameMapComponentClazz())));
            getEngine().removeEntity(pathEntity);
            Gdx.app.log("EndNodeInteractionSystem", "Interaction with resource: " + resourceText);
        } else if (chestMap.has(targetEntity)) {
            Gdx.app.log("EndNodeInteractionSystem", "Interaction with chest: ");
            ChestComponent chestComponent = chestMap.get(targetEntity);
            String chestText = "";
            if(chestComponent.resourceType != null && chestComponent.amountResource > 0) {
                playerData.resources.put(chestComponent.resourceType, playerData.resources.get(chestComponent.resourceType) + chestComponent.amountResource);
                chestText = "Picked up " + chestComponent.amountResource + " " + chestComponent.resourceType.toString().toLowerCase() + " ";
            }
            if(chestComponent.experience > 0) {
                selectedHeroData.heroLevel.addExperience(chestComponent.experience);
                chestText += "Picked up " + chestComponent.experience + " experience";
            }
            if(chestText.length() > 0) {
                chestText += "!";
                PositionComponent chestPosition = posMap.get(targetEntity);
                gameMapData.hud.getTopBar().updateResources();
                targetEntity.remove(ChestComponent.class);
                targetEntity
                        .add(game.engine.createComponent(LifePeriodComponent.class).init(1000))
                        .add(game.engine.createComponent(TransparencyComponent.class))
                        .add(game.engine.createComponent(TextureRenderableRemovalComponent.class));
                mapMapper.get(targetEntity).walkable = true;
                gameMapData.map.updateGraphConnectionsToNode(chestPosition.x, chestPosition.y, true);

                BitmapFont bitmapFont = game.assetManager.get("testFontSize32.ttf", BitmapFont.class);
                BitmapFontComponent bitmapFontComponent = game.engine.createComponent(BitmapFontComponent.class).init(new BitmapFont(bitmapFont.getData(), bitmapFont.getRegion(), bitmapFont.usesIntegerPositions()), chestText);
                bitmapFontComponent.bitmapFont.setColor(new Color(0,0,0,1));
                getEngine().addEntity(game.engine.createEntity()
                        .add(game.engine.createComponent(LifePeriodComponent.class).init(3000))
                        .add(bitmapFontComponent)
                        .add(game.engine.createComponent(TransparencyComponent.class).init(1))
                        .add(game.engine.createComponent(RenderableComponent.class))
                        .add(game.engine.createComponent(PositionComponent.class).init(chestPosition.x - gameMapData.map.getTileWidth(), chestPosition.y + gameMapData.map.getTileHeight()))
                        .add(game.engine.createComponent(VelocityComponent.class))
                        .add(game.engine.createComponent(gameMapData.map.getGameMapComponentClazz())));
            }
            getEngine().removeEntity(pathEntity);
        } else if (enemyMap.has(targetEntity)) {
            Gdx.app.log("EndNodeInteractionSystem", "Interaction with enemy: ");
            getEngine().removeEntity(pathEntity);
            Entity enemyEntity = enemyMap.get(targetEntity).enemyEntity;
            game.setScreen(new BattleScreen(game,selectedHeroData, enemyDataMap.get(enemyEntity)));
        }

        selectedHeroData.heroPath.setTargetEntity(null);
    }
}
