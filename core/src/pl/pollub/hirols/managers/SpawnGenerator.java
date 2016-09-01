package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.pollub.hirols.animation.AnimationSet;
import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.LifePeriodComponent;
import pl.pollub.hirols.components.PlayerComponent;
import pl.pollub.hirols.components.PlayerDataComponent;
import pl.pollub.hirols.components.graphics.AnimationComponent;
import pl.pollub.hirols.components.graphics.BitmapFontComponent;
import pl.pollub.hirols.components.graphics.RenderableComponent;
import pl.pollub.hirols.components.graphics.TextureComponent;
import pl.pollub.hirols.components.graphics.TransparencyComponent;
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
        ComponentMapper<MapComponent> mapMapper = ComponentMapper.getFor(MapComponent.class);
        Random rand = new Random();

        Map<AnimationType, Map<Direction, Animation>> snakeAnimationMap = new HashMap<AnimationType, Map<Direction, Animation>>();
        Direction[] snakeStayDirections = new Direction[] {Direction.N, Direction.NE, Direction.E, Direction.SE, Direction.S, Direction.SW, Direction.W, Direction.NW};
        snakeAnimationMap.put(AnimationType.stand, AnimationManager.createAnimation(snakeStayDirections,
                game.assetManager.get("animations/snake_stay_animation.png", Texture.class), 32, 8, 0.06f));

        Map<AnimationType, Map<Direction, Animation>> mechAnimationMap = new HashMap<AnimationType, Map<Direction, Animation>>();
        mechAnimationMap.put(AnimationType.stand, AnimationManager.createAnimation(snakeStayDirections,
                game.assetManager.get("animations/standing_mech.png", Texture.class), 13, 8, 0.06f));

        for (int i = 0; i < 50; i++) {
            Vector2 position = Pools.obtain(Vector2.class);
            generateRandomPositionOnMap(position,map);

            int indexX = (int)Math.floor(position.x / map.getTileWidth());
            int indexY = (int)Math.floor(position.y / map.getTileHeight());

            Entity mapEntity = map.getEntity(indexX,indexY);

            mapEntity.add(game.engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), snakeAnimationMap), true, rand.nextFloat()))
                    .add(map.getGameMapComponent())
                    .add(game.engine.createComponent(TextureComponent.class).setSize(128, 128).setAdditionalOffset(-16, 0))
                    .add(game.engine.createComponent(RenderableComponent.class))
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

        Entity testText = game.engine.createEntity();

        testText.add(game.engine.createComponent(PositionComponent.class).init(50, 50))
                .add(map.getGameMapComponent())
                .add(game.engine.createComponent(BitmapFontComponent.class).init(game.assetManager.get("testFontSize32.ttf",BitmapFont.class), "teeest efwkjerurhjkjfiuhrejrfjfkejfr4hfruijrigj5ffjr5678909488"))
                .add(game.engine.createComponent(RenderableComponent.class))
                .add(game.engine.createComponent(TransparencyComponent.class).init(1))
                .add(game.engine.createComponent(LifePeriodComponent.class).init(1000));
        game.engine.addEntity(testText);

        spawnPlayers(game.engine, mechAnimationMap,map,mapMapper, game);
    }



    private static void spawnPlayers(Engine engine, Map<AnimationType, Map<Direction, Animation>> animationMap, pl.pollub.hirols.gameMap.Map map, ComponentMapper<MapComponent> mapMapper, Hirols game) {
        int playerId = -1;
        Entity player = game.engine.createEntity();
        player
                .add(map.getGameMapComponent())
                .add(new PlayerDataComponent())
                .add(new PlayerComponent());
        engine.addEntity(player);

        Vector2 firstHeroPos = Pools.obtain(Vector2.class);
        engine.addEntity(game.engine.createEntity()
                .add(game.engine.createComponent(PositionComponent.class).init(1728, 1728))
                .add(map.getGameMapComponent())
                .add(game.engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), animationMap), true,0f))
                .add(game.engine.createComponent(TextureComponent.class).setSize(84, 102))
                .add(game.engine.createComponent(RenderableComponent.class))
                .add(game.engine.createComponent(HeroDataComponent.class).init(++playerId, "Cwel", 10.f,new Sprite(game.assetManager.get("temp/portrait.png", Texture.class))))
                .add(game.engine.createComponent(VelocityComponent.class))
                .add(new PlayerComponent()));
                //.add(new SelectedHeroComponent()));
        Pools.free(firstHeroPos);
        for (int i = 0; i < 10; i++) {
            Vector2 heroPosition = Pools.obtain(Vector2.class);
            Entity hero = game.engine.createEntity();
            hero
                    .add(map.getGameMapComponent())
                    .add(game.engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), animationMap), true, 0f))
                    .add(game.engine.createComponent(PositionComponent.class).init(generateRandomPositionOnMap(heroPosition,map)))
                    .add(game.engine.createComponent(RenderableComponent.class))
                    .add(game.engine.createComponent(TextureComponent.class).setSize(84, 102))
                    .add(game.engine.createComponent(HeroDataComponent.class).init(++playerId,"nołnejm", 13f, new Sprite(game.assetManager.get("temp/portrait.png", Texture.class))))
                    .add(game.engine.createComponent(VelocityComponent.class))
                    .add(new PlayerComponent());
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