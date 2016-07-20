package pl.pollub.hirols.console;

/**
 * Created by erykp_000 on 2016-07-16.
 */
public class TestConsole extends DefaultConsole {

    public TestConsole(CommandsContainer commandsContainer) {
        super(commandsContainer);
        this.commands = commandsContainer;
    }

    @Override
    public void draw() {
        System.out.println(logger.getLogsInOneString());
    }

    @Override
    public void clear() {
        System.out.println("CLEARRRRRRR\n");
    }

    @Override
    public void dispose() {
        System.out.println("dispose\n");
    }

    public String getLogs() {
        return logger.getLogsInOneString();
    }
}
