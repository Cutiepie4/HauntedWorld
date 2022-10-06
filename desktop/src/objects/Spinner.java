package objects;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import screen.GameScreen;

public class Spinner extends Enemy {

	public Spinner(float width, float height, Body body) {

		super(width, height, body);

		this.FRAME_TIME = 1 / 8f;

		this.name = "Spinner";

		String[] state = { "idle", "dead", "hit", "attack" };
		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "spinner", state[i], "");
		}

		this.health = 5;

		this.damage = 2;

		this.body.setLinearDamping(8f);

		this.animationHandler.setActionDirection("idle", "", true);

		this.speed = 3f;

	}

	@Override
	public void update() {

		if (this.animationHandler.getAction().equals("dead")) {
			if (this.animationHandler.isAnimationFinished() && !this.isDisposed) {
				GameScreen.INSTANCE.addToRemove(this);
				this.isDisposed = true;
			}
			return;
		}

		if (this.target == null) {
			if (this.animationHandler.isAnimationFinished()) {
				Random random = new Random();
				this.body.setLinearVelocity(random.nextInt(-1, 2) * speed, random.nextInt(-1, 2) * speed);
				this.animationHandler.setAction("idle", true);
				this.animationHandler.setStateTime(0f);
			}
		}

		else if (this.animationHandler.isAnimationFinished()) {
			this.follow(target);
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

	public void follow(Player player) {
		if (player != null) {
			this.body.setLinearVelocity(new Vector2(-(this.body.getPosition().x - player.body.getPosition().x),
					-(this.body.getPosition().y - player.body.getPosition().y)));

			this.animationHandler.setAction("attack", true);
		}
	}

}
