package pl.pollub.hirols.managers.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Eryk on 2015-11-28.
 */
public class MyInputProcessor implements InputProcessor {

    InputManager inputManager;

    public MyInputProcessor(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public boolean keyDown(int keycode) {
        inputManager.pressKey(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        inputManager.releaseKey(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        inputManager.lastChar = character;
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("INPUT", "InputManager touchDown");
//        InputManager.setMouseCoords(screenX, screenY);
//        InputManager.lastTouch.set(screenX,screenY);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("INPUT", "InputProcessor touchUp");
//        if(!InputManager.draggedForPath)
//            InputManager.unreadTouchForPath = true;
//        InputManager.draggedForPath = false;
        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Gdx.app.log("INPUT", "InputProcessor touchDragged");
        //InputManager.dragged = true;
        //InputManager.setMouseCoords(screenX, screenY);
//        Vector2 newTouch = new Vector2(screenX, screenY);
//        Vector2 temp;
//        temp = newTouch.cpy().sub(InputManager.lastTouch);
//        InputManager.draggedDelta.add(temp);
//        if(InputManager.draggedDelta.x + InputManager.draggedDelta.y >1)
//            InputManager.draggedForPath = true;
//        InputManager.lastTouch = newTouch;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        inputManager.setMouseCoords(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        Gdx.app.log("INPUT", "InputProcessor scrolled");
        inputManager.scrolledAmount = amount;
        return true;
    }
}
