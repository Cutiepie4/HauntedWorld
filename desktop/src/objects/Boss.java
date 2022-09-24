package objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import main.Boot;
import main.GameScreen;

public class Boss extends Enemy {

	public static Boss INSTANCE;
	public Vector2 center;
//	private Laser laser;
	private ArrayList<Bullet> bullets;

	public Boss(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;

		this.bullets = new ArrayList<Bullet>();

		this.FRAME_TIME = 1 / 5f;

		this.name = "Boss";

		String[] state = { "idle", "dead", "shoot", "charging" };

		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "boss", state[i], "");
		}

		this.health = 100;

		this.damage = 5;

		this.body.setLinearDamping(1000f);

		this.animationHandler.setDirection("");

		this.animationHandler.setAction("charging", true);

//		this.laser = new Laser(this.x, this.y);

	}

	public void reload() {

		bullets.add(new Bullet(0, 30, 90));

		bullets.add(new Bullet(0, -30, 90));

		bullets.add(new Bullet(30, 0, 0));

		bullets.add(new Bullet(-30, 0, 0));

		bullets.add(new Bullet(30, 30, 45));

		bullets.add(new Bullet(-30, 30, 135));

		bullets.add(new Bullet(30, -30, 135));
		
		bullets.add(new Bullet(-30, -30, 45));
	}

	@Override
	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
	}

	@Override
	public void render(SpriteBatch batch) {
		update();

//		this.laser.render(batch);

		for (Bullet i : bullets) {
			if (i.canRemove && !i.isDisposed) {
				GameScreen.INSTANCE.addToRemove(i);
				i.isDisposed = true;

			} else
				i.render(batch);
		}

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
				currentFrame.getRegionHeight() * 0.75f);
	}

	@Override
	public void setTarget(Player target) {
		this.target = target;
	}

	public void shoot() {
		if (!this.animationHandler.getAction().equals("shoot")) {
			this.animationHandler.setAction("shoot", false);
		}

		this.reload();
	}

}
