package pl.pollub.hirols.console;

/**
 * Created by erykp_000 on 2016-07-15.
 */
public interface Console {

    void draw();

    void executeCommand(String command);

    void clear();

    void log(String message, LogLevel logLevel);

    void log(String message);

    void showCommands();

    void saveLogsToFile(String file);

    void setCommands(CommandsContainer commands);

    void setHiddenCommandsExecute(boolean hiddenCommandsExecute);

    void setHiddenCommandsVisibility(boolean hiddenCommandsVisibility);

    boolean isHiddenCommandsExecute();

    boolean isHiddenCommandsVisible();

    void dispose();

}
