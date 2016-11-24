package pl.pollub.hirols.managers;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.managers.enums.AnimationType;
import pl.pollub.hirols.managers.enums.Direction;
import pl.pollub.hirols.managers.enums.Race;
import pl.pollub.hirols.managers.enums.Resource;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public class UnitsManager {

    public final Unit swordBearer, knight, wyvern;
    public final Unit snake;

    public UnitsManager() {
        Direction[] directions = new Direction[] {Direction.E, Direction.N, Direction.NE, Direction.NW, Direction.S, Direction.SE, Direction.SW, Direction.W};
        AnimationInformation animationInformation = new AnimationInformation(0,0,0,0);
        animationInformation.animationPropertiesMap.put(AnimationType.stand, new AnimationProperties(directions, "dfs", 1,1,1f));

        swordBearer = new Unit(animationInformation,Race.HUMAN, UnitType.WALK, "Miecznik", new UnitCost().addCost(Resource.GOLD, 150), 4, 5, 13, new Vector2(2,4), 6, 15);
        knight = new Unit(animationInformation,Race.HUMAN, UnitType.WALK, "Rycerz", new UnitCost().addCost(Resource.GOLD, 150).addCost(Resource.STEEL, 1), 4, 7, 15, new Vector2(5,7), 4, 12);

        snake = new Unit(animationInformation,Race.NONE, UnitType.WALK, "Wonsz", new UnitCost(), 10,10,20,new Vector2(11,23), 6, 2);

        animationInformation = new AnimationInformation(128,128, -16, -14);
        animationInformation.animationPropertiesMap.put(AnimationType.stand, new AnimationProperties(directions, "animations/OrcWyvern_Standing.png", 16, 8, 0.06f));
        animationInformation.animationPropertiesMap.put(AnimationType.run, new AnimationProperties(directions, "animations/OrcWyvern_Walking.png", 16, 8, 0.06f));
        wyvern = new Unit(animationInformation, Race.ORC, UnitType.FLY, "Wyvern", new UnitCost().addCost(Resource.GOLD, 1000).addCost(Resource.STONE, 1), 25, 13, 30, new Vector2(13,16), 5, 2);
    }

    public class Unit {
        public final AnimationInformation animationInformation;
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

        public Unit(AnimationInformation animationInformation,Race race, UnitType unitType, String name, UnitCost cost, int attack, int defense, int health, Vector2 damage, int speed, int growth) {
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

    public class UnitCost {
        private Map<Resource, Integer> costMap = new HashMap<Resource, Integer>();

        UnitCost addCost(Resource resource, int quantity) {
            costMap.put(resource,quantity);
            return this;
        }
    }

    public enum UnitType {
        WALK, RANGE, FLY;
    }

    public class AnimationInformation {

        public final Map<AnimationType, AnimationProperties> animationPropertiesMap = new HashMap<AnimationType, AnimationProperties>();
        public final Vector2 offset = new Vector2();
        public final Vector2 size = new Vector2();

        public AnimationInformation(int sizeX, int sizeY, int offsetX, int offsetY) {
            offset.set(offsetX, offsetY);
            size.set(sizeX, sizeY);
        }
    }

    public class AnimationProperties {
        private String path;
        private Direction[] directions;
        private int cols, rows;
        private float time;

        public AnimationProperties(Direction[] directions, String path, int cols, int rows, float time) {
            this.directions = directions;
            this.path = path;
            this.cols = cols;
            this.rows = rows;
            this.time = time;
        }

        public String getPath() {
            return path;
        }

        public Direction[] getDirections() {
            return directions;
        }

        public int getCols() {
            return cols;
        }

        public int getRows() {
            return rows;
        }

        public float getTime() {
            return time;
        }
    }
}

