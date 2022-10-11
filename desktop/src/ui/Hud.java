package ui;

import java.util.ArrayList;
import java.util.HashMap;

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

import character.Player;
import helper.HudActor;
import main.DesktopLauncher;

public class Hud {

	public static Hud INSTANCE;
	public Stage stage;
	private Viewport viewport;

	private Table tblMessage;
	private Label lblMessage;

	private Image healthBar, healthLevel, healthBackground;
	private float timer;
	public ArrayList<String> message = new ArrayList<String>();

	public Hud(SpriteBatch sb) {
		INSTANCE = this;

		this.timer = 0f;

		viewport = new FitViewport(DesktopLauncher.SCREEN_WIDTH, DesktopLauncher.SCREEN_HEIGHT,
				new OrthographicCamera());
		stage = new Stage(viewport, sb);

		stage.addActor(new Table());
		stage.addActor(new Table());
		stage.addActor(new Table());

		this.createMessage();
		this.createHudHealthBar();
	}

	private void createMessage() {

		tblMessage = (Table) stage.getActors().first();

		tblMessage.bottom();
		tblMessage.right();
		tblMessage.setFillParent(true);

		lblMessage = new Label("Welcome to the World of Haunt.", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		tblMessage.add(lblMessage).padBottom(50).padRight(50);

	}

	private void createHudHealthBar() {

		healthBar = new Image(new Texture("hud/health_bar_player.png"));
		healthBar.scaleBy(1.32f);

		healthLevel = new Image(new Texture("hud/health_level_player.png"));
		healthLevel.scaleBy(1.32f);

		healthBackground = new Image(new Texture("hud/health_level_background_player.png"));
		healthBackground.scaleBy(1.32f);

		Table table = (Table) stage.getActors().get(1);

		table.top();
		table.left();
		table.setFillParent(true);

		table.add(healthBackground).padTop(50).padLeft(50);

		table.add(healthLevel).padTop(50).padLeft(-56);

		table.add(healthBar).padTop(50).padLeft(-56);

	}

	private void updateHudActor() {

		Table table = new Table();

		table.bottom();

		table.left();

		table.setFillParent(true);

		if (Player.INSTANCE != null)
			for (String i : Player.INSTANCE.getInventory().keySet()) {
				int count = Player.INSTANCE.getInventory().get(i);
				if (count > 0) {
					HudActor actor = new HudActor(i, Player.INSTANCE.getInventory().get(i));
					table.add(actor.getImage()).padBottom(35).padLeft(50);
					table.add(actor.getLabel()).padBottom(50).padLeft(25);
					table.row();
				}
			}

		stage.getActors().pop();
		
		stage.addActor(table);
	}

	public void update(float delta) {

		this.updateHudActor();

		this.timer += delta;

		if (this.timer > 2f && !message.isEmpty()) {
			lblMessage.setText(message.get(0));
			message.remove(0);
			this.timer = 0f;
		}

		healthLevel.setWidth(16 + 4 * Player.INSTANCE.getHealth());

	}

	public void printMessage(String text) {
		message.add(text);
	}
}
