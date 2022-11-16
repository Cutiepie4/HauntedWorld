package model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import character.Player;
import controller.AnimationHandler;
import controller.Constants;
import main.Boot;
import screen.GameScreen;

public class Entity {

	protected Sprite sprite;
	protected float x, y, velX, velY, speed;
	protected float width, height;
	protected Body body;
	protected AnimationHandler animationHandler;
	protected float FRAME_TIME;
	public boolean isDisposed = false;
	protected float damage;
	protected String name;
	public boolean isDropped = false;

	public Entity(float x, float y, float width, float height, Body body) {
		this.body = body;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.velX = 0;
		this.velY = 0;
		this.speed = 0;
		this.animationHandler = new AnimationHandler();
		this.FRAME_TIME = 0f;
		this.damage = 0f;
		this.name = "null";
	}

	public Entity(float width, float height, Body body) {
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
		this.damage = 0f;
		this.name = "null";
	}

	public Entity(float width, float height, Body body, String name) {
		this.body = body;
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
		this.width = width;
		this.height = height;
		this.velX = 0;
		this.velY = 0;
		this.speed = 0;
		this.animationHandler = new AnimationHandler();
		this.FRAME_TIME = Constants.getFRAME_TIME(name);
		this.damage = 0f;
		this.name = name;
	}

	public Entity() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.body = null;
		this.velX = 0;
		this.velY = 0;
		this.speed = 0;
		this.animationHandler = new AnimationHandler();
		this.FRAME_TIME = 0f;
		this.name = "null";
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getY() {
		return this.y;
	}

	public float getX() {
		return this.x;
	}

	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;
	}

	public void render(SpriteBatch batch) {
		if (this.isDisposed)
			return;

		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2, this.y - this.height / 2, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());
	}

	public Body getBody() {
		return body;
	}

	public float getDamage() {
		return damage;
	}

	public AnimationHandler getAnimationHandler() {
		return animationHandler;
	}

	public String getName() {
		return this.name;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public boolean shouldDraw() {
		if (Math.abs(this.x - Player.INSTANCE.getX()) >= 250 || Math.abs(this.y - Player.INSTANCE.getY()) >= 250) {
			return false;
		}
		return true;
	}
}
