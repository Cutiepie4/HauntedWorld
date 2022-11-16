package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;

import controller.Assets;
import screen.MainMenuScreen;

public class Boot extends Game {

	public static Boot INSTANCE;
	private OrthographicCamera orthographicCamera;
	public static final float PPM = 16.0f;
	private Assets assets;

	public Boot() {
		INSTANCE = this;
	}

	@Override
	public void create() {
		this.assets = new Assets();
		assets.loadALL();
		assets.getAssetManager().finishLoading();
		this.orthographicCamera = new OrthographicCamera();
		this.orthographicCamera.setToOrtho(false, DesktopLauncher.SCREEN_WIDTH / 2.4f,
				DesktopLauncher.SCREEN_HEIGHT / 2.4f);

		setScreen(new MainMenuScreen(orthographicCamera));
	}

	public Assets getAssets() {
		return assets;
	}

}
