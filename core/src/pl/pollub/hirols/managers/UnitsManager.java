package pl.pollub.hirols.managers;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import pl.pollub.hirols.managers.enums.Race;
import pl.pollub.hirols.managers.enums.Resource;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public class UnitsManager {

    public final Unit swordBearer, knight;
    public final Unit snake;

    public UnitsManager() {
        swordBearer = new Unit(Race.HUMAN, UnitType.WALK, "Miecznik", new UnitCost().addCost(Resource.GOLD, 150), 4, 5, 13, new Vector2(2,4), 6, 15);
        knight = new Unit(Race.HUMAN, UnitType.WALK, "Rycerz", new UnitCost().addCost(Resource.GOLD, 150).addCost(Resource.Steel, 1), 4, 7, 15, new Vector2(5,7), 4, 12);

        snake = new Unit(Race.NONE, UnitType.WALK, "Wonsz", new UnitCost(), 10,10,20,new Vector2(11,23), 6, 2);
    }

    public class Unit {

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

        public Unit(Race race, UnitType unitType, String name, UnitCost cost, int attack, int defense, int health, Vector2 damage, int speed, int growth) {
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
}

