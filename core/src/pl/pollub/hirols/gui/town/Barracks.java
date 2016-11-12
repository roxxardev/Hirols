package pl.pollub.hirols.gui.town;

/**
 * Created by erykp_000 on 2016-11-12.
 */

public class Barracks extends TownBuilding {

    public Barracks() {
        super("Barracks");
    }

    @Override
    public boolean upgrade() {
        return false;
    }
}
