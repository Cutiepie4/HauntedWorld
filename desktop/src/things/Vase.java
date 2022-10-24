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

		this.audio.addSound("audio/sound/vase/break.mp3");
		this.audio.load();
	}

	@Override
	public void update() {
		if (this.isLooted) {
			if (this.animationHandler.isAnimationFinished()) {
				GameScreen.INSTANCE.addToRemove(this);
				this.setDisposed(true);
				this.dropItem();
			}
			return;
		}

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
	}

	@Override
	public void loot() {
		this.animationHandler.setAction("loot", false);
		this.isLooted = true;
		this.audio.playSound("break");
	}

	@Override
	public void dropItem() {
		if (this.isDropped)
			return;
		Random rnd = new Random();
		if (rnd.nextInt(100) < 100) { // rate drop items
			int idx = rnd.nextInt(Constants.ITEMS_DROP.length);
			new Items(this.x, this.y, 10, 10, Constants.ITEMS_DROP[idx]);
		}
		this.isDropped = true;
	}
}
