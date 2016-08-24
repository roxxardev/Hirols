package pl.pollub.hirols.components.map;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import pl.pollub.hirols.pathfinding.NodePath;

/**
 * Created by Eryk on 2015-12-02.
 */
public class HeroDataComponent implements Component{
    public int id = -1;
    public HeroEntity[] entities = new HeroEntity[5];
    public float movementPoints;
    public Sprite avatar;
    public String name;

    public ArrayList<Vector3> pathNodesPosition = new ArrayList<Vector3>();
    public ArrayList<Vector3> tempNodesPosition = new ArrayList<Vector3>();
    public Vector2 endPathTargetPosition = null;
    public Entity targetEntity = null;
    public NodePath path = new NodePath();

    public HeroDataComponent(int id, String name, float movementPoints, Sprite avatar) {
        this.id = id;
        this.movementPoints = movementPoints;
        this.avatar = avatar;
        this.name = name;
    }

    private class HeroEntity {
        int quantity;
        String name;
    }
}
