package pl.pollub.hirols.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.managers.GameManager;

/**
 * Created by erykp_000 on 2016-12-25.
 */

public class MainMenuScreen implements Screen{
    private Hirols game;

    private final Stage stage;
    private final Skin skin;
    private VisTextButton singlelayerButton, multiplayerButton, optionsButton, closeButton;
    private Table buttonsTable, hotSeatTable;

    private final int screenWidth = 1920;
    private final int screenHeight = 1080;

    public MainMenuScreen(final Hirols game) {
        this.game = game;

        Viewport loadPort = new FitViewport(screenWidth,screenHeight);
        this.stage = new Stage(loadPort, game.batch);
        this.skin = game.hudManager.skin;

        buttonsTable = new Table();
        buttonsTable.setSize(screenWidth / 2, screenHeight - 200);
        buttonsTable.setPosition(screenWidth / 2 - buttonsTable.getWidth() / 2, (screenHeight - buttonsTable.getHeight()) / 2);

        VisTextButton.VisTextButtonStyle buttonStyle = skin.get("mainMenuStyle", VisTextButton.VisTextButtonStyle.class);

        singlelayerButton = new VisTextButton("Singleplayer", new VisTextButton.VisTextButtonStyle(buttonStyle));
        multiplayerButton = new VisTextButton("Multiplayer", new VisTextButton.VisTextButtonStyle(buttonStyle));
        optionsButton = new VisTextButton("Options", new VisTextButton.VisTextButtonStyle(buttonStyle));
        closeButton = new VisTextButton("Close game", new VisTextButton.VisTextButtonStyle(buttonStyle));

        multiplayerButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonsTable.remove();
                VisTextButton.VisTextButtonStyle buttonStyle = skin.get("mainMenuStyle", VisTextButton.VisTextButtonStyle.class);

                hotSeatTable = new Table();
                hotSeatTable.setBackground(game.hudManager.getTransparentBackground().tint(new Color(1,1,1,0.08f)));
                hotSeatTable.setSize(screenWidth / 3, screenHeight / 2);
                hotSeatTable.setPosition(screenWidth / 2 - hotSeatTable.getWidth() / 2, (screenHeight - hotSeatTable.getHeight()) / 2);

                Label.LabelStyle labelStyle = new Label.LabelStyle(buttonStyle.font, buttonStyle.fontColor);
                Label title = new Label("Hotseat", labelStyle);
                hotSeatTable.defaults().pad(10);
                hotSeatTable.add(title).expandX().height(30).top().colspan(2).row();

                final ArrayList<TextField> playerFields = new ArrayList<>();

                for(int i = 0; i < 4; i++) {
                    Label number = new Label((i+1)+".", labelStyle);
                    number.setAlignment(Align.center);

                    TextField playerNameField = new TextField("", new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class)));
                    playerNameField.getStyle().font = buttonStyle.font;
                    playerNameField.getStyle().fontColor = buttonStyle.fontColor;
                    playerNameField.getStyle().messageFontColor = Color.GRAY;
                    playerNameField.setMessageText("Type " + (i + 1) + ". player name here");
                    playerFields.add(playerNameField);
                    hotSeatTable.add(number);
                    hotSeatTable.add(playerNameField).expand().fill().row();
                }

                VisTextButton confirm, cancel;
                confirm = new VisTextButton("Ok", new VisTextButton.VisTextButtonStyle(buttonStyle));
                cancel = new VisTextButton("Cancel", new VisTextButton.VisTextButtonStyle(buttonStyle));
                cancel.pad(5);
                confirm.pad(5);
                hotSeatTable.add(cancel).fill();
                hotSeatTable.add(confirm).fill();

                stage.addActor(hotSeatTable);

                cancel.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hotSeatTable.remove();
                        stage.addActor(buttonsTable);
                    }
                });

                confirm.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        List<String> playerNames = new ArrayList<>();
                        for (TextField textField : playerFields) {
                            if (!textField.getText().isEmpty()) {
                                playerNames.add(textField.getText());
                            }
                        }
                        if (!playerNames.isEmpty()) {
                            game.gameManager = new GameManager(game, playerNames);
                            game.gameManager.setCurrentMapScreen(game.gameManager.createMap("maps/defaultMap/Map1 tiles 48x48.tmx"));
                        }
                    }
                });

            }
        });

        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        buttonsTable.defaults().fill().pad(20);
        buttonsTable.add(singlelayerButton).expand().row();
        buttonsTable.add(multiplayerButton).expand().row();
        buttonsTable.add(optionsButton).expand().row();
        buttonsTable.add(closeButton).expand();

        stage.addActor(buttonsTable);
    }

    @Override
    public void show() {
        game.multiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        game.multiplexer.removeProcessor(stage);
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
