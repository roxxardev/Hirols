package pl.pollub.hirols.console;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by erykp_000 on 2016-07-15.
 */
public class LogLevel {

    private Color color;
    private String specialCharacters;

    public LogLevel(Color color, String specialCharacter) {
        this.color = color;
        this.specialCharacters = specialCharacter;
    }

    public Color getColor() {
        return color;
    }

    public String getSpecialCharacters() {
        return specialCharacters;
    }
}
