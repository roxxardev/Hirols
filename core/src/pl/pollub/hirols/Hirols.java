package pl.pollub.hirols;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pl.pollub.hirols.managers.HudManager;
import pl.pollub.hirols.managers.SoundManager;
import pl.pollub.hirols.managers.GameMapManager;
import pl.pollub.hirols.screens.LoadingScreen;

public class Hirols extends Game {

	public SpriteBatch batch;
	public Engine engine;
	public AssetManager assetManager;
	public SoundManager soundManager;
    public HudManager hudManager;

	public GameMapManager gameMapManager;
	public InputMultiplexer multiplexer = new InputMultiplexer();

	@Override
	public void create () {
		batch = new SpriteBatch();
        engine = new Engine();
		assetManager = new AssetManager();
		soundManager = new SoundManager(assetManager);
		gameMapManager = new GameMapManager(this);

		Gdx.input.setInputProcessor(multiplexer);

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}