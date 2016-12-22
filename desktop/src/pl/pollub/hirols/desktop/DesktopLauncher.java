package pl.pollub.hirols.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import pl.pollub.hirols.Hirols;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "HIROLS 3.5";

		config.width = 1280;
		config.height = 720;

		config.vSyncEnabled = true;
		config.samples = 4;

//        TexturePacker.Settings settings = new TexturePacker.Settings();
//        settings.maxWidth = 512;
//        settings.maxHeight = 512;
//		settings.filterMin = Texture.TextureFilter.Linear;
//		settings.filterMag = Texture.TextureFilter.Linear;
//        TexturePacker.process(settings, "./arrows", "./arrows", "arrows");

        new LwjglApplication(new Hirols(), config);
	}
}
