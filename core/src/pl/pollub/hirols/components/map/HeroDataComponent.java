package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

import pl.pollub.hirols.gameMap.HeroPath;
import pl.pollub.hirols.managers.UnitsManager;

/**
 * Created by Eryk on 2015-12-02.
 */
public class HeroDataComponent implements Component, Pool.Poolable{

    public UnitsManager.Hero hero;
    public int id = -1;
    public final HeroPath heroPath = new HeroPath();
    public final ArrayList<Entity> pathEntities = new ArrayList<Entity>();
    public final Army army = new Army();

    public float movementPoints;
    public int magicPoints;
    public String name;
    public int attack;
    public int defense;

    public HeroLevel heroLevel = new HeroLevel();

    public HeroDataComponent init(int id, String name, float movementPoints, UnitsManager.Hero hero, UnitsManager.Unit startUnit) {
        this.id = id;
        this.movementPoints = movementPoints;
        this.name = name;
        this.hero = hero;
        magicPoints = hero.magicPoints;
        attack = hero.attack;
        defense = hero.defense;
        army.addUnit(startUnit, 1);
        return this;
    }

    @Override
    public void reset() {
        id = -1;
        movementPoints = 0;
        name = null;
        heroPath.reset(true);
        pathEntities.clear();
        magicPoints = 0;
        attack = 0;
        defense = 0;
        army.clear();
        heroLevel.clear();
    }

    public class Army {
        final int capacity = 5;
        final Squad[] squads = new Squad[capacity];

        public boolean addUnit(UnitsManager.Unit unitToInsert, int quantity) {
            if(quantity < 1) return false;
            for(int i = 0; i < capacity; i++) {
                if(squads[i] != null && squads[i].unit.equals(unitToInsert)) {
                    squads[i].quantity += quantity;
                    return true;
                }
            }

            for(int i = 0; i < capacity; i++) {
                if(squads[i] == null) {
                    squads[i] = new Squad(unitToInsert, quantity);
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            for(int i = 0; i < capacity; i++) {
                squads[i] = null;
            }
        }

        public Squad[] getSquads() {
            return squads;
        }

        public class Squad {
            UnitsManager.Unit unit;
            int quantity;

            Squad(UnitsManager.Unit unit, int quantity) {
                this.unit = unit;
                this.quantity = quantity;
            }

            public int getQuantity() {
                return quantity;
            }

            public UnitsManager.Unit getUnit() {
                return unit;
            }
        }
    }

    public class HeroLevel {
        private int experience;
        private int level;

        public void addExperience(int amount) {

        }

        public void clear(){

        }
    }
}
