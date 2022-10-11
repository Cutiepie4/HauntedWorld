package things;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import helper.BodyHelperService;
import helper.Constants;
import main.Boot;
import screen.GameScreen;

public class Items extends Objects {

	protected boolean isLooted;
	protected Image icon;

	public Items(float width, float height, Body body, String name) {
		super(width, height, body, name);

		this.FRAME_TIME = 1 / 8f;

		String name_ani = String.join("", name.split("\\s+")).toLowerCase();

		for (String i : Constants.ITEMS_STATE) {
			this.animationHandler.add(FRAME_TIME, name_ani, i, "");
		}

		try {
			this.icon = new Image(new Texture(String.format("hud/%s.png", name_ani)));
		} catch (Exception e) {

		}

		this.isLooted = false;

		this.animationHandler.setActionDirection("idle", "", true);
	}

	public Items(float x, float y, float width, float height, String name) {
		super(width, height, BodyHelperService.createBody(x, y, width, height, true, GameScreen.INSTANCE.getWorld()),
				name);

		this.body.getFixtureList().first().setSensor(true);
		
		this.body.getFixtureList().first().setUserData(this);
		
		this.FRAME_TIME = Constants.getFRAME_TIME(name);

		String name_ani = String.join("", name.split("\\s+"));

		for (String i : Constants.ITEMS_STATE) {
			this.animationHandler.add(FRAME_TIME, name_ani, i, "");
		}

		try {
			this.icon = new Image(new Texture(String.format("hud/%s.png", name_ani)));
		} catch (Exception e) {

		}

		this.isLooted = false;

		this.animationHandler.setActionDirection("idle", "", true);

	}

	@Override
	public void update() {
		if (this.isLooted && !isDisposed() && this.animationHandler.isAnimationFinished()) {
			GameScreen.INSTANCE.addToRemove(this);
			this.setDisposed(true);
			this.dropItem();
		}

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2, this.y - this.height / 2,
				currentFrame.getRegionWidth() * 0.75f, currentFrame.getRegionHeight() * 0.75f);
	}

	public void loot() {
		this.animationHandler.setAction("loot", false);
		this.isLooted = true;
	}

	public String getName() {
		return this.name;
	}
	
	public boolean isLooted() {
		return isLooted;
	}

	public void dropItem() {
		if (this.name.equals("Chest")) {
			Random rnd = new Random();
			if (rnd.nextInt(100) < 100) {
				int idx = rnd.nextInt(Constants.ITEMS_DROP.length);
				GameScreen.INSTANCE.addObjects(new Items(this.x, this.y, 12, 12, Constants.ITEMS_DROP[idx]));
			}
		}
	}

}
