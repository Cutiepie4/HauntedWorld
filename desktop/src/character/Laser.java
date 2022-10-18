package character;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import main.Boot;
import screen.GameScreen;
import things.Objects;

public class Laser extends Objects {

	public Laser(float x_offset, float y_offset, float angle) {
		super(Boss.INSTANCE.getX() + x_offset, Boss.INSTANCE.getX() + y_offset, 72, 6, createLaserHitBox(angle));
		this.damage = 5;
		this.speed = (float) (Math.PI / 2f);
		this.animationHandler.add(1 / 10f, "laser", "shoot", "");
		this.animationHandler.setAction("shoot", false);
		this.createJoint();
		this.body.getFixtureList().first().setUserData(this);
		this.body.setTransform(new Vector2(this.x / Boot.PPM, this.y / Boot.PPM), (float) (angle * Math.PI / 180f));

		GameScreen.INSTANCE.addObjects(this);
	}

	private static Body createLaserHitBox(float angle) {

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;

		Body body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
		PolygonShape shape = new PolygonShape();

		// size 72 x 6
		shape.setAsBox(72 / Boot.PPM, 6 / Boot.PPM);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;

		body.createFixture(fixtureDef);

		shape.dispose();

		return body;
	}

	private void createJoint() {

		WeldJointDef jointDef = new WeldJointDef();

		jointDef.bodyA = Boss.INSTANCE.getBody();

		jointDef.bodyB = this.body;

		jointDef.collideConnected = true;

		jointDef.localAnchorA.set(0, Boss.INSTANCE.getHeight() / 2 / Boot.PPM);

		jointDef.localAnchorB.set(-64f / Boot.PPM, 0);

		GameScreen.INSTANCE.getWorld().createJoint(jointDef);

	}

	private void rotate() {
		if (this.body.getAngularVelocity() == 0) {
			int[] clockwise = new int[] { -1, 1 };
			int rnd = new Random().nextInt(2);
			this.body.setAngularVelocity(clockwise[rnd] * speed);
		}
	}

	public void update() {
		if (this.isDisposed())
			return;

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		if (this.animationHandler.isAnimationFinished()) {
			this.rotate();
		}

		if (this.animationHandler.getStateTime() >= 5.4f && !this.isDisposed()) {
			GameScreen.INSTANCE.addToRemove(this);
			this.setDisposed(true);
			Boss.INSTANCE.getAnimationHandler().setAction("idle", true);
		}

	}

	public void render(SpriteBatch batch) {

		update();

		if (this.isDisposed())
			return;

		TextureRegion currentFrame = this.animationHandler.getFrame();

		currentFrame.setRegionWidth(200);

		batch.draw(currentFrame, Boss.INSTANCE.getY() - 22, Boss.INSTANCE.getY() - 8, 22, 22,
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), 0.75f, 0.75f,
				(float) Math.toDegrees(this.body.getAngle()));

	}

}
