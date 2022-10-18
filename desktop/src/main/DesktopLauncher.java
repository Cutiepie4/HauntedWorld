package main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static final int SCREEN_WIDTH = 900;

	public static final int SCREEN_HEIGHT = 600;

	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(false);
		config.useVsync(true);
		config.setWindowIcon("icon.jpg");
		config.setForegroundFPS(60);
		config.setTitle("Escape The Haunt");
		config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
		new Lwjgl3Application(new Boot(), config);
	}
}
