package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import main.Boot;

public class Door extends Objects {

	private static final float FRAME_TIME = 1 / 8f;

	public Door(float width, float height, Body body) {
		super(width, height, body);

		this.animationHandler.add(FRAME_TIME, "door", "open", "");

		this.animationHandler.setActionDirection("open", "", true);
	}

	@Override
	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		TextureRegion currentFrame;

		currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2, this.y - this.height / 2,
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	}
}
