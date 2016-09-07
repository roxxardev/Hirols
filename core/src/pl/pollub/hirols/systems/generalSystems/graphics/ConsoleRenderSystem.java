package pl.pollub.hirols.systems.generalSystems.graphics;

import com.badlogic.ashley.core.Component;

import pl.pollub.hirols.console.Console;
import pl.pollub.hirols.systems.generalSystems.GeneralEntitySystem;

/**
 * Created by erykp_000 on 2016-08-25.
 */
public class ConsoleRenderSystem extends GeneralEntitySystem {

    private Console console;

    public ConsoleRenderSystem(int priority, Class<? extends Component> affiliationClass, Console console) {
        super(priority, affiliationClass);
        this.console = console;
    }

    @Override
    public void update(float deltaTime) {
        console.draw();
    }
}
