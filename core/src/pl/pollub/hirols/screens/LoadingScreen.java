package pl.pollub.hirols.screens;

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

    private Viewport loadPort;
    private float barval = 0;

    private Stage stage;

    private ProgressBar bar;
    private Skin skin;

    public LoadingScreen(Hirols game) {
        this.game = game;
        this.assetManager = game.assetManager;
        this.skin = new Skin();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.pack"));
        skin.addRegions(atlas);

        ProgressBar.ProgressBarStyle progressBarStyle;
        progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = skin.getDrawable("default-slider");
        progressBarStyle.knob = skin.getDrawable("default-slider-knob");

        skin.add("progress-bar", progressBarStyle);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class,new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        switch(Gdx.app.getType()) {
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
        
        OrthographicCamera loadCam;

        loadCam = new OrthographicCamera();
        loadCam.setToOrtho(false);

        loadPort = new ScreenViewport(loadCam);

        stage = new Stage(loadPort, game.batch);

        bar = new ProgressBar(0, 100, 1, false, skin.get("progress-bar",ProgressBar.ProgressBarStyle.class));
        bar.setPosition(100, 100);
        bar.setWidth(stage.getWidth() - 200);
        stage.addActor(bar);

        //tutaj wszystko do załadowania:
        loadAssets();

        //Texture.setAssetManager(assetManager);
    }

    private void loadAssets() {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        textureParameter.genMipMaps = true;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        textureParameter.minFilter = Texture.TextureFilter.MipMap;
        assetManager.load("arrows/arrow_E.png", Texture.class, textureParameter);
        assetManager.load("arrows/arrow_N.png", Texture.class, textureParameter);
        assetManager.load("arrows/arrow_NE.png", Texture.class, textureParameter);
        assetManager.load("arrows/arrow_NW.png", Texture.class, textureParameter);
        assetManager.load("arrows/arrow_S.png", Texture.class, textureParameter);
        assetManager.load("arrows/arrow_SE.png", Texture.class, textureParameter);
        assetManager.load("arrows/arrow_SW.png", Texture.class, textureParameter);
        assetManager.load("arrows/arrow_W.png", Texture.class, textureParameter);
        assetManager.load("arrows/cross.png", Texture.class, textureParameter);
        assetManager.load("arrows/sword.png", Texture.class, textureParameter);
        assetManager.load("animations/walking_swamp.png", Texture.class, textureParameter);
        assetManager.load("animations/standing_mech.png", Texture.class, textureParameter);
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontLoaderParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter.fontFileName = "fonts/test.TTF";
        fontLoaderParameter.fontParameters.size = 32;
        fontLoaderParameter.fontParameters.color = Color.BLACK;
        assetManager.load("testFontSize32.ttf", BitmapFont.class, fontLoaderParameter);
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontLoaderParameter2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter2.fontFileName = "fonts/test.TTF";
        fontLoaderParameter2.fontParameters.color = Color.BLACK;
        fontLoaderParameter2.fontParameters.size = 12;
        assetManager.load("testFontSize12.ttf", BitmapFont.class, fontLoaderParameter2);
        assetManager.load("test.mp3", Music.class);
        assetManager.load("gold.png", Texture.class, textureParameter);
        assetManager.load("animations/snake_stay_animation.png", Texture.class, textureParameter);
        assetManager.load("animations/loadingLongPress.png", Texture.class, textureParameter);
        assetManager.load("hexagon.png", Texture.class, textureParameter);
        assetManager.load("hexagon2.png", Texture.class, textureParameter);
        assetManager.load("battleBackground.png", Texture.class, textureParameter);
        assetManager.load("rect.png", Texture.class, textureParameter);
        assetManager.load("towns/snow-town.png", Texture.class, textureParameter);

        //GUI

        assetManager.load("fonts/test2.fnt", BitmapFont.class);

        assetManager.load("resources/energy.png", Texture.class, textureParameter);
        assetManager.load("resources/fuel.png", Texture.class, textureParameter);
        assetManager.load("resources/grafen2.png", Texture.class, textureParameter);
        assetManager.load("resources/metal.png", Texture.class, textureParameter);
        assetManager.load("resources/alienrsc.png", Texture.class, textureParameter);
        assetManager.load("resources/coinsresource.png", Texture.class, textureParameter);



        assetManager.load("ui/atlas.pack", TextureAtlas.class);

        assetManager.load("default_skin/uiskin.atlas", TextureAtlas.class);
        assetManager.load("default_skin/uiskin.json", Skin.class, new SkinLoader.SkinParameter("default_skin/uiskin.atlas"));

        assetManager.load("ui/button-images.png", Texture.class, textureParameter);
        assetManager.load("ui/menuDrag.png", Texture.class, textureParameter);
        assetManager.load("ui/minimapDrag.png", Texture.class, textureParameter);
        //assetManager.load("ui/townheroDrag.png", Texture.class, textureParameter);
    }

    @Override
    public void render(float delta) {

        update(delta);
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
        
        if(assetManager.update()) {
            if(Gdx.input.isTouched()) {
                //jeśli wczytano assety i naciśnieto
                game.hudManager = new HudManager(game);
                game.gameMapManager.createMap("temp.tmx");
                game.setScreen(game.gameMapManager.getCurrentMapScreen());
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

    }

    @Override
    public void dispose() {

    }
}
