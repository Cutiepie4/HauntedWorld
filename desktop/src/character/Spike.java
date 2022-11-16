package character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import model.Entity;

public class Spike extends Entity {

	private float timer;
	public static boolean isActive = false;

	public Spike(float width, float height, Body body) {

		super(width, height, body);

		this.animationHandler.add(1 / 8f, "spike", "show", "");

		this.animationHandler.add(1, "spike", "hide", "");

		this.hide();

		this.damage = 3;

		this.y += 12;
	}

	private void hide() {
		this.animationHandler.setActionDirection("hide", "", false);
		this.timer = 0f;
	}

	@Override
	public void update() {

		this.timer += Gdx.graphics.getDeltaTime();

		if (this.timer > 4f) {
			this.animationHandler.setAction("show", false);
			this.body.getFixtureList().first().setSensor(false);
			this.timer = 0f;
		}

		if (this.animationHandler.getAction().equals("show") && this.animationHandler.isAnimationFinished()) {
			this.body.getFixtureList().first().setSensor(true);
			this.hide();
		}
	}

	@Override
	public void render(SpriteBatch batch) {

		update();

//		TextureRegion currentFrame = this.animationHandler.getFrame();
//
//		batch.draw(currentFrame, this.x - this.width / 2, this.y - 12f - this.height / 2, currentFrame.getRegionWidth(),
//				currentFrame.getRegionHeight());

	}

	@Override
	public boolean shouldDraw() {
		return true;
	}

}
