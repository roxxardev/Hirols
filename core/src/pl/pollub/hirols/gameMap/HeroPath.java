package pl.pollub.hirols.gameMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;

import pl.pollub.hirols.pathfinding.NodePath;

/**
 * Created by erykp_000 on 2016-08-25.
 */
public class HeroPath {
    private NodePositionAndCostList walk = new NodePositionAndCostList();
    private NodePositionAndCostList stand = new NodePositionAndCostList();
    private final Vector2 targetPosition = new Vector2();
    private Entity targetEntity = null;
    private final NodePath path = new NodePath();

    public boolean hasWalkNodes() {
        return !walk.isEmpty();
    }

    public boolean hasStandNodes() {
        return !stand.isEmpty();
    }

    public boolean followPath() {
        if(!hasWalkNodes()) {
            if(hasStandNodes()) {
                NodePositionAndCostList temp = walk;
                walk = stand;
                stand = temp;
                stand.clear();
                return true;
            }
        }
        return false;
    }

    public boolean stopFollowing(boolean immediately) {
        if(!hasWalkNodes()) return false;
        if(immediately) {
            NodePositionAndCostList temp = stand;
            stand = walk;
            walk = temp;
            walk.clear();
            return true;
        } else if(walk.size() > 1) {
            NodePositionAndCostList temp = stand;
            stand = walk;
            walk = temp;
            walk.clear();
            walk.addElement(stand.getFirstElement());
            stand.removeFirstElement();
            return true;
        }
        return false;
    }

    public void reset(boolean resetNodePath) {
        walk.clear();
        stand.clear();
        resetTargetPosition();
        setTargetEntity(null);
        if(resetNodePath) path.clear();
    }

    public void resetTargetPosition() {
        targetPosition.set(0,0);
    }

    public int getPathSize() {return path.getCount();}

    public NodePositionAndCostList getWalk() {
        return walk;
    }

    public NodePositionAndCostList getStand() {
        return stand;
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

    public class NodePositionAndCostList {
        ArrayList<Vector3> array = new ArrayList<Vector3>();

        public Vector3 getFirstElement() {
            return array.get(0);
        }

        public void removeFirstElement() {
           Pools.free(array.remove(0));
        }

        public void removeLastElement() {
            Pools.free(array.remove(array.size() - 1));
        }

        public void addElement(float positionX, float positionY, float cost) {
            Vector3 vector3 = Pools.obtain(Vector3.class).set(positionX,positionY,cost);
            array.add(vector3);
        }

        public void addElement(Vector3 element) {
            addElement(element.x,element.y,element.z);
        }

        public boolean isEmpty() {
            return array.isEmpty();
        }

        public void clear() {
            array.clear();
        }

        public int size() { return array.size();}
    }
}
