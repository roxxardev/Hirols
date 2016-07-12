package pl.pollub.hirols.managers.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eryk on 2016-02-16.
 */
public class MyGestureListener implements GestureDetector.GestureListener {

    private InputManager inputManager;

    public MyGestureListener(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log("INPUT", "Gesture touchDown"+", finger: "+button);
        inputManager.setMouseCoords(x, y);
        inputManager.lastTouch.set(x, y);
        inputManager.touchDownForLongPress = true;
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("INPUT", "Gesture tap, finger: "+button);
        inputManager.unreadTap = true;
        inputManager.touchDownForLongPress = false;
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        Gdx.app.log("INPUT", "Gesture longPress");
        inputManager.unreadLongPress = true;
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log("INPUT", "Gesture fling, finger: "+button);
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Gdx.app.log("INPUT", "Gesture pan");
        inputManager.setMouseCoords(x, y);
        inputManager.pan = true;
        inputManager.panDelta.add(deltaX, deltaY);
        inputManager.touchDownForLongPress = false;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        Gdx.app.log("INPUT", "Gesture panStop, finger: " + button);
        inputManager.setMouseCoords(x, y);
        inputManager.pan = false;
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        Gdx.app.log("INPUT", "zoom");
        inputManager.zoom = true;
        inputManager.touchDownForLongPress = false;
        inputManager.originalDistance = initialDistance;
        inputManager.currentDistance = distance;
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        Gdx.app.log("INPUT", "pinch");
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
