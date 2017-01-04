package pl.pollub.hirols.gui.town;

/**
 * Created by erykp_000 on 2017-01-04.
 */
public class Library extends TownBuilding {

    public Library() {
        super("Biblioteka");
    }

    @Override
    public boolean upgrade() {
        return false;
    }
}
