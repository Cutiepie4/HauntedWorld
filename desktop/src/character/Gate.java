package character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import model.Entity;

public class Gate extends Entity {

	public static boolean button = false;

	public Gate(float width, float height, Body body) {

		super(width, height, body);

		this.y += 8f;

		this.animationHandler.add(1 / 6f, "gate", "close", "");

		this.animationHandler.add(1 / 6f, "gate", "open", "");

		this.animationHandler.setActionDirection("close", "", false);
	}

	@Override
	public void update() {
		if (Gate.button) {
			if (this.animationHandler.getAction().equals("close"))
				this.animationHandler.setAction("open", false);
			if (this.animationHandler.isAnimationFinished())
				this.body.getFixtureList().first().setSensor(true);
		}

		else {
			if (this.animationHandler.getAction().equals("open") && this.animationHandler.isAnimationFinished()) {
				this.body.getFixtureList().first().setSensor(false);
				this.animationHandler.setAction("close", false);
			}
		}

	}

	@Override
	public void render(SpriteBatch batch) {

		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2, this.y - 8f - this.height / 2, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());

	}
}
