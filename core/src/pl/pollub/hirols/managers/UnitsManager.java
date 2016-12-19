package pl.pollub.hirols.managers;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;
import pl.pollub.hirols.managers.enums.Race;
import pl.pollub.hirols.managers.enums.ResourceType;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public class UnitsManager {

    public final Unit swordBearer, knight;
    public final Unit smallWyvern, wyvern;

    public final Unit snake;

    public final Hero heroOrcWarrior, heroOrcMage;

    public UnitsManager() {
        Direction[] directions = new Direction[] {Direction.E, Direction.N, Direction.NE, Direction.NW, Direction.S, Direction.SE, Direction.SW, Direction.W};
        AnimationManager.AnimationInformation animationInformation = new AnimationManager.AnimationInformation(0,0,0,0);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "dfs", 1,1,1f));

        swordBearer = new Unit(animationInformation,Race.HUMAN, UnitType.WALK, "Miecznik", new UnitCost().addCost(ResourceType.GOLD, 150), 4, 5, 13, new Vector2(2,4), 6, 15);
        knight = new Unit(animationInformation,Race.HUMAN, UnitType.WALK, "Rycerz", new UnitCost().addCost(ResourceType.GOLD, 150).addCost(ResourceType.METAL, 1), 4, 7, 15, new Vector2(5,7), 4, 12);

        snake = new Unit(animationInformation,Race.NONE, UnitType.WALK, "Wonsz", new UnitCost(), 10,10,20,new Vector2(11,23), 6, 2);

        animationInformation = new AnimationManager.AnimationInformation(176,176, -40, -40);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Walking.png", 16, 8, 0.06f));
        smallWyvern = new Unit(animationInformation, Race.ORC, UnitType.FLY, "Wyvern", new UnitCost().addCost(ResourceType.GOLD, 1000).addCost(ResourceType.STONE, 1), 25, 13, 30, new Vector2(13,16), 5, 2);

        animationInformation = new AnimationManager.AnimationInformation(256,256, -80, -70);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Walking.png", 16, 8, 0.06f));
        wyvern = new Unit(animationInformation, Race.ORC, UnitType.FLY, "Wyvern", new UnitCost().addCost(ResourceType.GOLD, 1250).addCost(ResourceType.STONE, 1).addCost(ResourceType.METAL, 2), 32, 15, 40, new Vector2(18,21), 6, 3);


        animationInformation = new AnimationManager.AnimationInformation(128,128,-16,-14);
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcHero_Walking.png",16, 8, 0.05f));
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcHero_Standing.png",16, 8, 0.08f));
        heroOrcWarrior = new Hero(animationInformation, "temp/orki.png", 20, "Sulmuk", 4,1);

        animationInformation = new AnimationManager.AnimationInformation(128,128,-16,-14);
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcMageHero_Walking.png",16, 8, 0.05f));
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcMageHero_Standing.png",16, 8, 0.08f));
        heroOrcMage = new Hero(animationInformation, "temp/orki.png", 60, "Nastria", 2,1);
    }

    public class Unit {
        public final AnimationManager.AnimationInformation animationInformation;

        public final Race race;
        public final UnitType unitType;
        public final String name;
        public final UnitCost cost;
        public final int attack;
        public final int defense;
        public final int health;
        public final Vector2 damage;
        public final int speed;
        public final int growth;

        public Unit(AnimationManager.AnimationInformation animationInformation, Race race, UnitType unitType, String name, UnitCost cost, int attack, int defense, int health, Vector2 damage, int speed, int growth) {
            this.animationInformation = animationInformation;
            this.race = race;
            this.unitType = unitType;
            this.name = name;
            this.cost = cost;
            this.attack = attack;
            this.defense = defense;
            this.health = health;
            this.damage = damage;
            this.speed = speed;
            this.growth = growth;
        }
    }

    public class Hero {
        public final AnimationManager.AnimationInformation animationInformation;
        public final String avatarPath;

        public final int magicPoints;
        public final String name;
        public final int attack;
        public final int defense;

        public Hero(AnimationManager.AnimationInformation animationInformation, String avatarPath, int magicPoints, String name, int attack, int defense) {
            this.animationInformation = animationInformation;
            this.avatarPath = avatarPath;
            this.magicPoints = magicPoints;
            this.name = name;
            this.attack = attack;
            this.defense = defense;
        }
    }

    public class UnitCost {
        private Map<ResourceType, Integer> costMap = new HashMap<ResourceType, Integer>();

        UnitCost addCost(ResourceType resourceType, int quantity) {
            costMap.put(resourceType,quantity);
            return this;
        }
    }

    public enum UnitType {
        WALK, RANGE, FLY;
    }


}

