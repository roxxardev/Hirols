package pl.pollub.hirols.systems.gameMapSystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.components.SelectedComponent;
import pl.pollub.hirols.components.graphics.AnimationComponent;
import pl.pollub.hirols.components.map.HeroDataComponent;
import pl.pollub.hirols.components.map.PathComponent;
import pl.pollub.hirols.components.map.maps.GameMapComponent;
import pl.pollub.hirols.managers.enums.AnimationType;

/**
 * Created by erykp_000 on 2016-09-09.
 */
public class HeroAnimationChangerSystem extends GameMapEntitySystem {

    private Hirols game;

    private ComponentMapper<HeroDataComponent> heroDataMap = ComponentMapper.getFor(HeroDataComponent.class);
    private ComponentMapper<PathComponent> pathMap = ComponentMapper.getFor(PathComponent.class);
    private ComponentMapper<AnimationComponent> animationMap = ComponentMapper.getFor(AnimationComponent.class);

    public HeroAnimationChangerSystem(int priority, Class<? extends GameMapComponent> gameMapClass, Hirols game) {
        super(priority, gameMapClass);
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> playerSelectedHeroes = getEngine().getEntitiesFor(Family.all(HeroDataComponent.class, SelectedComponent.class, game.gameManager.getCurrentPlayerClass(), gameMapClass).get());
        if(playerSelectedHeroes.size() < 1) return;
        Entity hero = playerSelectedHeroes.first();
        HeroDataComponent heroData = heroDataMap.get(hero);

        if(heroData.heroPath.hasWalkNodes()) {
            animationMap.get(hero).animationSet.setDirection(pathMap.get(heroData.pathEntities.get(0)).direction);
            animationMap.get(hero).animationSet.changeAnimationType(AnimationType.RUN);
        } else {
            if(heroData.heroPath.hasStandNodes())
                animationMap.get(hero).animationSet.setDirection(pathMap.get(heroData.pathEntities.get(0)).direction);
            animationMap.get(hero).animationSet.changeAnimationType(AnimationType.STAND);
        }
    }
}
