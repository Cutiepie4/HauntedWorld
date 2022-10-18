package character;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import helper.Dropable;
import main.Boot;
import screen.GameScreen;

public class Boss extends Enemy implements Dropable {

	public static Boss INSTANCE;
	public static boolean trapButton = false;
	private ArrayList<Bullet> bullets, toRemove;
	private boolean isReloaded = false; // switch reload bullets
	private boolean flip; // true is left, false is right
	private float timer;

	public Boss(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;

		this.bullets = new ArrayList<>();

		this.toRemove = new ArrayList<>();

		this.FRAME_TIME = 1 / 10f;

		this.name = "Boss";

		String[] state = { "idle", "dead", "castlaser", "casttrap", "castbullet", "hit" };

		for (int i = 0; i < state.length; i++) {
			this.animationHandler.add(FRAME_TIME, "boss", state[i], "");
		}

		this.health = 60;

		this.MAX_HEALTH = 100;

		this.damage = 5;

		this.animationHandler.setActionDirection("idle", "", true);

		this.flip = false;
	}

	private void reloadBullets() {

		bullets.add(new Bullet(0, 20, 90));

		bullets.add(new Bullet(0, -20, -90));

		bullets.add(new Bullet(20, 0, 0));

		bullets.add(new Bullet(-20, 0, 180));

		bullets.add(new Bullet(20, 20, 45));

		bullets.add(new Bullet(-20, 20, 135));

		bullets.add(new Bullet(20, -20, -45));

		bullets.add(new Bullet(-20, -20, -135));
	}

	@Override
	public void update() {

		// set directions against player
		this.facingPlayer();

		this.disposeBullet();

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		// update health bar position
		this.healthBar.setPosition(this.x - 42, this.y + 30);
		this.healthLevel.setPosition(this.x - 42, this.y + 30);

		if (this.animationHandler.getAction().equals("hit") && this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("idle", true);
		}

		if (Boss.trapButton && this.animationHandler.getAction().equals("casttrap")
				&& this.animationHandler.isAnimationFinished()) {
			Trap.enableTrap();
			Boss.trapButton = false;
		}

		if (this.detected) {
			this.attack();
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

	private void drawBullet(SpriteBatch batch) {
		if (this.isReloaded && this.animationHandler.getAction().equals("castbullet")
				&& this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("idle", true);
			this.reloadBullets();
			this.isReloaded = false;
		}

		// update bullets
		for (Bullet i : bullets) {
			if (i.isDisposed()) {
				this.toRemove.add(i);
			} else {
				if (i.canRemove) {
					GameScreen.INSTANCE.addToRemove(i);
					i.setDisposed(true);
				} else
					i.render(batch);
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {

		timer += Gdx.graphics.getDeltaTime();

		update();

		this.drawBoss(batch);

		this.showHealth(batch, 80, 6);

		this.drawBullet(batch);

	}

	@Override
	public void isHit(Player player) {
		if (this.animationHandler.getAction().equals("dead"))
			return;

		this.health -= player.getDamage();

		if (this.health > 0) {
			if (!this.animationHandler.getAction().equals("castlaser"))
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

	// BULLET
	public void bulletActive() {
		this.animationHandler.setAction("castbullet", false);
		this.isReloaded = true;
	}

	private void disposeBullet() {
		for (Bullet i : toRemove) {
			bullets.remove(i);
			i = null;
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
		new Laser(0, 70, -90);
		this.animationHandler.setAction("castlaser", false);
	}

	@Override
	public void dropItem() {
		// TODO Auto-generated method stub

	}

	public void attack() {
		if (!this.animationHandler.getAction().equals("idle") || this.timer < 10f)
			return;
		
		this.laserActive();

//		Random rnd = new Random();
//
//		int nextAction = rnd.nextInt(3);
//		switch (nextAction) {
//		case 0:
//			this.laserActive();
//			this.timer = 0f;
//			break;
//		case 1:
//			this.trapActive();
//			this.timer = 0f;
//			break;
//		case 2:
//			this.timer = 0f;
//			this.bulletActive();
//			break;
//		}

	}

}
