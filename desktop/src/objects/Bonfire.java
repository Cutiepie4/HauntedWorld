package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Bonfire extends Objects {

	private static final float FRAME_TIME = 1 / 12f;

	public Bonfire(float width, float height, Body body) {
		super(width, height, body);

		this.animationHandler.add(FRAME_TIME, "bonfire", "idle", "");

		this.animationHandler.setActionDirection("idle", "", true);
	}

	@Override
	public void update() {
		this.x = this.body.getPosition().x * 16.0f;
		this.y = this.body.getPosition().y * 16.0f;

	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		TextureRegion currentFrame;

		currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2 - 2, this.y - this.height / 2,
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight() );
	}
}
