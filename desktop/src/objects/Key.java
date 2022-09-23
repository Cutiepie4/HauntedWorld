package objects;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class Key extends Items {

	public Key(float width, float height, Body body) {
		super(width, height, body);

		this.FRAME_TIME = 1 / 10f;

		this.animationHandler.setActionDirection("idle", "", true);
	}

}
