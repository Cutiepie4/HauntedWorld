package character;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import helper.AnimationHandler;
import helper.Dropable;
import main.Boot;
import screen.GameScreen;
import things.Entity;

public abstract class Enemy extends Entity implements Dropable {

	protected float MAX_HEALTH;
	protected boolean detected;
	protected float health;
	protected float speed;
	protected String name;
	protected Sprite healthLevel, healthBar;

	public Enemy(float width, float height, Body body) {
		super(width, height, body);

		this.name = "enemy";
		this.detected = false;
		this.health = 0;
		this.damage = 0f;
		this.speed = 0;
		this.healthLevel = new Sprite(new Texture("hud/health_level_enemy.png"));
		this.healthBar = new Sprite(new Texture("hud/health_bar_enemy.png"));
	}

	public void isHit(Player player) {
		if (this.animationHandler.getAction().equals("dead"))
			return;
		
		Player.INSTANCE.getAudio().playSound("slash");
		this.health -= player.getDamage();

		if (this.health > 0) {

			this.animationHandler.setAction("hit", false);

			// push back
			this.body.setLinearVelocity(
					new Vector2((this.body.getPosition().x - player.getBody().getPosition().x) * speed,
							(this.body.getPosition().y - player.getBody().getPosition().y) * speed));
		}

		else if (!this.animationHandler.getAction().equals("dead")) {
			this.animationHandler.setAction("dead", false);
		}
	}

	public void detectPlayer() {
		this.detected = true;
	}

	public void lostPlayer() {
		this.detected = false;
	}

	protected void showHealth(SpriteBatch batch, float MAX_WIDTH_IMAGE, float MAX_HEIGHT_IMAGE) {
		this.healthBar.setSize(MAX_WIDTH_IMAGE, MAX_HEIGHT_IMAGE);
		this.healthBar.draw(batch);
		this.healthLevel.setSize(this.health / this.MAX_HEALTH * MAX_WIDTH_IMAGE, MAX_HEIGHT_IMAGE);
		this.healthLevel.draw(batch);
	}

	public void follow(Player player) {
		if (player != null) {
			this.body.setLinearVelocity(new Vector2(-(this.body.getPosition().x - player.getBody().getPosition().x),
					-(this.body.getPosition().y - player.getBody().getPosition().y)));

			this.animationHandler.setAction("attack", true);
		}
	}
	
	@Override
	public void update() {

		this.healthBar.setPosition(this.x - 10f, this.y + this.height);
		this.healthLevel.setPosition(this.x - 10f, this.y + this.height);

		if (this.animationHandler.getAction().equals("dead")) {
			this.body.getFixtureList().first().setSensor(true);
			if (this.animationHandler.isAnimationFinished()) {
				this.dropItem();
				GameScreen.INSTANCE.addToRemove(this);
			}
			return;
		}

		if (!this.detected) {
			if (this.animationHandler.isAnimationFinished()) {
				Random random = new Random();
				this.body.setLinearVelocity(random.nextInt(-1, 2) * speed, random.nextInt(-1, 2) * speed);
				this.animationHandler.setAction("idle", true);
				this.animationHandler.setStateTime(0f);
			}
		}

		else if (this.animationHandler.isAnimationFinished()) {
			this.follow(Player.INSTANCE);
		}

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		update();

		if (!this.animationHandler.getAction().equals("dead"))
			this.showHealth(batch, 20, 2f);

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2, this.y - this.height / 2,
				currentFrame.getRegionWidth() * 0.75f, currentFrame.getRegionHeight() * 0.75f);
	}

	@Override
	public abstract void dropItem();

}
