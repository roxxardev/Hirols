package pl.pollub.hirols.systems.generalSystems;

import com.badlogic.ashley.core.Component;

import pl.pollub.hirols.managers.input.InputManager;

/**
 * Created by Eryk on 2016-06-17.
 */
public class InputManagerUpdateSystem extends GeneralEntitySystem {

    private InputManager inputManager;

    public InputManagerUpdateSystem(int priority, Class<? extends Component> affiliationClass, InputManager inputManager) {
        super(priority, affiliationClass);
        this.inputManager = inputManager;
    }

    @Override
    public void update(float deltaTime) {
        inputManager.update();
    }
}
