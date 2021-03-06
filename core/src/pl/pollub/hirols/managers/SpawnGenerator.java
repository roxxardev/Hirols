package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import pl.pollub.hirols.animation.AnimationSet;
import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.map.BannerComponent;
import pl.pollub.hirols.components.map.EnemyDataComponent;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.components.player.PlayerDataComponent;
import pl.pollub.hirols.components.graphics.AnimationComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.components.map.EnemyComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.MapComponent;
import pl.pollub.hirols.components.physics.PositionComponent;
import pl.pollub.hirols.components.physics.VelocityComponent;
import pl.pollub.hirols.managers.enums.*;

/**
 * Created by Eryk on 2015-12-02.
 */

public class SpawnGenerator {

    public static void loadEntities(Hirols game, pl.pollub.hirols.gameMap.Map map) {

        spawnEnemyRandomPlaces(game,map,game.unitsManager.smallWyvern,5);

        for (Class<? extends PlayerComponent> playerClass : game.gameManager.getPlayerClasses()) {
            spawnHeroes(game, map, playerClass);
        }
    }

    private static void spawnEnemyRandomPlaces(Hirols game, pl.pollub.hirols.gameMap.Map map, UnitsManager.Unit unit, int number) {
        ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);
        Random rand = new Random();
        Map<AnimationType, Map<Direction, Animation>> enemyAnimationMap = AnimationManager.createUnitAnimationMaps(unit, game);
        AnimationManager.AnimationInformation enemyAnimationInformation = unit.animationInformation;

