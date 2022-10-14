package things;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import helper.AnimationHandler;
import helper.Constants;
import main.Boot;
import screen.GameScreen;

public class Objects {

	protected Sprite sprite;
	protected float x, y, velX, velY, speed;
	protected float width, height;
	protected Body body;
	protected AnimationHandler animationHandler;
	protected float FRAME_TIME;
	private boolean isDisposed;
	protected float damage;
	protected String name;

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
		this.setDisposed(false);
		this.damage = 0f;
		this.name = "";
	}

	public Objects(float width, float height, Body body, String name) {
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
		this.setDisposed(false);
		this.damage = 0f;
		this.name = name;
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
		this.setDisposed(false);
		this.name = "";
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

	public boolean isDisposed() {
		return isDisposed;
	}

	public void setDisposed(boolean isDisposed) {
		this.isDisposed = isDisposed;
	}

	public AnimationHandler getAnimationHandler() {
		return animationHandler;
	}
}
