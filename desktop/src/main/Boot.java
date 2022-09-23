package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Boot extends Game {

	public static Boot INSTANCE;
	private int widthScreen, heightScreen;
	private OrthographicCamera orthographicCamera;
	public static final float PPM = 16.0f;

	public Boot() {
		INSTANCE = this;
	}

	@Override
	public void create() {
		this.widthScreen = Gdx.graphics.getWidth();
		this.heightScreen = Gdx.graphics.getHeight();
		this.orthographicCamera = new OrthographicCamera();
		this.orthographicCamera.setToOrtho(false, widthScreen / 2.4f, heightScreen / 2.4f);
		setScreen(new GameScreen(orthographicCamera));
	}

}
