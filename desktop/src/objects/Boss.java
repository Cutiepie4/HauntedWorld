package objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import main.Boot;
import screen.GameScreen;

public class Boss extends Enemy {

	public static Boss INSTANCE;
	public static boolean trapButton = false;
	private Laser laser;
	private ArrayList<Bullet> bullets, toRemove;
	private boolean isReloaded = false; // switch reload bullets
	private boolean flip; // true is left, false is right

	public Boss(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;

		this.bullets = new ArrayList<>();

		this.toRemove = new ArrayList<>();

		this.FRAME_TIME = 1 / 10f;

		this.name = "Boss";

		String[] state = { "idle", "dead", "castlaser", "casttrap", "castbullet" };

		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "boss", state[i], "");
		}

		this.health = 100;
		
		this.MAX_HEALTH = 100;

		this.damage = 5;

		this.body.setLinearDamping(1000f);

		this.animationHandler.setActionDirection("idle", "", false);

		this.laser = null;

		this.flip = false;

		this.MAX_HEALTH = 100;

		this.healthBar = new Sprite(new Texture("hud/healthbar_enemy.png"));
		
	}

	public void reload() {

		bullets.add(new Bullet(0, 20, 90));

		bullets.add(new Bullet(0, -20, -90));

		bullets.add(new Bullet(20, 0, 0));

		bullets.add(new Bullet(-20, 0, 180));

		bullets.add(new Bullet(20, 20, 45));

		bullets.add(new Bullet(-20, 20, 135));

		bullets.add(new Bullet(20, -20, -45));

		bullets.add(new Bullet(-20, -20, -135));
	}

	public void update() {

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
		
		// update health bar position
		this.healthBar.setBounds(this.x - 40, this.y + 30, 80, 6);
		
		// set directions against player
		this.facingPlayer();

		if (this.animationHandler.getStateTime() >= 3) {
			this.animationHandler.setAction("idle", true);
			return;
		}

		if (this.animationHandler.getAction().equals("castlaser") && this.animationHandler.isAnimationFinished()) {
			this.laserActive();
		}

		this.disposeBullet();

		if (Boss.trapButton && this.animationHandler.getAction().equals("casttrap")
				&& this.animationHandler.isAnimationFinished()) {
			Trap.enableTrap();
			Boss.trapButton = false;
		}

	}

	@Override
	public void render(SpriteBatch batch) {

		update();

		this.showHealth(batch, 80, 6);

		TextureRegion currentFrame = this.animationHandler.getFrame();

		// render in facing player
		if (flip) {
			currentFrame.flip(true, false);
			batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
					currentFrame.getRegionHeight() * 0.75f);
			currentFrame.flip(true, false);
		}

		else {
			batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
					currentFrame.getRegionHeight() * 0.75f);
		}

		if (this.isReloaded && this.animationHandler.getAction().equals("castbullet")
				&& this.animationHandler.isAnimationFinished()) {
			this.reload();
			this.isReloaded = false;
		}

		// update bullets
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

		// render laser
		if (this.laser != null)
			this.laser.render(batch);
	}

	private void facingPlayer() {
		if (this.detected) {
			if (this.x > Player.INSTANCE.getX()) {
				this.flip = true;
			} else
				this.flip = false;
		}

	}

	// BULLET
	public void bulletActive() {
		this.animationHandler.setAction("castbullet", false);
		this.isReloaded = true;
	}

	public void disposeBullet() {
		for (Bullet i : toRemove) {
			bullets.remove(i);
		}
		toRemove.clear();
	}

	// TRAP
	public void trapActive() {
		Boss.trapButton = true;
		this.animationHandler.setAction("casttrap", false);
	}

	// LASER
	public void laserActive() {
		if (this.laser == null)
			this.laser = new Laser(0, 70, -90);

	}

	public void disposeLaser() {
		GameScreen.INSTANCE.getWorld().destroyBody(this.laser.body);
		this.laser = null;
	}

}
