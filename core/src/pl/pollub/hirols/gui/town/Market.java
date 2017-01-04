package pl.pollub.hirols.gui.town;

/**
 * Created by erykp_000 on 2017-01-04.
 */
public class Market extends TownBuilding {
    public Market() {
        super("Market");
    }

    @Override
    public boolean upgrade() {
        return false;
    }
}
