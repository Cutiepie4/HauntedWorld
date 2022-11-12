package things;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import helper.Constants;

public class Decor extends Entity {

	public Decor(float width, float height, Body body, String name) {
		super(width, height, body, name);

		this.animationHandler.add(FRAME_TIME, name, "idle", "");

		this.animationHandler.setActionDirection("idle", "", true);
	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		float x_offset = Constants.getOFFSET(this.name)[0], y_offset = Constants.getOFFSET(this.name)[1];

		batch.draw(currentFrame, this.x - currentFrame.getRegionWidth() / 2 + x_offset,
				this.y - currentFrame.getRegionHeight() / 2 + y_offset, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());
	}
}
