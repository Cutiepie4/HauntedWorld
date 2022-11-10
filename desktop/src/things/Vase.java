package things;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.Body;

import helper.AudioManager;
import helper.Constants;
import helper.Dropable;
import main.Boot;
import screen.GameScreen;

public class Vase extends Items implements Dropable {

	public Vase(float width, float height, Body body) {
		super(width, height, body, "Vase");

	}

	@Override
	public void loot() {
		this.animationHandler.setAction("loot", false);
		this.isLooted = true;
		AudioManager.INSTANCE.playSound("break");
	}

	@Override
	public void dropItem() {
		Random rnd = new Random();
		if (rnd.nextInt(100) < 100) { // rate drop items
			int idx = rnd.nextInt(Constants.ITEMS_DROP.length);
			new Items(this.x, this.y, 10, 10, Constants.ITEMS_DROP[idx]);
		}
	}
}
