package objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import main.Boot;
import main.GameScreen;

public class Boss extends Enemy {

	public static Boss INSTANCE;
	private Laser laser;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	public Boss(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;

		this.FRAME_TIME = 1 / 5f;

		this.name = "Boss";

		String[] state = { "idle", "dead", "shoot", "charging" };

		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "boss", state[i], "");
		}

		this.health = 100;

		this.damage = 5;

		this.getBody().setLinearDamping(1000f);

		this.animationHandler.setDirection("");

		this.animationHandler.setAction("idle", true);

//		this.speed = 2f;

		update();

//		this.laser = new Laser(this.x, this.y);
	}

	public void reload() {
		bullets.add(new Bullet(this.x + 10, this.y + 10));
	}

	@Override
	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		shoot();

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
		if (this.animationHandler.getAction().equals("shoot")) {

		}

		this.reload();
	}

}