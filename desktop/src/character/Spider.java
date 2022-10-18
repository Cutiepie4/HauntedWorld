package character;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import helper.Constants;
import main.Boot;
import screen.GameScreen;
import things.Items;

public class Spider extends Enemy {
	public Spider(float width, float height, Body body) {

		super(width, height, body);

		this.FRAME_TIME = 1 / 8f;

		this.name = "Spider";

		for (String i : Constants.ACTION) {
			this.animationHandler.add(FRAME_TIME, "spider", i, "");
		}

		this.health = 3;

		this.MAX_HEALTH = 8;

		this.damage = 3;

		this.body.setLinearDamping(8f);

		this.animationHandler.setActionDirection("idle", "", true);

		this.speed = 4f;

	}

	@Override
	public void update() {

		this.healthBar.setPosition(this.x - 10f, this.y + this.height);
		this.healthLevel.setPosition(this.x - 10f, this.y + this.height);

		if (this.animationHandler.getAction().equals("dead")) {
			if (!this.isDisposed()) {
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

	@Override
	public void dropItem() {
		Random rnd = new Random();
		if (rnd.nextInt(100) < 100) { // rate drop items
			int idx = rnd.nextInt(Constants.ITEMS_DROP.length);
			new Items(this.x, this.y, 10, 10, Constants.ITEMS_DROP[idx]);
		}
	}
}
