package character;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import model.Entity;
import screen.GameScreen;

public class Trap extends Entity {

	public static ArrayList<Trap> INSTANCE = new ArrayList<>();

	public Trap(float width, float height, Body body) {

		super(width, height, body);

		this.animationHandler.add(1 / 5f, "trap", "up", "");

		this.animationHandler.add(1 / 8f, "trap", "show", "");

		this.animationHandler.add(1 / 5f, "trap", "down", "");

		this.animationHandler.add(1, "trap", "hide", "");

		this.animationHandler.setActionDirection("hide", "", false);

		INSTANCE.add(this);

		this.damage = 3;
		
		this.y += 12f;
	}

	public static void enableTrap() {
		for (Trap i : Trap.INSTANCE) {
			i.active();
		}
	}

	public void active() {
		if (this.animationHandler.getAction().equals("hide")) {
			this.animationHandler.setAction("up", false);
			this.body.getFixtureList().first().setSensor(false);
		}
	}

	public void update() {

		if (this.animationHandler.getAction().equals("up") && this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("show", true);
		}

		if (this.animationHandler.getStateTime() >= 4f && this.animationHandler.getAction().equals("show")) {
			this.animationHandler.setAction("down", false);
		}

		if (this.animationHandler.getAction().equals("down") && this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("hide", false);
			this.body.getFixtureList().first().setSensor(true);
			Boss.INSTANCE.getAnimationHandler().setAction("idle", true);
			Boss.INSTANCE.isTrapping = false;
		}
	}

	public void render(SpriteBatch batch) {

		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width, this.y - 12f - this.height / 2, currentFrame.getRegionWidth() * 0.75f,
				currentFrame.getRegionHeight() * 0.75f);

	}

}
