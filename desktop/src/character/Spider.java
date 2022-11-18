package character;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import controller.BodyHelperService;
import controller.Constants;
import main.Boot;
import model.Enemy;
import model.Items;
import screen.GameScreen;

public class Spider extends Enemy {
	public Spider(float width, float height, Body body) {

		super(width, height, body);

		this.FRAME_TIME = 1 / 8f;

		this.name = "Spider";

		for (String i : Constants.ACTION) {
			this.animationHandler.add(FRAME_TIME, "spider", i, "");
		}

		this.health = 10;

		this.MAX_HEALTH = 10;

		this.damage = 4;

		this.body.setLinearDamping(8f);

		this.animationHandler.setActionDirection("idle", "", true);

		this.speed = 5f;

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
