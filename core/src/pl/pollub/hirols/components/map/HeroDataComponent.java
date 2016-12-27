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

    public float additionalMovementPoints;
    public float movementPoints;
    public int magicPoints;
    public String name;
    public int attack;
    public int defense;

    public HeroLevel heroLevel = new HeroLevel();

    public HeroDataComponent init(int id, String name, float additionalMovementPoints, UnitsManager.Hero hero, UnitsManager.Unit startUnit) {
        this.id = id;
        this.additionalMovementPoints = additionalMovementPoints;
        this.movementPoints = additionalMovementPoints;
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
                    updateHeroMovementPoints();
                    return true;
                }
            }
            return false;
        }

        public boolean removeUnit(UnitsManager.Unit unitToRemove, int quantity) {
            if(quantity < 1) return false;
            for(int i = 0; i < capacity; i++) {
                if(squads[i] != null && squads[i].unit.equals(unitToRemove)) {
                    squads[i].quantity -= quantity;
                    if(quantity <= 0) {
                        squads[i] = null;
                        updateHeroMovementPoints();
                    }
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

        private void updateHeroMovementPoints() {
            int maxSpeed = 0;
            for(Squad squad : squads) {
                if(squad != null && squad.getUnit().speed > maxSpeed) maxSpeed = squad.getUnit().speed;
            }
            float movementPointsFromSpeed = 0;
            if(maxSpeed <= 3) {
                movementPointsFromSpeed = 10;
            } else if(maxSpeed == 4) {
                movementPointsFromSpeed = 12;
            } else if(maxSpeed == 5) {
                movementPointsFromSpeed = 14;
            } else if(maxSpeed == 6) {
                movementPointsFromSpeed = 16;
            } else if(maxSpeed >= 7) {
                movementPointsFromSpeed = 18;
            }
            movementPoints = additionalMovementPoints + movementPointsFromSpeed;
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
        private int experience = 1000;
        private int level = 1;

        public void addExperience(int amount) {
            experience += Math.abs(amount);
            double lvl = Math.log(experience/1000d)/Math.log(2.5d) + 1;
            level = (lvl >= 1) ? (int)Math.floor(lvl) : 1;
        }

        public void clear(){
            experience = 1000;
            level = 1;
        }

        public int getExperience() {
            return experience;
        }

        public int getLevel() {
            return level;
        }
    }
}
