package objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import helper.AnimationHandler;

public abstract class Enemy extends Objects {
	
	protected float MAX_HEALTH;
	protected boolean detected;
	protected int health;
	protected float speed;
	protected String name;
	protected Sprite healthBar;

	public Enemy(float width, float height, Body body) {
		super(width, height, body);

		this.name = "null";
		this.detected = false;
		this.health = 0;
		this.damage = 0f;
		this.speed = 0;
	}

	public void isHit(Player player) {
		if (this.animationHandler.getAction().equals("dead"))
			return;

		this.health -= player.getDamage();

		if (this.health > 0) {

			this.animationHandler.setAction("hit", false);

			// push back
			this.body.setLinearVelocity(new Vector2((this.body.getPosition().x - player.body.getPosition().x) * speed,
					(this.body.getPosition().y - player.body.getPosition().y) * speed));
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

	public AnimationHandler getAnimationHandler() {
		return this.animationHandler;
	}
	
	protected void showHealth(SpriteBatch batch, int MAX_WIDTH_IMAGE, int MAX_HEIGHT_IMAGE) {
		this.healthBar.setSize(MAX_WIDTH_IMAGE / this.MAX_HEALTH * this.health, MAX_HEIGHT_IMAGE);
		this.healthBar.draw(batch);
	}
	
}