        for (int i = 0; i < number; i++) {
            Vector2 position = Pools.obtain(Vector2.class);
            generateRandomPositionOnMap(position,map);

            int indexX = (int)Math.floor(position.x / map.getTileWidth());
            int indexY = (int)Math.floor(position.y / map.getTileHeight());

            Entity mapEntity = map.getEntity(indexX,indexY);

            if(game.engine.getEntities().contains(mapEntity, true)) continue;

            mapEntity.add(game.engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.STAND, Direction.getRandomDirection(), enemyAnimationMap), true, rand.nextFloat()))
                    .add(game.engine.createComponent(map.getGameMapComponentClazz()))
                    .add(game.engine.createComponent(TextureComponent.class).setSize(enemyAnimationInformation.size).setAdditionalOffset(enemyAnimationInformation.offset))
                    .add(game.engine.createComponent(RenderableComponent.class))
                    .add(game.engine.createComponent(EnemyDataComponent.class).init(unit, rand.nextInt(40) + 1))
                    .add(game.engine.createComponent(EnemyComponent.class).init(position,mapEntity,true));
            game.engine.addEntity(mapEntity);

            mapMapper.get(mapEntity).walkable = false;
            map.updateGraphConnectionsToNode(position.x,position.y,mapMapper.get(mapEntity).walkable);

            ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

            for(Entity entity : map.getAdjacentEntities(position.x,position.y)) {
                if(mapMapper.get(entity).walkable) {
                    entity.add(game.engine.createComponent(EnemyComponent.class).init(position, mapEntity,false));
                    mapMapper.get(entity).walkable = false;
                    map.updateGraphConnectionsToNode(posMap.get(entity).x,posMap.get(entity).y,false);
                }
            }
            Pools.free(position);
        }
    }


    private static void spawnHeroes(Hirols game, pl.pollub.hirols.gameMap.Map map, Class<? extends PlayerComponent> playerClass) {
        //TODO remove player id
        int heroID = -1;
        PooledEngine engine = game.engine;

        Map<AnimationType, Map<Direction, Animation>> orcHeroAnimationMap = AnimationManager.createHeroAnimationMap(game,game.unitsManager.heroOrcWarrior);
        Map<AnimationType, Map<Direction, Animation>> orcMageHeroAnimationMap = AnimationManager.createHeroAnimationMap(game, game.unitsManager.heroOrcMage);

        ImmutableArray<Entity> players = game.engine.getEntitiesFor(Family.all(PlayerDataComponent.class, playerClass).get());
        PlayerDataComponent playerDataComponent = ComponentMapper.getFor(PlayerDataComponent.class).get(players.first());

        for (int i = 0; i < 2; i++) {
            Map<AnimationType, Map<Direction, Animation>> animationMap = orcHeroAnimationMap;
            UnitsManager.Hero hero = game.unitsManager.heroOrcWarrior;

            if(i%2 == 0){
                animationMap = orcMageHeroAnimationMap;
                hero = game.unitsManager.heroOrcMage;
            }

            Sprite flagSprite = new Sprite(game.assetManager.get("temp/Flag.png", Texture.class));

            Vector2 heroPosition = Pools.obtain(Vector2.class);
            Entity heroEntity = engine.createEntity();
            heroEntity
                    .add(engine.createComponent(map.getGameMapComponentClazz()))
                    .add(engine.createComponent(AnimationComponent.class)
                            .init(new AnimationSet(AnimationType.STAND, Direction.getRandomDirection(), animationMap), true, 0f))
                    .add(engine.createComponent(PositionComponent.class).init(generateRandomPositionOnMap(heroPosition,map)))
                    .add(engine.createComponent(RenderableComponent.class))
                    .add(engine.createComponent(TextureComponent.class).setSize(128, 128).setAdditionalOffset(-16, -14))
                    .add(engine.createComponent(HeroDataComponent.class)
                            .init(++heroID,"nołnejm", 10f, hero, game.unitsManager.orc, game.unitsManager.oldShaman, game.unitsManager.smallWyvern, game.unitsManager.shaman, game.unitsManager.wyvern))
                    .add(engine.createComponent(VelocityComponent.class))
                    .add(engine.createComponent(BannerComponent.class).init(flagSprite, playerDataComponent.color, 0, (int) (map.getTileHeight() - flagSprite.getHeight())))
                    .add(engine.createComponent(playerClass));
            engine.addEntity(heroEntity);
            Pools.free(heroPosition);
        }

        ArrayList<UnitsManager.Unit> units = new ArrayList<UnitsManager.Unit>();
        units.add(game.unitsManager.oldShaman);
        units.add(game.unitsManager.orc);
        units.add(game.unitsManager.orcWarior);
        units.add(game.unitsManager.shaman);
        units.add(game.unitsManager.smallWyvern);
        units.add(game.unitsManager.wyvern);

        for(UnitsManager.Unit unit : units){
            AnimationManager.AnimationInformation animationInformation = unit.animationInformation;
            Map<AnimationType, Map<Direction, Animation>> animationMap = AnimationManager.createUnitAnimationMaps(unit, game);

            for (int i = 0; i < 1; i++) {
                Vector2 heroPosition = Pools.obtain(Vector2.class);
                Sprite flagSprite = new Sprite(game.assetManager.get("temp/Flag.png", Texture.class));
                Entity hero = engine.createEntity();
                hero
                        .add(engine.createComponent(map.getGameMapComponentClazz()))
                        .add(engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.STAND, Direction.getRandomDirection(), animationMap), true, 0f))
                        .add(engine.createComponent(PositionComponent.class).init(generateRandomPositionOnMap(heroPosition,map)))
                        .add(engine.createComponent(RenderableComponent.class).init(pl.pollub.hirols.managers.enums.RenderPriority.LAST))
                        .add(engine.createComponent(TextureComponent.class).setSize(animationInformation.size).setAdditionalOffset(animationInformation.offset))
                        .add(engine.createComponent(HeroDataComponent.class).init(++heroID,"nołnejm", 100000f, game.unitsManager.heroOrcMage, unit))
                        .add(engine.createComponent(BannerComponent.class).init(flagSprite, playerDataComponent.color, 0, (int) (map.getTileHeight() - flagSprite.getHeight())))
                        .add(engine.createComponent(VelocityComponent.class))
                        .add(engine.createComponent(playerClass));
                engine.addEntity(hero);
                Pools.free(heroPosition);
            }
        }
    }

    private static Vector2 generateRandomPositionOnMap(Vector2 position, pl.pollub.hirols.gameMap.Map map) {
        ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);
        int x;
        int y;
        Random rand = new Random();
        do {
            x = (rand.nextInt(map.getTileWidth()*map.getTileMapWidth()) / map.getTileWidth());
            y = (rand.nextInt(map.getTileHeight()*map.getTileMapHeight()) / map.getTileHeight());

        } while (!mapMapper.get(map.getEntity(x,y)).walkable);

        return position.set(x*map.getTileWidth(),y*map.getTileHeight());
    }


}