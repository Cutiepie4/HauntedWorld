package objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import helper.AnimationHandler;
import main.Boot;
import main.GameScreen;

public abstract class Objects {

	protected Sprite sprite;
	protected float x, y, velX, velY, speed;
	protected float width, height;
	protected Body body;
	protected AnimationHandler animationHandler;
	protected float FRAME_TIME;
	protected boolean isDisposed;
	protected float damage;

	public Objects(float width, float height, Body body) {
		this.body = body;
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
		this.width = width;
		this.height = height;
		this.velX = 0;
		this.velY = 0;
		this.speed = 0;
		this.animationHandler = new AnimationHandler();
		this.FRAME_TIME = 0f;
		this.isDisposed = false;
		this.damage = 0f;
	}

	public Objects() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.velX = 0;
		this.velY = 0;
		this.speed = 0;
		this.animationHandler = new AnimationHandler();
		this.FRAME_TIME = 0f;
		this.isDisposed = false;
	}

	public float getY() {
		return this.x;
	}

	public float getX() {
		return this.y;
	}

	public void update() {

	};

	public void render(SpriteBatch batch) {

	};

	public Body getBody() {
		return body;
	}

	public float getDamage() {
		return damage;
	}

}
