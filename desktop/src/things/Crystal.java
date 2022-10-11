package things;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Crystal extends Items {

	public Crystal(float width, float height, Body body) {
		super(width, height, body, "Crystal");

		this.FRAME_TIME = 1 / 8f;
		this.icon = new Image(new Texture("hud/crystal.png"));

		String[] state = { "idle", "loot" };
		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "crystal", state[i], "");
		}

		this.isLooted = false;

		this.animationHandler.setActionDirection("idle", "", true);
	}

}
