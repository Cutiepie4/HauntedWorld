package character;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import main.Boot;
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

		this.health = 4;
		
		this.MAX_HEALTH = 5;

		this.damage = 2;

		this.body.setLinearDamping(8f);

		this.animationHandler.setActionDirection("idle", "", true);

		this.speed = 3f;

	}

	@Override
	public void update() {

		this.healthBar.setPosition(this.x - 10f, this.y + this.height);
		this.healthLevel.setPosition(this.x - 10f, this.y + this.height);

		if (this.animationHandler.getAction().equals("dead")) {
			if (this.animationHandler.isAnimationFinished() && !this.isDisposed()) {
				GameScreen.INSTANCE.addToRemove(this);
				this.setDisposed(true);
			}
			return;
		}

		if (!this.detected) {
			if (this.animationHandler.isAnimationFinished()) {
				Random random = new Random();
				this.body.setLinearVelocity(random.nextInt(-1, 2) * speed, random.nextInt(-1, 2) * speed);
				this.animationHandler.setAction("idle", true);
				this.animationHandler.setStateTime(0f);
			}
		}

		else if (this.animationHandler.isAnimationFinished()) {
			this.follow(Player.INSTANCE);
		}

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		this.showHealth(batch, 20, 2f);
		
		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2, this.y - this.height / 2,
				currentFrame.getRegionWidth() * 0.75f, currentFrame.getRegionHeight() * 0.75f);
	}

	public void follow(Player player) {
		if (player != null) {
			this.body.setLinearVelocity(new Vector2(-(this.body.getPosition().x - player.getBody().getPosition().x),
					-(this.body.getPosition().y - player.getBody().getPosition().y)));

			this.animationHandler.setAction("attack", true);
		}
	}

}
