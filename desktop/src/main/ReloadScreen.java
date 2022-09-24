package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ReloadScreen extends ScreenAdapter{
    private OrthographicCamera camera;
    private SpriteBatch batch;
	Texture background;
    Label label;
    private Stage stage;
    private Skin skin;
    private Table table;
    private Viewport viewport;
    private AssetManager assetManager;
    
    
    
    public ReloadScreen(OrthographicCamera camera) {
    	this.batch = new SpriteBatch();
    	this.camera = camera;
    	this.assetManager = Boot.INSTANCE.getAssets().getAssetManager();
    	background = new Texture("background/youaredead.png");
    	skin = assetManager.get(Assets.SKIN);
    }
    
    
    @Override
	public void show() {
		viewport = new ExtendViewport(900, 600);
		stage = new Stage(viewport);
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		addButton("PLAY AGAIN").addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				Boot.INSTANCE.setScreen(new GameScreen(camera));
			}
		});
		addButton("EXIT").addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				Gdx.app.exit();
			}
		});
		
		Gdx.input.setInputProcessor(stage);
	}
    
    private TextButton addButton(String name) {
		TextButton button = new TextButton(name, skin);
		table.add(button).width(200).height(50).padTop(60);
		table.row();
		return button;
	}

    public void update(float dt) {
    }

    @Override
    public void render(float delta) {
    	Gdx.gl.glClearColor(.1f, .2f, .15f, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	
    	this.update(delta);
        batch.begin();
        batch.draw(background, 0, 0, 900 , 600);
        batch.end();
        stage.act();
    	stage.draw();
    }
}
