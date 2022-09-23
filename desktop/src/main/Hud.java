package main;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import helper.HudObjectsHelper;
import objects.Player;

public class Hud {

	public static Hud INSTANCE;
	public Stage stage;
	private Viewport viewport;

	private Integer worldTimer;

	private Label countdownLabel, timeLabel, linkLabel, levelLabel, message;
	private HashMap<String, HudObjectsHelper> listObjects = new HashMap<>();

	private Image healthBar, health, levelBox;
	private Table messageTable;
	private float timer;

	public Hud(SpriteBatch sb) {
		INSTANCE = this;

		// define tracking variables
		worldTimer = 10;

		// setup the HUD viewport using a new camera separate from game camera
		// define stage using that viewport and games sprite batch
		viewport = new FitViewport(DesktopLauncher.SCREEN_WIDTH, DesktopLauncher.SCREEN_HEIGHT,
				new OrthographicCamera());
		stage = new Stage(viewport, sb);

		// define labels using the String, and a Label style consisting of a font and
		// color

		countdownLabel = new Label(String.format("%03d", worldTimer),
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("LEFTOVER TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		linkLabel = new Label("POINTS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		listObjects.put("Silver Key",
				new HudObjectsHelper(new Image(new Texture(Gdx.files.internal("hud/silverkey.png"))),
						new Label(String.format("%d", 0), new Label.LabelStyle(new BitmapFont(), Color.WHITE))));
		listObjects.put("Gold Key", new HudObjectsHelper(new Image(new Texture(Gdx.files.internal("hud/goldkey.png"))),
				new Label(String.format("%d", 0), new Label.LabelStyle(new BitmapFont(), Color.WHITE))));
		listObjects.put("Crystal", new HudObjectsHelper(new Image(new Texture(Gdx.files.internal("hud/crystal.png"))),
				new Label(String.format("%d", 0), new Label.LabelStyle(new BitmapFont(), Color.WHITE))));

		// add labels to table, padding the top, and giving them all equal width with
		// expandX

//		table.add(linkLabel).expandX().padTop(30);
//		table.add(timeLabel).expandX().padTop(30);
//		table.row();
//		table.add(scoreLabel).expandX();
//		table.add(countdownLabel).expandX();

		this.timer = 0f;
		// add table to the stage
		this.createMessage();
		this.createHudHealthBar();
		this.createHudObjects();
	}

	private void createMessage() {

		messageTable = new Table();

		messageTable.bottom();
		messageTable.right();
		messageTable.setFillParent(true);

		message = new Label("Welcome to the World of Haunt.", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		messageTable.add(message).padBottom(50).padRight(50);

		stage.addActor(messageTable);
	}

	private void createHudHealthBar() {

		healthBar = new Image(new Texture(Gdx.files.internal("hud/healthbar.png")));
		healthBar.scaleBy(1.32f);

		health = new Image(new Texture(Gdx.files.internal("hud/health_hud.png")));
		health.scaleBy(1.32f);

		Table table = new Table();

		table.top();
		table.left();
		table.setFillParent(true);

		table.add(health).padTop(50).padLeft(50);

		table.add(healthBar).padTop(50).padLeft(-56);

		stage.addActor(table);
	}

	private void createHudObjects() {

		String[] objects = { "Silver Key", "Gold Key", "Crystal" };

		Table table = new Table();

		table.bottom();

		table.left();

		table.setFillParent(true);

		for (int i = 0; i < objects.length; i++) {
			table.add(listObjects.get(objects[i]).getImage()).padLeft(40);
		}

		table.row();

		for (int i = 0; i < objects.length; i++) {
			table.add(listObjects.get(objects[i]).getLabel()).padBottom(50).padLeft(50);
		}

		stage.addActor(table);
	}

	public void update(float delta) {

		this.timer += delta;

		if (this.timer > 3) {
			message.setText("");
		}

		for (String i : Player.INSTANCE.getInventory().keySet()) {
			if (listObjects.containsKey(i))
				listObjects.get(i).setCount(Player.INSTANCE.getInventory().get(i));
		}

		health.setWidth(16 + 4 * Player.INSTANCE.getHealth());

//		if (timer >= 1) {
//			if (worldTimer > 0) {
//				countdownLabel.setText(String.format("%d", worldTimer));
//				worldTimer--;
//			} else {
//				countdownLabel.setText("TIME UP");
//			}
//
//			timer = 0;
//		}
	}

	public void printMessage(String text) {
		message.setText(text);
		this.timer = 0f;
	}
}
