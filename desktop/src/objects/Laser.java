package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import main.Boot;
import main.GameScreen;

public class Laser extends Objects {

	private float speed;
	private float x, y;
	public boolean canRemove, isDisposed;

	public Laser(float x, float y) {
		super(x, y);
		this.canRemove = false;

		this.isDisposed = false;

		this.x = x;

		this.y = y;

		this.speed = 50;

		this.animationHandler.add(1 / 4f, "laser", "shoot", "");

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;
		bodyDef.position.set(x / Boot.PPM, y / Boot.PPM);

		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);

		this.body.setBullet(true);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(3 / Boot.PPM, 9 / Boot.PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();
//		this.body.setLinearVelocity(new Vector2(x, y).scl((float) 1 / 5 / Boot.PPM));
//		this.body.setTransform(Boss.INSTANCE.getX(), Boss.INSTANCE.getY(), 90);
		
	}

	public void setTarget(Vector2 vt2) {
		this.body.setLinearVelocity(vt2);
	}

	public void update(float delta) {

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		if (y > 50 * Boot.PPM || x > 50 * Boot.PPM || x < 0 || y < 0)
			this.canRemove = true;

		if (this.isDisposed) {
			this.x = 1000;
			this.y = 1000;
		}
	}

	public void render(SpriteBatch batch) {

		update(speed);

		TextureRegion currentFrame = this.animationHandler.getFrame();
		

		batch.draw(currentFrame, this.x - this.width, this.y - this.height, currentFrame.getRegionWidth() * 0.75f,
				currentFrame.getRegionHeight() * 0.75f);
	}

}
