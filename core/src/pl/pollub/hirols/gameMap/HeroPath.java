package pl.pollub.hirols.gameMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import pl.pollub.hirols.pathfinding.NodePath;

/**
 * Created by erykp_000 on 2016-08-25.
 */
public class HeroPath {
    private ArrayList<Vector3> walkNodesPosition = new ArrayList<Vector3>();
    private ArrayList<Vector3> standNodesPosition = new ArrayList<Vector3>();
    private Vector2 targetPosition = new Vector2();
    private Entity targetEntity = null;
    private NodePath path = new NodePath();

    public boolean hasWalkNodes() {
        return !walkNodesPosition.isEmpty();
    }

    public boolean hasStandNodes() {
        return !standNodesPosition.isEmpty();
    }

    public boolean followPath() {
        if(!hasWalkNodes()) {
            if(hasStandNodes()) {
                walkNodesPosition = standNodesPosition;
                standNodesPosition = new ArrayList<Vector3>();
                return true;
            }
        }
        return false;
    }

    public boolean stopFollowing(boolean immediately) {
        if(!hasWalkNodes()) return false;
        if(immediately) {
            standNodesPosition = walkNodesPosition;
            walkNodesPosition = new ArrayList<Vector3>();
            return true;
        } else if(walkNodesPosition.size() > 1) {
            standNodesPosition = walkNodesPosition;
            walkNodesPosition = new ArrayList<Vector3>();
            walkNodesPosition.add(standNodesPosition.get(0));
            standNodesPosition.remove(0);
            return true;
        }
        return false;
    }

    public int getPathSize() {return path.getCount();}

    public ArrayList<Vector3> getWalkNodesPosition() {
        return walkNodesPosition;
    }

    public ArrayList<Vector3> getStandNodesPosition() {
        return standNodesPosition;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public NodePath getPath() {
        return path;
    }

    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }
}
