package pl.pollub.hirols.gui.town;

/**
 * Created by erykp_000 on 2017-01-04.
 */
public class TownHall extends TownBuilding{
    public TownHall() {
        super("Ratusz");

    }

    @Override
    public boolean upgrade() {
        return false;
    }
}
