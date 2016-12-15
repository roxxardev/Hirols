package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.pollub.hirols.animation.AnimationSet;
import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.SelectedComponent;
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
import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;

/**
 * Created by Eryk on 2015-12-02.
 */

public class SpawnGenerator {

    public static void loadEntities(Hirols game, pl.pollub.hirols.gameMap.Map map) {

        spawnEnemyRandomPlaces(game,map,game.unitsManager.smallWyvern,6);

        spawnPlayerAndHeroes(game, map, game.gameManager.getCurrentPlayerClass());
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
                    .add(game.engine.createComponent(EnemyDataComponent.class).init(unit))
                    .add(game.engine.createComponent(EnemyComponent.class).init(position,true));
            game.engine.addEntity(mapEntity);

            mapMapper.get(mapEntity).walkable = false;
            map.updateGraphConnectionsToNode(position.x,position.y,mapMapper.get(mapEntity).walkable);

            ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

            for(Entity entity : map.getAdjacentEntities(position.x,position.y)) {
                if(mapMapper.get(entity).walkable) {
                    entity.add(game.engine.createComponent(EnemyComponent.class).init(position,false));
                    mapMapper.get(entity).walkable = false;
                    map.updateGraphConnectionsToNode(posMap.get(entity).x,posMap.get(entity).y,false);
                }
            }
            Pools.free(position);
        }
    }


    private static void spawnPlayerAndHeroes(Hirols game, pl.pollub.hirols.gameMap.Map map, Class<? extends PlayerComponent> playerClass) {
        //TODO remove player id
        int playerId = -1;
        PooledEngine engine = game.engine;

        Map<AnimationType, Map<Direction, Animation>> orcHeroAnimationMap = AnimationManager.createHeroAnimationMap(game,game.unitsManager.heroOrcWarrior);
        Map<AnimationType, Map<Direction, Animation>> orcMageHeroAnimationMap = AnimationManager.createHeroAnimationMap(game, game.unitsManager.heroOrcMage);

        for (int i = 0; i < 10; i++) {
            Map<AnimationType, Map<Direction, Animation>> animationMap = orcHeroAnimationMap;
            if(i%2 == 0){
                animationMap = orcMageHeroAnimationMap;
            }
            Vector2 heroPosition = Pools.obtain(Vector2.class);
            Entity hero = engine.createEntity();
            hero
                    .add(engine.createComponent(map.getGameMapComponentClazz()))
                    .add(engine.createComponent(AnimationComponent.class)
                            .init(new AnimationSet(AnimationType.STAND, Direction.getRandomDirection(), animationMap), true, 0f))
                    .add(engine.createComponent(PositionComponent.class).init(generateRandomPositionOnMap(heroPosition,map)))
                    .add(engine.createComponent(RenderableComponent.class))
                    .add(engine.createComponent(TextureComponent.class).setSize(128, 128).setAdditionalOffset(-16, -14))
                    .add(engine.createComponent(HeroDataComponent.class)
                            .init(++playerId,"nołnejm", 10f, new Sprite(game.assetManager.get("temp/orki.png", Texture.class))))
                    .add(engine.createComponent(VelocityComponent.class))
                    .add(engine.createComponent(playerClass));
            engine.addEntity(hero);
            Pools.free(heroPosition);
        }

        Map<AnimationType, Map<Direction, Animation>> wywernMap = AnimationManager.createUnitAnimationMaps(game.unitsManager.wyvern, game);
        for (int i = 0; i < 5; i++) {
            Vector2 heroPosition = Pools.obtain(Vector2.class);
            Entity hero = engine.createEntity();
            hero
                    .add(engine.createComponent(map.getGameMapComponentClazz()))
                    .add(engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.STAND, Direction.getRandomDirection(), wywernMap), true, 0f))
                    .add(engine.createComponent(PositionComponent.class).init(generateRandomPositionOnMap(heroPosition,map)))
                    .add(engine.createComponent(RenderableComponent.class))
                    .add(engine.createComponent(TextureComponent.class).setSize(256, 256).setAdditionalOffset(-80, -70))
                    .add(engine.createComponent(HeroDataComponent.class).init(++playerId,"nołnejm", 100000f, new Sprite(game.assetManager.get("temp/portrait.png", Texture.class))))
                    .add(engine.createComponent(VelocityComponent.class))
                    .add(engine.createComponent(playerClass));
            engine.addEntity(hero);
            Pools.free(heroPosition);
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