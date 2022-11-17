package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import controller.Assets;
import controller.AudioManager;
import controller.Constants;
import main.Boot;

public class MainMenuScreen extends ScreenAdapter {
	private OrthographicCamera camera;
	private Image background;
	private Stage stage;
	private Skin skin;
	private Table table;
	private Viewport viewport;
	private AssetManager assetManager;

	public MainMenuScreen(OrthographicCamera camera) {
		Constants.init();
		this.camera = camera;
		this.assetManager = Boot.INSTANCE.getAssets().getAssetManager();
		background = new Image(new Texture("background/background.png"));
		background.setSize(900, 600);
		skin = assetManager.get(Assets.SKIN);
		AudioManager.INSTANCE.playMusic("MainScreenMusic", true);

		viewport = new ExtendViewport(900, 600);
		stage = new Stage(viewport);

		table = new Table();
		table.add(background).top().left();
		table.setFillParent(true);
		stage.addActor(table);
	}

	@Override
	public void show() {

		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		addButton("PLAY").addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AudioManager.INSTANCE.playSound("click");
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {

					@Override
					public void run() {
						AudioManager.INSTANCE.stopMusic();

						Gdx.app.postRunnable(() -> {
							dispose();
							Boot.INSTANCE.setScreen(new GameScreen(camera));
						});

					}

				})));
			}
		});

		addButton("EXIT").addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		Gdx.input.setInputProcessor(stage);
	}

	private TextButton addButton(String name) {
		TextButton button = new TextButton(name, skin);
		table.add(button).width(200).height(50).padTop(50);
		table.row();
		return button;
	}

	public void update(float dt) {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.update(delta);
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
//		this.assetManager.dispose();
		this.stage.dispose();
	}

}