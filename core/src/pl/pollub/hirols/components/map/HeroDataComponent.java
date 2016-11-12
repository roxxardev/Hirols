package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

import pl.pollub.hirols.gameMap.HeroPath;
import pl.pollub.hirols.managers.UnitsManager;
import pl.pollub.hirols.pathfinding.NodePath;

/**
 * Created by Eryk on 2015-12-02.
 */
public class HeroDataComponent implements Component, Pool.Poolable{
    public int id = -1;
    public Sprite avatar;
    public final HeroPath heroPath = new HeroPath();
    public final ArrayList<Entity> pathEntities = new ArrayList<Entity>();
    public final Army army = new Army();

    public float movementPoints;
    public int magicPoints;
    public String name;
    public int attack;
    public int defense;

    public HeroDataComponent init(int id, String name, float movementPoints, Sprite avatar) {
        this.id = id;
        this.movementPoints = movementPoints;
        this.avatar = avatar;
        this.name = name;
        return this;
    }

    @Override
    public void reset() {
        id = -1;
        movementPoints = 0;
        avatar = null;
        name = null;
        heroPath.reset(true);
        pathEntities.clear();
        magicPoints = 0;
        attack = 0;
        defense = 0;
        army.clear();
    }

    public class Army {
        int capacity = 5;
        Squad[] squads = new Squad[capacity];

        public boolean addUnit(UnitsManager.Unit unitToInsert, int quantity) {
            for(int i = 0; i < capacity; i++) {
                if(squads[i].unit.equals(unitToInsert)) {
                    squads[i].quantity += quantity;
                    return true;
                }
            }

            for(int i = 0; i < capacity; i++) {
                if(squads[i] == null) {
                    squads[i] = new Squad(unitToInsert, quantity);
                }
            }

            return false;
        }

        public void clear() {
            for(Squad squad : squads) {
                squad = null;
            }
            capacity = 0;
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
}
