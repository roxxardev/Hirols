package pl.pollub.hirols.console;

import com.badlogic.gdx.utils.StringBuilder;

import java.sql.Timestamp;

/**
 * Created by erykp_000 on 2016-07-15.
 */
public class Log {

    private String logText;
    private Timestamp timestamp;
    private LogLevel logLevel;

    public Log(String logText, LogLevel logLevel) {
        this.logText = logText;
        this.logLevel = logLevel;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getLogText() {
        return logText;
    }

    public String getLogTextForConsole() {
        StringBuilder stringBuilder = new StringBuilder();

            stringBuilder
                    .append(logLevel.getSpecialCharacters())
                    .append(" ")
                    .append(" ")
                    .append(logText);

        return stringBuilder.toString();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }
}
