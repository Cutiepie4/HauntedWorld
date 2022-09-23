package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Crystal extends Items {

	public Crystal(float width, float height, Body body) {
		super(width, height, body);

		this.FRAME_TIME = 1 / 8f;
		this.name = "Crystal";
		this.image = new Image(new Texture(Gdx.files.internal("hud/crystal.png")));

		String[] state = { "idle", "loot" };
		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "crystal", state[i], "");
		}

		this.isLooted = false;

		this.animationHandler.setActionDirection("idle", "", true);
	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		TextureRegion currentFrame;

		currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
				currentFrame.getRegionHeight() * 0.75f);
	}

}
