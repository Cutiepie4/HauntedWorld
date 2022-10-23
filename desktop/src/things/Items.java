package things;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import helper.BodyHelperService;
import helper.Constants;
import screen.GameScreen;

public class Items extends Entity {
	
	protected boolean isLooted;
	protected Image icon;

	public Items(float width, float height, Body body, String name) {
		super(width, height, body, name);

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

		GameScreen.INSTANCE.addObjects(this);
	}

	public Items(float x, float y, float width, float height, String name) {
		super(width, height, BodyHelperService.createBody(x, y, width, height, true, GameScreen.INSTANCE.getWorld()),
				name);

		this.body.getFixtureList().first().setSensor(true);

		this.body.getFixtureList().first().setUserData(this);

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

		GameScreen.INSTANCE.addObjects(this);
	}

	@Override
	public void update() {

		if (this.isLooted) {
			if (!isDisposed()) {
				GameScreen.INSTANCE.addToRemove(this);
				this.setDisposed(true);
			}
			return;
		}
		
		super.update();
	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - currentFrame.getRegionWidth() / 2,
				this.y - currentFrame.getRegionHeight() / 2, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());
	}

	public void loot() {
		this.animationHandler.setAction("loot", false);
		this.isLooted = true;
	}

	public boolean isLooted() {
		return isLooted;
	}
}
