package pl.pollub.hirols.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.pollub.hirols.Hirols;
import pl.pollub.hirols.managers.HudManager;

/**
 * Created by Eryk on 2016-02-28.
 */
public class LoadingScreen implements Screen {

    private Hirols game;
    private AssetManager assetManager;

    private Stage stage;
    private ProgressBar bar;
    private Skin skin;

    public LoadingScreen(Hirols game) {
        this.game = game;
        this.assetManager = game.assetManager;
        this.skin = new Skin(Gdx.files.internal("ui/default_skin/uiskin.json"));

        ProgressBar.ProgressBarStyle progressBarStyle;
        progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = skin.getDrawable("default-slider");
        progressBarStyle.knob = skin.getDrawable("default-slider-knob");

        skin.add("progress-bar", progressBarStyle);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class,new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        Application.ApplicationType applicationType = Gdx.app.getType();

        switch(applicationType) {
            case Android:
                int androidVersion = Gdx.app.getVersion();
                Gdx.app.log("Device", "Android device, sdk version: "+androidVersion);
                break;
            case Desktop:
                Gdx.app.log("Device", "desktop device");
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
        
        OrthographicCamera loadCam = new OrthographicCamera();
        loadCam.setToOrtho(false);
        Viewport loadPort = new ScreenViewport(loadCam);
        stage = new Stage(loadPort, game.batch);

        bar = new ProgressBar(0, 100, 1, false, skin.get("progress-bar",ProgressBar.ProgressBarStyle.class));
        bar.setPosition(100, stage.getHeight()/5);
        bar.setWidth(stage.getWidth() - 200);
        stage.addActor(bar);

        loadAssets();

        //Texture.setAssetManager(assetManager);
    }

    private void loadAssets() {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        textureParameter.genMipMaps = false;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        textureParameter.minFilter = Texture.TextureFilter.Linear;

        FreetypeFontLoader.FreeTypeFontLoaderParameter fontLoaderParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter.fontFileName = "ui/fonts/VIDEOPHREAK.ttf";
        fontLoaderParameter.fontParameters.size = 32;
        fontLoaderParameter.fontParameters.color = Color.WHITE;
        assetManager.load("testFontSize32.ttf", BitmapFont.class, fontLoaderParameter);
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontLoaderParameter2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter2.fontFileName = "ui/fonts/VIDEOPHREAK.ttf";
        fontLoaderParameter2.fontParameters.color = Color.WHITE;
        fontLoaderParameter2.fontParameters.size = 12;
        assetManager.load("testFontSize12.ttf", BitmapFont.class, fontLoaderParameter2);
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontLoaderParameter3 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter3.fontFileName = fontLoaderParameter.fontFileName;
        fontLoaderParameter3.fontParameters.color = Color.WHITE;
        fontLoaderParameter3.fontParameters.size = 18;
        assetManager.load("testFontSize18.ttf", BitmapFont.class, fontLoaderParameter3);

        assetManager.load("test.mp3", Music.class);
        assetManager.load("animations/loadingLongPress.png", Texture.class, textureParameter);
        assetManager.load("battle/battleBackground.png", Texture.class, textureParameter);
        assetManager.load("towns/townBackground.PNG", Texture.class, textureParameter);
        assetManager.load("arrows/arrows.atlas", TextureAtlas.class);
        assetManager.load("resources/CoalPile.png", Texture.class, textureParameter);
        assetManager.load("resources/GoldPile.png", Texture.class, textureParameter);
        assetManager.load("resources/LogPile.png", Texture.class, textureParameter);
        assetManager.load("resources/StonePile.png", Texture.class, textureParameter);

        assetManager.load("animations/OrcWyvern_Standing.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWyvern_Walking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWyvern_Attacking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWyvern_Dying.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWyvern_TakingDamage.png", Texture.class, textureParameter);

        assetManager.load("animations/Orc_Staying.png", Texture.class, textureParameter);
        assetManager.load("animations/Orc_Walking.png", Texture.class, textureParameter);
        assetManager.load("animations/Orc_Attacking.png", Texture.class, textureParameter);
        assetManager.load("animations/Orc_Dying.png", Texture.class, textureParameter);
        assetManager.load("animations/Orc_TakingDamage.png", Texture.class, textureParameter);

        assetManager.load("animations/OrcCrossbow_Standing.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcCrossbow_Walking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcCrossbow_Attacking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcCrossbow_Dying.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcCrossbow_TakingDamage.png", Texture.class, textureParameter);

        assetManager.load("animations/OrcSlinger_Standing.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcSlinger_Walking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcSlinger_Attacking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcSlinger_Dying.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcSlinger_TakingDamage.png", Texture.class, textureParameter);

        assetManager.load("animations/OrcRider_Standing.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcRider_Walking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcRider_Attacking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcRider_Dying.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcRider_TakingDamage.png", Texture.class, textureParameter);

        assetManager.load("animations/OrcWarrior_Standing.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWarrior_Walking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWarrior_Attacking.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWarrior_Dying.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcWarrior_TakingDamage.png", Texture.class, textureParameter);


        assetManager.load("animations/OrcHero_Standing.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcHero_Walking.png", Texture.class, textureParameter);

        assetManager.load("animations/OrcMageHero_Standing.png", Texture.class, textureParameter);
        assetManager.load("animations/OrcMageHero_Walking.png", Texture.class, textureParameter);

        //GUI
        assetManager.load("ui/default_skin/uiskin.json", Skin.class, new SkinLoader.SkinParameter("ui/default_skin/uiskin.atlas"));

        assetManager.load("ui/button-images.png", Texture.class, textureParameter);
        assetManager.load("ui/menuDrag.png", Texture.class, textureParameter);
        assetManager.load("ui/minimapDrag.png", Texture.class, textureParameter);

        assetManager.load("temp/portrait.png", Texture.class, textureParameter);
        assetManager.load("temp/orki.png", Texture.class, textureParameter);
        assetManager.load("temp/town.gif", Texture.class, textureParameter);
    }

    @Override
    public void render(float delta) {
        update(delta);
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
        
        if(assetManager.update()) {
            if(Gdx.input.isTouched()) {
                game.hudManager = new HudManager(game);
                game.gameManager.createMap("maps/defaultMap/Map1 tiles 48x48.tmx");
                game.gameManager.setCurrentMapScreen("maps/defaultMap/Map1 tiles 48x48.tmx");
                //game.setScreen(new TownScreen(game));
                //game.setScreen(new BattleScreen(game));
            }
        } else {
            Gdx.app.log("Loading progress", String.valueOf(assetManager.getProgress()*100));
        }
    }

    public void update(float delta){
        stage.act(delta);
        bar.setValue(assetManager.getProgress() * 100);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
