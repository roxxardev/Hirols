package pl.pollub.hirols.managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Pools;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.pollub.hirols.animation.AnimationSet;
import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.LifePeriodComponent;
import pl.pollub.hirols.components.SelectedComponent;
import pl.pollub.hirols.components.player.PlayerComponent;
import pl.pollub.hirols.components.player.PlayerDataComponent;
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

        Map<AnimationType, Map<Direction, Animation>> wyvernAnimationMap = new HashMap<AnimationType, Map<Direction, Animation>>();
        Direction[] snakeAnimationDirections = new Direction[] {Direction.E, Direction.N, Direction.NE, Direction.NW, Direction.S, Direction.SE, Direction.SW, Direction.W};
        wyvernAnimationMap.put(AnimationType.stand, AnimationManager.createAnimation(snakeAnimationDirections,
                game.assetManager.get("animations/OrcWyvern_Standing.png", Texture.class), 16, 8, 0.06f));


        Map<AnimationType, Map<Direction, Animation>> wyvern2AnimationMap = new HashMap<AnimationType, Map<Direction, Animation>>();
        wyvern2AnimationMap.put(AnimationType.stand, AnimationManager.createAnimation(snakeAnimationDirections,
                game.assetManager.get("animations/OrcWyvern_Standing.png", Texture.class), 16, 8, 0.06f));
        wyvern2AnimationMap.put(AnimationType.run, AnimationManager.createAnimation(snakeAnimationDirections,
                game.assetManager.get("animations/OrcWyvern_Walking.png", Texture.class), 16, 8, 0.06f));


        for (int i = 0; i < 5; i++) {
            Vector2 position = Pools.obtain(Vector2.class);
            generateRandomPositionOnMap(position,map);

            int indexX = (int)Math.floor(position.x / map.getTileWidth());
            int indexY = (int)Math.floor(position.y / map.getTileHeight());

            Entity mapEntity = map.getEntity(indexX,indexY);

            if(game.engine.getEntities().contains(mapEntity, true)) continue;

            mapEntity.add(game.engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), wyvernAnimationMap), true, rand.nextFloat()))
                    .add(game.engine.createComponent(map.getGameMapComponentClazz()))
                    .add(game.engine.createComponent(TextureComponent.class).setSize(176, 176).setAdditionalOffset(-40, -40))
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
                .add(game.engine.createComponent(map.getGameMapComponentClazz()))
                .add(game.engine.createComponent(BitmapFontComponent.class).init(game.assetManager.get("testFontSize32.ttf",BitmapFont.class), "teeest efwkjerurhjkjfiuhrejrfjfkejfr4hfruijrigj5ffjr5678909488"))
                .add(game.engine.createComponent(RenderableComponent.class))
                .add(game.engine.createComponent(TransparencyComponent.class).init(1))
                .add(game.engine.createComponent(LifePeriodComponent.class).init(1000));
        game.engine.addEntity(testText);

        spawnPlayerAndHeroes(game, wyvern2AnimationMap, map, game.gameManager.getPlayerClasses().get(0));
    }



    private static void spawnPlayerAndHeroes(Hirols game,Map<AnimationType, Map<Direction, Animation>> wywernMap, pl.pollub.hirols.gameMap.Map map, Class<? extends PlayerComponent> playerClass) {
        int playerId = -1;
        PooledEngine engine = game.engine;
        Entity player = engine.createEntity();
        player
                .add(engine.createComponent(map.getGameMapComponentClazz()))
                .add(engine.createComponent(playerClass))
                .add(engine.createComponent(PlayerDataComponent.class).init(Color.BROWN, "Gracz Michau"))
                .add(engine.createComponent(SelectedComponent.class));
        engine.addEntity(player);

        Direction[] heroAnimationDirections = new Direction[] {Direction.E, Direction.N, Direction.NE, Direction.NW, Direction.S, Direction.SE, Direction.SW, Direction.W};
        Map<AnimationType, Map<Direction, Animation>> orcHeroAnimationMap = new HashMap<AnimationType, Map<Direction, Animation>>();
        orcHeroAnimationMap.put(AnimationType.run, AnimationManager.createAnimation(heroAnimationDirections,
                game.assetManager.get("animations/OrcHero_Walking.png", Texture.class), 16, 8, 0.05f));
        orcHeroAnimationMap.put(AnimationType.stand, AnimationManager.createAnimation(heroAnimationDirections,
                game.assetManager.get("animations/OrcHero_Standing.png", Texture.class), 16, 8, 0.08f));

        Map<AnimationType, Map<Direction, Animation>> orcMageHeroAnimationMap = new HashMap<AnimationType, Map<Direction, Animation>>();
        orcMageHeroAnimationMap.put(AnimationType.run, AnimationManager.createAnimation(heroAnimationDirections,
                game.assetManager.get("animations/OrcMageHero_Walking.png", Texture.class), 16, 8, 0.05f));
        orcMageHeroAnimationMap.put(AnimationType.stand, AnimationManager.createAnimation(heroAnimationDirections,
                game.assetManager.get("animations/OrcMageHero_Standing.png", Texture.class), 16, 8, 0.08f));

        Vector2 firstHeroPos = Pools.obtain(Vector2.class);
        engine.addEntity(engine.createEntity()
                .add(engine.createComponent(PositionComponent.class).init(1728, 1728))
                .add(engine.createComponent(map.getGameMapComponentClazz()))
                .add(engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), orcHeroAnimationMap), true,0f))
                .add(engine.createComponent(TextureComponent.class).setSize(128, 128).setAdditionalOffset(-40, -40))
                .add(engine.createComponent(RenderableComponent.class))
                .add(engine.createComponent(HeroDataComponent.class).init(++playerId, "Sebek", 10.f,new Sprite(orcHeroAnimationMap.get(AnimationType.stand).get(Direction.S).getKeyFrame(0))))
                .add(engine.createComponent(VelocityComponent.class))
                .add(engine.createComponent(playerClass)));
        Pools.free(firstHeroPos);
        for (int i = 0; i < 10; i++) {
            Map<AnimationType, Map<Direction, Animation>> animationMap = orcHeroAnimationMap;
            if(i%2 == 0){
                animationMap = orcMageHeroAnimationMap;
            }
            Vector2 heroPosition = Pools.obtain(Vector2.class);
            Entity hero = engine.createEntity();
            hero
                    .add(engine.createComponent(map.getGameMapComponentClazz()))
                    .add(engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), animationMap), true, 0f))
                    .add(engine.createComponent(PositionComponent.class).init(generateRandomPositionOnMap(heroPosition,map)))
                    .add(engine.createComponent(RenderableComponent.class))
                    .add(engine.createComponent(TextureComponent.class).setSize(128, 128).setAdditionalOffset(-16, 0))
                    .add(engine.createComponent(HeroDataComponent.class).init(++playerId,"nołnejm", 100000f, new Sprite(game.assetManager.get("temp/portrait.png", Texture.class))))
                    .add(engine.createComponent(VelocityComponent.class))
                    .add(engine.createComponent(playerClass));
            engine.addEntity(hero);
            Pools.free(heroPosition);
        }
        for (int i = 0; i < 5; i++) {
            Vector2 heroPosition = Pools.obtain(Vector2.class);
            Entity hero = engine.createEntity();
            hero
                    .add(engine.createComponent(map.getGameMapComponentClazz()))
                    .add(engine.createComponent(AnimationComponent.class).init(new AnimationSet(AnimationType.stand, Direction.getRandomDirection(), wywernMap), true, 0f))
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