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
    public HeroEntity[] entities = new HeroEntity[5];
    public float movementPoints;
    public Sprite avatar;
    public String name;

    public final HeroPath heroPath = new HeroPath();

    public HeroDataComponent(int id, String name, float movementPoints, Sprite avatar) {
        this.id = id;
        this.movementPoints = movementPoints;
        this.avatar = avatar;
        this.name = name;
    }

    @Override
    public void reset() {
        id = -1;
        for(int i = 0; i < 5; i++) {
            entities[i] = null;
        }
        movementPoints = 0;
        avatar = null;
        name = null;
        heroPath.reset(true);
    }

    private class HeroEntity {
        int quantity;
        String name;
    }
}
