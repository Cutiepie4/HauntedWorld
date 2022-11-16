package character;

import com.badlogic.gdx.physics.box2d.Body;

import controller.AudioManager;
import main.Boot;
import model.Dropable;
import model.Items;
import screen.GameScreen;

public class Chest extends Items implements Dropable {

	public Chest(float width, float height, Body body) {
		super(width, height, body, "Chest");

	}

	@Override
	public void loot() {
		this.isLooted = true;
		AudioManager.INSTANCE.playSound("chestopen");
		this.animationHandler.setAction("loot", false);
	}

	@Override
	public void dropItem() {
		if (this.isDropped)
			return;
		new Items(this.x, this.y, 16, 12, "Gold Key");
		this.isDropped = true;
	}
}
