package things;

import com.badlogic.gdx.physics.box2d.Body;

public class Decor extends Objects {
	public Decor(float width, float height, Body body, String name) {
		super(width, height, body);

		this.FRAME_TIME = 1 / 6f;

		this.animationHandler.add(FRAME_TIME, name, "idle", "");

		this.animationHandler.setActionDirection("idle", "", true);

	}
}
