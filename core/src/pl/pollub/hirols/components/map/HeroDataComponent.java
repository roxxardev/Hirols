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
import pl.pollub.hirols.pathfinding.NodePath;

/**
 * Created by Eryk on 2015-12-02.
 */
public class HeroDataComponent implements Component, Pool.Poolable{
    public int id = -1;
    public float movementPoints;
    public String name;
    public Sprite avatar;
    public final HeroPath heroPath = new HeroPath();
    public final ArrayList<Entity> pathEntities = new ArrayList<Entity>();

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
    }
}
