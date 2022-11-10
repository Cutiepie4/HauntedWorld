package character;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import helper.BodyHelperService;
import helper.Constants;
import main.Boot;
import screen.GameScreen;
import things.Items;

public class Spider extends Enemy {
	public Spider(float width, float height, Body body) {

		super(width, height, body);

		this.FRAME_TIME = 1 / 8f;

		this.name = "Spider";

		for (String i : Constants.ACTION) {
			this.animationHandler.add(FRAME_TIME, "spider", i, "");
		}

		this.health = 3;

		this.MAX_HEALTH = 8;

		this.damage = 3;

		this.body.setLinearDamping(8f);

		this.animationHandler.setActionDirection("idle", "", true);

		this.speed = 3f;

	}

	@Override
	public void dropItem() {
		Random rnd = new Random();
		if (rnd.nextInt(100) < 40) { // rate drop items
			int idx = rnd.nextInt(Constants.ITEMS_DROP.length);
			new Items(this.x, this.y, 10, 10, Constants.ITEMS_DROP[idx]);
		}
	}
}
