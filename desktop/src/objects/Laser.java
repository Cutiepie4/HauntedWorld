package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import main.Boot;
import screen.GameScreen;

public class Laser extends Objects {

	public static Laser INSTANCE;

	public Laser(float x_offset, float y_offset, float angle) {
		super();

		INSTANCE = this;

		this.x = Boss.INSTANCE.x + x_offset;

		this.y = Boss.INSTANCE.x + y_offset;

		this.damage = 5;

		this.speed = (float) (Math.PI / 2f);

		this.animationHandler.add(1 / 10f, "laser", "shoot", "");

		this.animationHandler.setAction("shoot", false);

		this.createLaserHitBox(angle);

		this.createJoint();

	}

	public void createLaserHitBox(float angle) {

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;

		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
		this.body.setBullet(true);
		PolygonShape shape = new PolygonShape();

		// size 98 x 6
		shape.setAsBox(98 / Boot.PPM, 6 / Boot.PPM);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();

		body.setTransform(new Vector2(this.x / Boot.PPM, this.y / Boot.PPM), (float) (angle * Math.PI / 180f));
	}

	public void createJoint() {

		WeldJointDef jointDef = new WeldJointDef();

		jointDef.bodyA = Boss.INSTANCE.body;

		jointDef.bodyB = this.body;

		jointDef.collideConnected = true;

		jointDef.localAnchorA.set(0, Boss.INSTANCE.height / 2 / Boot.PPM);

		jointDef.localAnchorB.set(-90f / Boot.PPM, 0);

		GameScreen.INSTANCE.getWorld().createJoint(jointDef);

	}

	public void rotate() {
		this.body.setAngularVelocity(speed);
	}

	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		if (this.animationHandler.isAnimationFinished()) {
			this.rotate();
		}

		if (this.animationHandler.getStateTime() >= 5.4f && !this.isDisposed) {
			Boss.INSTANCE.disposeLaser();
			this.isDisposed = true;
		}

	}

	public void render(SpriteBatch batch) {

		update();

		if (this.isDisposed)
			return;

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, Boss.INSTANCE.x - 22, Boss.INSTANCE.y - 8, 22, 22, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight(), 0.75f, 0.75f, (float) Math.toDegrees(this.body.getAngle()));

	}

}
