package pl.pollub.hirols.console;

import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erykp_000 on 2016-07-15.
 */
public class Logger {

    private List<Log> logs;

    public Logger() {
        logs = new ArrayList<Log>();
    }

    void log(String message, LogLevel logLevel) {
        logs.add(new Log(message,logLevel));
    }

    public List<Log> getLogs() {
        return logs;
    }

    public String getLogsInOneString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Log log : logs) {
            stringBuilder
                    .append(log.getLogLevel().getSpecialCharacters())
                    .append(" ")
                    //.append(log.getTimestamp())
                    .append(" ")
                    .append(log.getLogText())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

}
