package pl.pollub.hirols;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pl.pollub.hirols.managers.HudManager;
import pl.pollub.hirols.managers.SoundManager;
import pl.pollub.hirols.managers.GameManager;
import pl.pollub.hirols.managers.UnitsManager;
import pl.pollub.hirols.screens.LoadingScreen;

public class Hirols extends Game {

	public SpriteBatch batch;
	public PooledEngine engine;
	public AssetManager assetManager;
	public SoundManager soundManager;
    public HudManager hudManager;
	public UnitsManager unitsManager;

	public GameManager gameManager;
	public InputMultiplexer multiplexer = new InputMultiplexer();

	@Override
	public void create () {
		batch = new SpriteBatch();
        engine = new PooledEngine(100,1000,100,1000);
		assetManager = new AssetManager();
		soundManager = new SoundManager(assetManager);
		gameManager = new GameManager(this,4);
		unitsManager = new UnitsManager();

		Gdx.input.setInputProcessor(multiplexer);

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}