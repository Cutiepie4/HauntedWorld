package objects;

import com.badlogic.gdx.physics.box2d.Body;

public class Chest extends Items {

	public Chest(float width, float height, Body body) {
		super(width, height, body);

		this.FRAME_TIME = 1 / 8f;
		
		this.name = "Chest";

		String[] state = { "idle", "loot" };
		for (int i = 0; i < 2; i++) {
			this.animationHandler.add(FRAME_TIME, "chest", state[i], "");
		}

		this.animationHandler.setActionDirection("idle", "", true);
	}

}
