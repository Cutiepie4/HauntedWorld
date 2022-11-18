package character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import controller.AudioManager;
import model.Entity;
import screen.GameScreen;

public class Door extends Entity {

	public boolean button = false;

	public Door(float width, float height, Body body) {

		super(width, height, body);

		this.animationHandler.add(1 / 6f, "door", "close", "");

		this.animationHandler.add(1 / 6f, "door", "open", "");

		this.animationHandler.setActionDirection("close", "", false);

	}

	@Override
	public void update() {

		if (this.button) {
			if (this.animationHandler.getAction().equals("close")) {
				this.animationHandler.setAction("open", false);
				AudioManager.INSTANCE.playSound("open");
			}

			if (this.animationHandler.getAction().equals("open") && this.animationHandler.isAnimationFinished()) {
				this.body.getFixtureList().first().setSensor(true);
				if (Boss.INSTANCE.getAnimationHandler().getAction().equals("dead")) {
					GameScreen.isWin = true;
				}
			}

		}

	}

	@Override
	public void render(SpriteBatch batch) {

		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2, this.y - this.height / 2, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());

	}
}
