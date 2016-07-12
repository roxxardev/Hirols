package pl.pollub.hirols.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.pollub.hirols.Hirols;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "HIROLS 3.5";

		config.width = 1280;
		config.height = 720;

		config.vSyncEnabled = true;
		config.samples = 4;
		new LwjglApplication(new Hirols(), config);
	}
}
