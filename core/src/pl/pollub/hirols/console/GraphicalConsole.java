package pl.pollub.hirols.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisImageButton;

import java.util.ArrayList;

import pl.pollub.hirols.Hirols;

/**
 * Created by erykp_000 on 2016-07-17.
 */
public class GraphicalConsole extends DefaultConsole {

    private final Stage stage;
    private final Window window;
    private final Table consoleTable;
    private final ScrollPane scrollPane;
    private final TextField textField;
    private final Table logTable;
    private final ArrayList<Label> textLabels = new ArrayList<Label>();
    private final InputListener stageInputListener;
    private final Skin skin;

    private TextFieldHistory textFieldHistory = new TextFieldHistory();
    private final InputMultiplexer multiplexer;

    private boolean visible = true;
    private int key;

    public GraphicalConsole(CommandsContainer commandsContainer, Skin skin,
                            Hirols game, Viewport viewport) {
        super(commandsContainer);
        this.multiplexer = game.multiplexer;
        this.skin = skin;

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        stage = new Stage(viewport,game.batch);

        consoleTable = new Table(skin);
        consoleTable.setFillParent(true);
        consoleTable.pad(4);
        consoleTable.padTop(22);
        consoleTable.setFillParent(true);

        logTable = new Table(skin);

        textField = new TextField("", skin);
        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(("" + c).equalsIgnoreCase(Input.Keys.toString(Input.Keys.GRAVE))) {
                    String text = textField.getText();
                    textField.setText(text.substring(0,text.length() - 1));
                    textField.setCursorPosition(text.length() - 1);
                }
            }
        });

        scrollPane = new ScrollPane(logTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setOverscroll(false,false);

        consoleTable.add(scrollPane).expand().fill().pad(4).row();
        consoleTable.add(textField).expandX().fillX().pad(4);

        window = new Window("Console Hirols", skin);

        //TODO change temporary exit button
        Table titleTable = window.getTitleTable();
        VisImageButton closeButton = new VisImageButton("close-window");
        titleTable.add(closeButton).padRight(-window.getPadRight() + 0.7f);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setVisible(false);
            }
        });

        window.setKeepWithinStage(true);
        window.setMovable(true);
        window.setResizable(true);
        window.setTouchable(Touchable.enabled);
        setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        setPosition(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);

        window.addActor(consoleTable);

        stage.addActor(window);

        stageInputListener = new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {

                switch(keycode) {
                    case Input.Keys.GRAVE:
                        setVisible(!visible);
                        break;
                    case Input.Keys.ENTER:
                            String text = textField.getText();
                            if(!(text.length() == 0)) {
                                textFieldHistory.add(text);
                                executeCommand(text);
                                textField.setText("");
                            }
                        break;
                    case Input.Keys.UP:
                            textField.setText(textFieldHistory.getPreviousCommand());
                            textField.setCursorPosition(textField.getText().length());
                        break;
                    case Input.Keys.DOWN:
                            textField.setText(textFieldHistory.getNextCommand());
                            textField.setCursorPosition(textField.getText().length());
                        break;
                    default:
                }
                return false;
            }
        };
        stage.addListener(stageInputListener);

        setKey(Input.Keys.GRAVE);
        setVisible(false);
    }

    @Override
    public void log(String message, LogLevel logLevel) {
        super.log(message, logLevel);
        updateLogTable();
    }

    @Override
    public void log(String message) {
        super.log(message);
        updateLogTable();
    }

    private void updateLogTable() {
        ArrayList<Log> logs = (ArrayList<Log>) logger.getLogs();
        logTable.clear();
        logTable.add().expand().fill().row();
        for(int i = 0; i < logs.size(); i++) {
            Log log = logs.get(i);
            Label label;
            if(textLabels.size() > i) {
                label = textLabels.get(i);
            } else {
                label = new Label(log.getLogTextForConsole(), skin, "default-font", log.getLogLevel().getColor());
                label.setWrap(true);
                textLabels.add(label);
            }
            logTable.add(label).expandX().fillX().top().left().padLeft(4).row();
        }

        scrollPane.validate();
        scrollPane.setScrollPercentY(1);
    }

    @Override
    public void draw() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        //stage.getViewport().update(width, height, true);
        //stage.getViewport().setScreenSize(width,height);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        window.setVisible(visible);
        consoleTable.setVisible(visible);
        scrollPane.setVisible(visible);
        textField.setVisible(visible);
        logTable.setVisible(visible);
        for(Label label : textLabels) {
            label.setVisible(visible);
        }
        stage.setKeyboardFocus(visible ? textField : null);
    }

    public void removeInputProcessorFromMultiplexer() {
        multiplexer.removeProcessor(stage);
    }

    public void addInputProcessorToMultiplexer() {
        multiplexer.getProcessors().insert(0,stage);
    }

    public void setKey(int keyID) {
        this.key = keyID;
    }

    public int getKey() {
        return key;
    }

    public void setSize(int width, int height) {
        window.setSize(width,height);
    }

    public float getWidth() {
        return window.getWidth();
    }

    public float getHeight() {
        return window.getHeight();
    }

    public void setPosition(int x, int y) {
        window.setPosition(x,y);
    }

    public float getPositionX() {
        return window.getX();
    }

    public float getPositionY() {
        return window.getY();
    }

    @Override
    public void clear() {
        logTable.clear();
        logger.getLogs().clear();
        textLabels.clear();
    }

    @Override
    public void dispose() {
        multiplexer.removeProcessor(stage);
        stage.dispose();
    }

    private class TextFieldHistory {
        private final ArrayList<String> texts = new ArrayList<String>();
        private int index;

        public void add(String string) {
            if(texts.size() > 0 && texts.get(texts.size() - 1).equals(string)) {
                index = texts.size();
                return;
            }
            texts.add(string);
            index = texts.size();
        }

        public String getPreviousCommand() {
            if(texts.size() < 1) {
                return "";
            }
            if(index == 0) {
                index = texts.size();
                return "";
            }
            return texts.get(--index);
        }

        public String getNextCommand() {
            if(texts.size() < 1 || index == texts.size()) {
                return "";
            }
            if(index == texts.size() - 1) {
                index = texts.size();
                return "";
            }
            return texts.get(++index);
        }
    }
}
