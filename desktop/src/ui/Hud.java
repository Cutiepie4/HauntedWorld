package ui;

import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import character.Player;
import helper.HudActorItems;
import main.DesktopLauncher;

public class Hud {

	public static Hud INSTANCE;
	private Stage stageUI;
	private Viewport viewport;

	private Image healthBar, healthLevel, healthBackground;
	private float timer;
	public LinkedHashMap<Label, Float> message = new LinkedHashMap<>();

	public Hud(SpriteBatch batch) {
		INSTANCE = this;

		this.timer = 0f;

		viewport = new FitViewport(DesktopLauncher.SCREEN_WIDTH, DesktopLauncher.SCREEN_HEIGHT,
				new OrthographicCamera());
		stageUI = new Stage(viewport, batch);

		stageUI.addActor(new Table());
		stageUI.addActor(new Table());
		stageUI.addActor(new Table());
		stageUI.addActor(new Table());

		this.addMessage("Welcome to the World of Haunt.");
		this.addMessage("You must survive and find the way to escape here.");

		this.createHudHealthBar();
		this.volumeMessage();
	}

	private void volumeMessage() {
		Table tbl = (Table) stageUI.getActors().get(3);
		tbl.right();
		tbl.top();
		tbl.setFillParent(true);
		tbl.padRight(50);
		tbl.padTop(30);

		tbl.add(new Label("F1, F2: change Music Volume.", new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
		tbl.row();
		tbl.add(new Label("F3, F4: change SFX Volume.", new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
		tbl.row();
		tbl.add(new Label("F5: Mute.", new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
		tbl.row();
		tbl.add(new Label("SPACE: Attack.", new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
		tbl.row();
		tbl.add(new Label("W, A, S, D: Move.", new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
	}

	private void updateMessage(float delta) {
		Table tblMessage = (Table) stageUI.getActors().get(2);
		tblMessage.clear();

		tblMessage.bottom();
		tblMessage.right();
		tblMessage.setFillParent(true);
		tblMessage.padBottom(50).padRight(50);

		for (Label i : message.keySet()) {
			if (message.get(i) > 4f) {
				i.act(delta);
			}
			tblMessage.add(i);
			tblMessage.row();
		}
	}

	private void createHudHealthBar() {

		healthBar = new Image(new Texture("hud/health_bar_player.png"));
		healthBar.scaleBy(1.32f);

		healthLevel = new Image(new Texture("hud/health_level_player.png"));
		healthLevel.scaleBy(1.32f);

		healthBackground = new Image(new Texture("hud/health_level_background_player.png"));
		healthBackground.scaleBy(1.32f);

		Table table = (Table) stageUI.getActors().get(0);

		table.clear();

		table.top();
		table.left();
		table.setFillParent(true);

		table.add(healthBackground).padTop(50).padLeft(50);

		table.add(healthLevel).padTop(50).padLeft(-56);

		table.add(healthBar).padTop(50).padLeft(-56);

	}

	private void updateHudItems() {

		Table table = (Table) stageUI.getActors().get(1);
		table.clear();

		table.bottom();
		table.left();
		table.setFillParent(true);

		if (Player.INSTANCE != null)
			for (String i : Player.INSTANCE.getInventory().keySet()) {
				int count = Player.INSTANCE.getInventory().get(i);
				if (count > 0) {
					HudActorItems actor = new HudActorItems(i, Player.INSTANCE.getInventory().get(i));
					table.add(actor.getImage()).padBottom(35).padLeft(50);
					table.add(actor.getLabel()).padBottom(50).padLeft(25);
					table.row();
				}
			}

	}

	public void update(float delta) {

		this.updateHudItems();

		this.updateMessageTimer(delta);

		this.updateMessage(delta);

		this.timer += delta;

		healthLevel.setWidth(16 + (float) 40 / Player.MAX_HEALTH * Player.INSTANCE.getHealth());

	}

	private void updateMessageTimer(float delta) {
		LinkedHashMap<Label, Float> newMessage = new LinkedHashMap<>();
		for (Label i : message.keySet()) {
			if (message.get(i) + delta < 5f) {
				newMessage.put(i, message.get(i) + delta);
			}
		}
		message = new LinkedHashMap<>(newMessage);
	}

	public void addMessage(String text) {
		Label lbl = new Label(text, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		lbl.addAction(Actions.sequence(Actions.fadeOut(0.5f)));
		message.put(lbl, 0f);
	}

	public void draw(SpriteBatch batch) {
		batch.setProjectionMatrix(this.stageUI.getCamera().combined);
		this.stageUI.draw();
	}

}
