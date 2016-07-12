package pl.pollub.hirols.systems.generalSystems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;

import java.util.Comparator;

/**
 * Created by Eryk on 2016-05-02.
 */
public abstract class GeneralSortedIteratingSystem extends SortedIteratingSystem{

    protected final Class<? extends Component> affiliationClass;

    public GeneralSortedIteratingSystem(Family family, Comparator<Entity> comparator, int priority, Class<? extends Component> affiliationClass) {
        super(family, comparator, priority);
        this.affiliationClass = affiliationClass;
    }
}
