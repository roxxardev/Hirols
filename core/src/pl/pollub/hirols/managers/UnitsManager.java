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
    public final Unit orc, orcWarior;
    public final Unit griffin, griffinRider;
    public final Unit smallWyvern, wyvern;
    public final Unit archer, shooter;
    public final Unit shaman, oldShaman;

    public final Hero heroOrcWarrior, heroOrcMage, tempWyvernOrcHero;
    public final Hero heroHumanWarrior, heroHumanMage;

    public UnitsManager() {
        Direction[] directions = new Direction[] {Direction.E, Direction.N, Direction.NE, Direction.NW, Direction.S, Direction.SE, Direction.SW, Direction.W};
        AnimationManager.AnimationInformation animationInformation = new AnimationManager.AnimationInformation(0,0,0,0);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "dfs", 1,1,1f));

        swordBearer = new Unit(animationInformation,Race.HUMAN, UnitType.WALK, "Miecznik", new UnitCost().addCost(ResourceType.GOLD, 150), 4, 5, 13, new Vector2(2,4), 6, 15);
        knight = new Unit(animationInformation,Race.HUMAN, UnitType.WALK, "Rycerz", new UnitCost().addCost(ResourceType.GOLD, 150).addCost(ResourceType.METAL, 1), 4, 7, 15, new Vector2(5,7), 4, 12);

        animationInformation = new AnimationManager.AnimationInformation(180, 180, -42, -42);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/Orc_Staying.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/Orc_Walking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.ATTACK, new AnimationManager.AnimationProperties(directions, "animations/Orc_Attacking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.DIE, new AnimationManager.AnimationProperties(directions, "animations/Orc_Dying.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.TAKING_DAMAGE, new AnimationManager.AnimationProperties(directions, "animations/Orc_TakingDamage.png", 11, 8, 0.06f));
        orc = new Unit(animationInformation, Race.ORC, UnitType.WALK, "Ork", new UnitCost().addCost(ResourceType.GOLD, 250), 6, 6, 17, new Vector2(5,7), 5, 7);

        animationInformation = new AnimationManager.AnimationInformation(180, 180, -42, -42);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcWarrior_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcWarrior_Walking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.ATTACK, new AnimationManager.AnimationProperties(directions, "animations/OrcWarrior_Attacking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.DIE, new AnimationManager.AnimationProperties(directions, "animations/OrcWarrior_Dying.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.TAKING_DAMAGE, new AnimationManager.AnimationProperties(directions, "animations/OrcWarrior_TakingDamage.png", 11, 8, 0.06f));
        orcWarior = new Unit(animationInformation, Race.ORC, UnitType.WALK, "Ork wojownik", new UnitCost().addCost(ResourceType.GOLD, 250).addCost(ResourceType.METAL, 1), 8, 7, 20, new Vector2(8,10), 5, 5);

        griffin = new Unit(animationInformation, Race.HUMAN, UnitType.FLY, "Gryf", new UnitCost().addCost(ResourceType.GOLD, 600).addCost(ResourceType.STONE, 2), 20, 10, 25, new Vector2(10,12), 7, 5);
        griffinRider = new Unit(animationInformation, Race.HUMAN, UnitType.FLY, "Gryf z jeźdźcem", new UnitCost().addCost(ResourceType.GOLD, 750).addCost(ResourceType.STONE, 2).addCost(ResourceType.METAL, 1), 24, 13, 30, new Vector2(14,16), 6, 5);

        animationInformation = new AnimationManager.AnimationInformation(176,176, -40, -40);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Walking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.ATTACK, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Attacking.png", 11, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.DIE, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Dying.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.TAKING_DAMAGE, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_TakingDamage.png", 11, 8, 0.06f));
        smallWyvern = new Unit(animationInformation, Race.ORC, UnitType.FLY, "Wiwerna mała", new UnitCost().addCost(ResourceType.GOLD, 1000).addCost(ResourceType.STONE, 1), 25, 13, 30, new Vector2(13,16), 5, 2);

        animationInformation = new AnimationManager.AnimationInformation(256,256, -80, -80);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcRider_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcRider_Walking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.ATTACK, new AnimationManager.AnimationProperties(directions, "animations/OrcRider_Attacking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.DIE, new AnimationManager.AnimationProperties(directions, "animations/OrcRider_Dying.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.TAKING_DAMAGE, new AnimationManager.AnimationProperties(directions, "animations/OrcRider_TakingDamage.png", 16, 8, 0.06f));
        wyvern = new Unit(animationInformation, Race.ORC, UnitType.FLY, "Wiwerna", new UnitCost().addCost(ResourceType.GOLD, 1250).addCost(ResourceType.STONE, 1).addCost(ResourceType.METAL, 2), 32, 15, 40, new Vector2(18,21), 6, 3);

        archer = new Unit(animationInformation, Race.HUMAN, UnitType.RANGE, "Lucznik", new UnitCost().addCost(ResourceType.GOLD, 300).addCost(ResourceType.WOOD, 1), 10, 3, 10, new Vector2(6,9), 5, 7);
        shooter = new Unit(animationInformation, Race.HUMAN, UnitType.RANGE, "Strzelec", new UnitCost().addCost(ResourceType.GOLD, 500).addCost(ResourceType.WOOD, 1).addCost(ResourceType.METAL, 1), 13, 5, 15, new Vector2(11,13), 6, 8);

        animationInformation = new AnimationManager.AnimationInformation(180, 180, -42, -42);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcSlinger_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcSlinger_Walking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.ATTACK, new AnimationManager.AnimationProperties(directions, "animations/OrcSlinger_Attacking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.DIE, new AnimationManager.AnimationProperties(directions, "animations/OrcSlinger_Dying.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.TAKING_DAMAGE, new AnimationManager.AnimationProperties(directions, "animations/OrcSlinger_TakingDamage.png", 11, 8, 0.06f));
        shaman = new Unit(animationInformation, Race.ORC, UnitType.RANGE, "Szaman", new UnitCost().addCost(ResourceType.GOLD, 600).addCost(ResourceType.WOOD, 1).addCost(ResourceType.STONE, 1).addCost(ResourceType.METAL, 1), 17, 6, 18, new Vector2(13,15), 4, 5);

        animationInformation = new AnimationManager.AnimationInformation(128, 128, -16, -16);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcCrossbow_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcCrossbow_Walking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.ATTACK, new AnimationManager.AnimationProperties(directions, "animations/OrcCrossbow_Attacking.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.DIE, new AnimationManager.AnimationProperties(directions, "animations/OrcCrossbow_Dying.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.TAKING_DAMAGE, new AnimationManager.AnimationProperties(directions, "animations/OrcCrossbow_TakingDamage.png", 11, 8, 0.06f));
        oldShaman = new Unit(animationInformation, Race.ORC, UnitType.RANGE, "Starszy szaman", new UnitCost().addCost(ResourceType.GOLD, 700).addCost(ResourceType.STONE, 1).addCost(ResourceType.METAL, 1), 21, 10, 22, new Vector2(17,20), 4, 6);




        animationInformation = new AnimationManager.AnimationInformation(128,128,-16,-14);
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcHero_Walking.png",16, 8, 0.05f));
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcHero_Standing.png",16, 8, 0.08f));
        heroOrcWarrior = new Hero(animationInformation, "temp/orki.png", 20, "Sulmuk", 4,1);

        animationInformation = new AnimationManager.AnimationInformation(128,128,-16,-14);
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcMageHero_Walking.png",16, 8, 0.05f));
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcMageHero_Standing.png",16, 8, 0.08f));
        heroOrcMage = new Hero(animationInformation, "temp/orki.png", 60, "Nastria", 2,1);

        animationInformation = new AnimationManager.AnimationInformation(256,256, -80, -70);
        animationInformation.animationPropertiesMap.put(AnimationType.STAND, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.RUN, new AnimationManager.AnimationProperties(directions, "animations/OrcWyvern_Walking.png", 16, 8, 0.06f));
        tempWyvernOrcHero = new Hero(animationInformation, "portrait.png", 10000, "WONSZ", 10,10);

        heroHumanWarrior = new Hero(animationInformation, "portrait.png", 20, "Sargor", 3, 2);
        heroHumanMage = new Hero(animationInformation, "portrait.png", 50, "Ameris", 2, 2);
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

