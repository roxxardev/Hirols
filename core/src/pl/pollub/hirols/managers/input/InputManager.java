package pl.pollub.hirols.managers.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class InputManager {

    Map<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
    Map<Integer, Boolean> previousKeyMap = new HashMap<Integer, Boolean>();

    Character lastChar;

    Vector2 mouseCoords = new Vector2();
    Vector2 mouseCoordsYUP = new Vector2();

    boolean pan = false;
    int scrolledAmount = 0;

    boolean zoom = false;
    float originalDistance;
    float currentDistance;

    boolean unreadTouchForPath = false;

    boolean unreadTap = false;

    boolean unreadLongPress = false;

    boolean touchDownForLongPress = false;

    Vector2 lastTouch = new Vector2();
    Vector2 draggedDelta = new Vector2();

    boolean draggedForPath = false;

    Vector2 panDelta = new Vector2();

    public void update() {
        for(Map.Entry<Integer, Boolean> it: keyMap.entrySet()) {
            previousKeyMap.put(it.getKey(), it.getValue());
        }
    }

    public void pressKey(int keyID) {
        keyMap.put(keyID, true);
    }

    public void releaseKey(int keyID) {
        keyMap.put(keyID, false);
    }

    void setMouseCoords(float x, float y) {
        mouseCoords.x = x;
        mouseCoords.y = y;
    }

    public boolean isKeyDown(int keyID) {
        Object it = keyMap.get(keyID);
        if(it != null) {
            return Boolean.valueOf(it.toString());
        } else return false;
    }

    public boolean isKeyPressed(int keyID) {
        return isKeyDown(keyID) && !wasKeyDown(keyID);
    }

    private boolean wasKeyDown(int keyID) {
        Object it = previousKeyMap.get(keyID);

        if(it != null) {
            return Boolean.valueOf(it.toString());
        } else return false;
    }

    public Vector2 getMouseCoords() {
        return mouseCoords;
    }

    public Vector2 getMouseCoordsYAxisUp() {
        return mouseCoordsYUP.set(mouseCoords.x, Gdx.graphics.getHeight() - mouseCoords.y);
    }

    public int getPressedKey() {
        for(Map.Entry<Integer, Boolean> it: keyMap.entrySet()) {
            if(it.getValue()) {
                return it.getKey();
            }
        }
        return -1;
    }

    public boolean getZoom() {
        boolean temp = zoom;
        zoom = false;
        return temp;
    }

    public int getScrolledAmount() {
        int temp = scrolledAmount;
        scrolledAmount = 0;
        return temp;
    }

    public boolean getUnreadTap() {
        if(unreadTap){
            unreadTap=false;
            return true;
        }
        return false;
    }
    public boolean getUnreadLongPress() {
        if (unreadLongPress) {
            unreadLongPress = false;
            return true;
        }
        return false;
    }

    public Vector2 getDraggedDelta() {
        Vector2 temp = draggedDelta.cpy();
        draggedDelta.set(0,0);
        return temp;
    }

    public boolean getTouchDownForLongPress() {
        return touchDownForLongPress;
    }

    public boolean isPan() {
        return pan;
    }

    public Vector2 getPanDelta(Vector2 pan) {
        pan.set(panDelta);
        panDelta.set(0,0);
        return pan;
    }

    public float getOriginalDistance() {
        return originalDistance;
    }

    public float getCurrentDistance() {
        return currentDistance;
    }
}