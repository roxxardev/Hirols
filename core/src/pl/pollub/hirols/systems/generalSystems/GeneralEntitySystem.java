package pl.pollub.hirols.systems.generalSystems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Created by Eryk on 2016-05-02.
 */
public abstract class GeneralEntitySystem extends EntitySystem {

    protected final Class<? extends Component> affiliationClass;

    public GeneralEntitySystem(int priority, Class<? extends Component> affiliationClass) {
        super(priority);
        this.affiliationClass = affiliationClass;
    }
}
