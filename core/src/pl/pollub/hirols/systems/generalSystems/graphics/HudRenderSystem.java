package pl.pollub.hirols.systems.generalSystems.graphics;

import com.badlogic.ashley.core.Component;

import pl.pollub.hirols.gui.Hud;
import pl.pollub.hirols.systems.generalSystems.GeneralEntitySystem;

/**
 * Created by erykp_000 on 2016-08-25.
 */
public abstract class HudRenderSystem extends GeneralEntitySystem {

    private Hud hud;

    public HudRenderSystem(int priority, Class<? extends Component> affiliationClass, Hud hud) {
        super(priority, affiliationClass);
        this.hud = hud;
    }

    @Override
    public void update(float deltaTime) {
        hud.update(deltaTime);
        hud.getStage().draw();
    }
}
