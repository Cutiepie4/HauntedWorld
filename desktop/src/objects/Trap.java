package objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Trap extends Objects {

	public static ArrayList<Trap> INSTANCE = new ArrayList<>();

	public Trap(float width, float height, Body body) {

		super(width, height, body);

		this.animationHandler.add(1 / 5f, "trap", "up", "");

		this.animationHandler.add(1 / 8f, "trap", "show", "");

		this.animationHandler.add(1 / 5f, "trap", "down", "");

		this.animationHandler.add(1, "trap", "hide", "");

		this.animationHandler.setActionDirection("hide", "", false);

		INSTANCE.add(this);
	}

	public static void enableTrap() {
		for (Trap i : Trap.INSTANCE) {
			i.active();
		}
	}

	public void active() {
		if (this.animationHandler.getAction().equals("hide")) {
			this.animationHandler.setAction("up", false);
		}
	}

	public void update() {

		if (this.animationHandler.getAction().equals("up") && this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("show", true);
			this.body.getFixtureList().first().setSensor(false);
		}

		if (this.animationHandler.getStateTime() >= 5) {
			this.animationHandler.setAction("down", false);
		}

		if (this.animationHandler.getAction().equals("down") && this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("hide", false);
			this.body.getFixtureList().first().setSensor(true);
		}

	}

	public void render(SpriteBatch batch) {

		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width, this.y - this.height / 2, currentFrame.getRegionWidth() * 0.75f,
				currentFrame.getRegionHeight() * 0.75f);

	}
}
