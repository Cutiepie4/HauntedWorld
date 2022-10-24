package character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import helper.AudioManager;
import things.Entity;

public class Door extends Entity {

	public static boolean button = false;
	private AudioManager audio = new AudioManager();

	public Door(float width, float height, Body body) {

		super(width, height, body);

		this.animationHandler.add(1 / 6f, "door", "close", "");

		this.animationHandler.add(1 / 6f, "door", "open", "");

		this.animationHandler.setActionDirection("close", "", false);
		
		this.audio.addSound("audio/sound/door/open.ogg");
		
		this.audio.load();
	}

	@Override
	public void update() {

		if (Door.button) {
			if (this.animationHandler.getAction().equals("close")) {
				this.animationHandler.setAction("open", false);
				this.audio.playSound("open");
			}
				
			if (this.animationHandler.isAnimationFinished())
				this.body.getFixtureList().first().setSensor(true);
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
