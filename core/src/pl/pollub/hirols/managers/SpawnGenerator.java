package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

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
import pl.pollub.hirols.components.map.SelectedHeroComponent;
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

            Vector2 position = generateRandomPositionOnMap(map, mapMapper);
            int indexX = (int)Math.floor(position.x / map.getTileWidth());
            int indexY = (int)Math.floor(position.y / map.getTileHeight());

            Entity mapEntity = map.getEntity(indexX,indexY);

            mapEntity.add(new AnimationComponent(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), snakeAnimationMap), true, rand.nextFloat()))
                    .add(map.getGameMapComponent())
                    .add(new TextureComponent(128, 128, -16, 0))
                    .add(new RenderableComponent())
                    .add(new EnemyComponent(position.x,position.y,i,true));
            game.engine.addEntity(mapEntity);

            mapMapper.get(mapEntity).walkable = false;
            map.updateGraphConnectionsToNode(position.x,position.y,mapMapper.get(mapEntity).walkable);

            ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);

            for(Entity entity : map.getAdjacentEntities(position.x,position.y)) {
                if(mapMapper.get(entity).walkable) {
                    entity.add(new EnemyComponent(position.x,position.y,i,false));
                    mapMapper.get(entity).walkable = false;
                    map.updateGraphConnectionsToNode(posMap.get(entity).x,posMap.get(entity).y,false);
                }
            }
        }

        Entity testText = new Entity();

        testText.add(new PositionComponent(50, 50))
                .add(map.getGameMapComponent())
                .add(new BitmapFontComponent(game.assetManager.get("testFontSize32.ttf",BitmapFont.class), "teeest efwkjerurhjkjfiuhrejrfjfkejfr4hfruijrigj5ffjr5678909488"))
                .add(new RenderableComponent())
                .add(new TransparencyComponent(1))
                .add(new LifePeriodComponent(1000));
        game.engine.addEntity(testText);

        spawnPlayers(game.engine, mechAnimationMap,map,mapMapper, game);
    }



    private static void spawnPlayers(Engine engine, Map<AnimationType, Map<Direction, Animation>> animationMap, pl.pollub.hirols.gameMap.Map map, ComponentMapper<MapComponent> mapMapper, Hirols game) {
        int playerId = -1;
        Entity player = new Entity();
        player
                .add(map.getGameMapComponent())
                .add(new PlayerDataComponent())
                .add(new PlayerComponent());
        engine.addEntity(player);

        engine.addEntity(new Entity()
                .add(new PositionComponent(1728, 1728))
                .add(map.getGameMapComponent())
                .add(new AnimationComponent(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), animationMap), true))
                .add(new TextureComponent(84, 102))
                .add(new RenderableComponent())
                .add(new HeroDataComponent(++playerId, "Cwel", 10.f,new Sprite(game.assetManager.get("temp/portrait.png", Texture.class))))
                .add(new VelocityComponent(new Vector2(0, 0)))
                .add(new PlayerComponent())
                .add(new SelectedHeroComponent()));
        for (int i = 0; i < 10; i++) {
            Entity hero = new Entity();
            hero
                    .add(map.getGameMapComponent())
                    .add(new AnimationComponent(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), animationMap), true))
                    .add(new PositionComponent(generateRandomPositionOnMap(map,mapMapper)))
                    .add(new RenderableComponent())
                    .add(new TextureComponent(84, 102))
                    .add(new HeroDataComponent(++playerId,"nołnejm", 13f, new Sprite(game.assetManager.get("temp/portrait.png", Texture.class))))
                    .add(new VelocityComponent(new Vector2(0, 0)))
                    .add(new PlayerComponent());
            engine.addEntity(hero);
        }
    }

    private static Vector2 generateRandomPositionOnMap(pl.pollub.hirols.gameMap.Map map, ComponentMapper<MapComponent> mapMapper) {
        int x;
        int y;
        Random rand = new Random();
        do {
            x = (rand.nextInt(map.getTileWidth()*map.getTileMapWidth()) / map.getTileWidth());
            y = (rand.nextInt(map.getTileHeight()*map.getTileMapHeight()) / map.getTileHeight());

        } while (!mapMapper.get(map.getEntity(x,y)).walkable);

        return new Vector2(x*map.getTileWidth(),y*map.getTileHeight());
    }


}