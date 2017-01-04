package pl.pollub.hirols.gui.town;

/**
 * Created by erykp_000 on 2017-01-04.
 */
public class Walls extends TownBuilding {

    public Walls() {
        super("Mury");
    }

    @Override
    public boolean upgrade() {
        return false;
    }
}
