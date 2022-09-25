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
	private Laser laser;
	private ArrayList<Bullet> bullets, toRemove;

	public Boss(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;

		this.bullets = new ArrayList<>();

		this.toRemove = new ArrayList<>();

		this.laser = new Laser(0, 70, 90);

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

	}

	public void reload() {

		bullets.add(new Bullet(0, 30, 90));

		bullets.add(new Bullet(0, -30, -90));

		bullets.add(new Bullet(30, 0, 0));

		bullets.add(new Bullet(-30, 0, 180));

		bullets.add(new Bullet(30, 30, 45));

		bullets.add(new Bullet(-30, 30, 135));

		bullets.add(new Bullet(30, -30, -45));

		bullets.add(new Bullet(-30, -30, -135));
	}

	@Override
	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		for (Bullet i : toRemove) {
			bullets.remove(i);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		update();
		
		

		for (Bullet i : bullets) {
			if (i.isDisposed) {
				this.toRemove.add(i);
			} else {
				if (i.canRemove) {
					GameScreen.INSTANCE.addToRemove(i);
					i.isDisposed = true;
				} else
					i.render(batch);
			}
		}

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
				currentFrame.getRegionHeight() * 0.75f);
		
		laser.render(batch);
	}

	@Override
	public void setTarget(Player target) {
		this.target = target;
	}

	public void shoot() {
		
		laser.animationHandler.setAction("shoot", false);

		if (!this.animationHandler.getAction().equals("shoot")) {
			this.animationHandler.setAction("shoot", false);
		}
		
		this.reload();
	}

}
