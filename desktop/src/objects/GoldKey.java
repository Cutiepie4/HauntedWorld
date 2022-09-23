package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GoldKey extends Key {

	public GoldKey(float width, float height, Body body) {
		super(width, height, body);
		this.name = "Gold Key";
		this.image = new Image(new Texture(Gdx.files.internal("hud/goldkey.png")));
		
		String[] state = { "idle", "loot" };
		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "goldkey", state[i], "");
		}
	}

}
