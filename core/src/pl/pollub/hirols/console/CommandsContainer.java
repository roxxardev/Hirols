package pl.pollub.hirols.console;

/**
 * Created by erykp_000 on 2016-07-15.
 */
public abstract class CommandsContainer {
    protected Console console;

    public abstract void exit();

    public abstract void showCommands();

    public abstract void help();

    public abstract void clear();

    public void setConsole(Console console) { this.console = console; }


}
