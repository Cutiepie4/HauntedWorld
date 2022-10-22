package character;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import helper.Dropable;
import main.Boot;
import screen.GameScreen;

public class Boss extends Enemy implements Dropable {

	public static Laser LASER = null;
	public static boolean lasering = false;
	public static Boss INSTANCE;
	public static boolean trapButton = false;
	public static ArrayList<Bullet> listBullets = new ArrayList<>();
	private boolean flip = false; // true is left, false is right
	private float timer;

	public Boss(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;

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
	}

	private void reloadBullets() {

		Boss.listBullets.add(new Bullet(0, 20, 90));

		Boss.listBullets.add(new Bullet(0, -20, -90));

		Boss.listBullets.add(new Bullet(20, 0, 0));

		Boss.listBullets.add(new Bullet(-20, 0, 180));

		Boss.listBullets.add(new Bullet(20, 20, 45));

		Boss.listBullets.add(new Bullet(-20, 20, 135));

		Boss.listBullets.add(new Bullet(20, -20, -45));

		Boss.listBullets.add(new Bullet(-20, -20, -135));
	}

	@Override
	public void update() {

		// set directions facing player
		this.facingPlayer();

		this.updateBullet();

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		// update health bar position
		this.healthBar.setPosition(this.x - 42, this.y + 30);
		this.healthLevel.setPosition(this.x - 42, this.y + 30);

		if ((this.animationHandler.getAction().equals("hit") || this.animationHandler.getAction().equals("castbullet"))
				&& this.animationHandler.isAnimationFinished()) {
			this.animationHandler.setAction("idle", true);
		}

//		if (Boss.trapButton && this.animationHandler.getAction().equals("casttrap")
//				&& this.animationHandler.isAnimationFinished()) {
//			Trap.enableTrap();
//			Boss.trapButton = false;
//			return;
//		}

		if (this.detected && this.timer > 8f) {
			this.attack();
			this.timer = 0f;
		}

	}

	private void updateBullet() {
		ArrayList<Bullet> temp = new ArrayList<>();
		for (Bullet i : Boss.listBullets) {
			if (!i.canRemove) {
				temp.add(i);
			} else {
				GameScreen.INSTANCE.addToRemove(i);
			}
		}
		Boss.listBullets = temp;
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
		for (Bullet i : Boss.listBullets) {
			i.render(batch);
		}
	}

	private void drawLaser(SpriteBatch batch) {
		if (Boss.lasering) {
			Boss.LASER.render(batch);
		}
	}

	@Override
	public void render(SpriteBatch batch) {

		timer += Gdx.graphics.getDeltaTime();

		update();

		this.drawBoss(batch);

		this.showHealth(batch, 80, 6);

		this.drawBullet(batch);

		this.drawLaser(batch);
	}

	@Override
	public void isHit(Player player) {
		if (this.animationHandler.getAction().equals("dead"))
			return;

		this.health -= player.getDamage();

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

	// BULLET
	public void bulletActive() {
		this.animationHandler.setAction("castbullet", false);
		this.reloadBullets();
	}

	// TRAP
	public void trapActive() {
		Boss.trapButton = true;
		this.animationHandler.setAction("casttrap", false);
	}

	// LASER
	public void laserActive() {
		if (!Boss.lasering) {
			Boss.LASER = new Laser(0, 70, -90);
			this.animationHandler.setAction("castlaser", false);
			Boss.lasering = true;
		}
	}

	@Override
	public void dropItem() {

	}

	public void attack() {
		if (!this.animationHandler.getAction().equals("idle"))
			return;

//		this.laserActive();
		if (Boss.listBullets.isEmpty())
			this.bulletActive();

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
