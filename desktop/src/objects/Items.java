package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import screen.GameScreen;

public abstract class Items extends Objects {

	protected String name;
	protected boolean isLooted;
	protected Image image;

	public Items(float width, float height, Body body) {
		super(width, height, body);

		this.isLooted = false;

		this.name = "";

		this.image = null;
	}

	@Override
	public void update() {
		if (this.isLooted && !isDisposed && this.animationHandler.isAnimationFinished() && !this.name.equals("Chest")) {
			GameScreen.INSTANCE.addToRemove(this);
			this.isDisposed = true;
		}

		this.x = this.body.getPosition().x * 16.0f;
		this.y = this.body.getPosition().y * 16.0f;

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

}
