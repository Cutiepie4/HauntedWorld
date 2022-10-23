package character;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import helper.Dropable;
import main.Boot;

public class Boss extends Enemy implements Dropable {

	public static Boss INSTANCE;
	public Array<Bullet> listBullets = new Array<>();
	public Laser laser = null;
	public boolean isLasering = false;
	public boolean isTrapping = false;
	public boolean isFiring = false;
	private boolean flip = false; // true is left, false is right
	private float timer = 0f;

	public Boss(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;

		this.FRAME_TIME = 1 / 10f;

		this.name = "Boss";

		String[] state = { "idle", "dead", "castlaser", "casttrap", "castbullet", "hit" };

		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "boss", state[i], "");
		}

		this.health = 85;

		this.MAX_HEALTH = 100;

		this.damage = 5f;

		this.animationHandler.setActionDirection("idle", "", true);

		this.initBullets();
	}

	private void initBullets() {

		this.listBullets.add(new Bullet(0, 10, 90));

		this.listBullets.add(new Bullet(0, -10, -90));

		this.listBullets.add(new Bullet(10, 0, 0));

		this.listBullets.add(new Bullet(-10, 0, 180));

		this.listBullets.add(new Bullet(10, 10, 45));

		this.listBullets.add(new Bullet(-10, 10, 135));

		this.listBullets.add(new Bullet(10, -10, -45));

		this.listBullets.add(new Bullet(-10, -10, -135));
	}

	@Override
	public void update() {

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		timer += Gdx.graphics.getDeltaTime();

		this.body.setAwake(true);

		// set directions facing player
		this.facingPlayer();

		// update health bar position
		this.healthBar.setPosition(this.x - 42, this.y + 30);
		this.healthLevel.setPosition(this.x - 42, this.y + 30);

		if ((this.animationHandler.getAction().equals("castbullet") || this.animationHandler.getAction().equals("hit"))
				&& this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("idle", true);
		}

		if (this.detected) {
			if(!this.isLasering && !this.isTrapping && this.timer > 8f) { 
				this.attack();
			}
			
			else if(this.timer > 10f) {
				this.attack();
			}
		}

		if (this.isTrapping && this.animationHandler.getAction().equals("casttrap")) {
			if (this.animationHandler.isAnimationFinished())
				Trap.enableTrap();
			return;
		}

		if (this.isLasering) {
			this.animationHandler.setAction("castlaser", false);
			return;
		}

	}

	private void drawBoss(SpriteBatch batch) {
		TextureRegion currentFrame = this.animationHandler.getFrame();

		// render in facing player
		if (flip && !this.animationHandler.getAction().equals("dead")) {
			currentFrame.flip(true, false);
			batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
					currentFrame.getRegionHeight() * 0.75f);
			currentFrame.flip(true, false);
		}

		else {
			batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
					currentFrame.getRegionHeight() * 0.75f);
		}
	}

	@Override
	public void render(SpriteBatch batch) {

		update();

		this.drawBullet(batch);

		this.drawBoss(batch);

		this.showHealth(batch, 80, 6);

		this.drawLaser(batch);
	}

	@Override
	public void isHit(Player player) {

		if (this.animationHandler.getAction().equals("dead"))
			return;

		this.health -= player.getDamage();

		if (this.isLasering || this.isTrapping)
			return;

		if (this.health > 0) {
			if (this.animationHandler.getAction().equals("idle"))
				this.animationHandler.setAction("hit", false);

		}

		else if (!this.animationHandler.getAction().equals("dead")) {
			this.animationHandler.setAction("dead", false);
		}
	}

	private void facingPlayer() {
		if (this.detected) {
			if (this.x > Player.INSTANCE.getX()) {
				this.flip = true;
			} else
				this.flip = false;
		}
	}

	@Override
	public void dropItem() {

	}

	public void attack() {
		if (!this.animationHandler.getAction().equals("idle"))
			return;

		this.timer = 0f;
//		this.laserActive();
//		this.bulletActive();
//		this.trapActive();

		Random rnd = new Random();

		int nextAction = rnd.nextInt(3);
		switch (nextAction) {
		case 0:
			this.laserActive();
			break;
		case 1:
			this.trapActive();
			break;
		case 2:
			this.bulletActive();
			break;
		}

	}

	// TRAP
	public void trapActive() {
		this.isTrapping = true;
		this.animationHandler.setAction("casttrap", false);
	}

	// LASER
	public void laserActive() {
		if (!this.isLasering) {
			this.laser = new Laser(0, 70, 90);
//			this.animationHandler.setAction("castlaser", false);
			this.isLasering = true;
		}
	}

	private void drawLaser(SpriteBatch batch) {
		if (this.isLasering) {
			this.laser.render(batch);
		}
	}

	// BULLET
	public void bulletActive() {
		this.animationHandler.setAction("castbullet", false);
		this.restoreBullets();
	}

	private void restoreBullets() {
		for (Bullet i : this.listBullets) {
			i.reset(0);
		}
	}

	private void drawBullet(SpriteBatch batch) {
		for (Bullet i : this.listBullets) {
			i.render(batch);
		}
	}
}
