package pl.pollub.hirols.systems.generalSystems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * Created by Eryk on 2016-05-02.
 */
public abstract class GeneralIteratingSystem extends IteratingSystem {

    protected final Class<? extends Component> affiliationClass;

    public GeneralIteratingSystem(Family family, int priority, Class<? extends Component> affiliationClass) {
        super(family, priority);
        this.affiliationClass = affiliationClass;
    }
}
