package character;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import controller.Constants;
import main.Boot;
import model.Enemy;
import model.Items;
import screen.GameScreen;

public class Spinner extends Enemy {

	public Spinner(float width, float height, Body body) {

		super(width, height, body);

		this.FRAME_TIME = 1 / 8f;

		this.name = "Spinner";

		for (String i : Constants.ACTION) {
			this.animationHandler.add(FRAME_TIME, "spinner", i, "");
		}

		this.health = 6;

		this.MAX_HEALTH = 6;

		this.damage = 2;

		this.body.setLinearDamping(8f);

		this.animationHandler.setActionDirection("idle", "", true);

		this.speed = 2f;

	}

	@Override
	public void dropItem() {
		Random rnd = new Random();
		if (rnd.nextInt(100) < 25) { // rate drop items
			int idx = rnd.nextInt(Constants.ITEMS_DROP.length);
			new Items(this.x, this.y, 10, 10, Constants.ITEMS_DROP[idx]);
		}
	}

}
